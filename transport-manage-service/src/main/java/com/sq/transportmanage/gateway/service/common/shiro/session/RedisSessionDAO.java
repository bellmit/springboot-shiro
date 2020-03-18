package com.sq.transportmanage.gateway.service.common.shiro.session;

import com.sq.transportmanage.gateway.service.auth.MyDataSourceService;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.common.shiro.cache.RedisCache;
import com.sq.transportmanage.gateway.service.common.shiro.realm.UsernamePasswordRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**实现shiro分布式会话持久化 （基于REDIS），实现会话数据的CRUD操作
 *
 * 注意：保存在REDIS中的会话过期时间只要略大于shiro会话管理器中的globalSessionTimeout即可，设置太长的时效没有意义。
 * 此外，当权限信息、角色信息、用户信息发生变化时，可以同时自动清理与之相关联的会话
 * @author zhaoyali
 **/
@Component
public class RedisSessionDAO extends CachingSessionDAO{
	private static final Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
	private static final String KEY_PREFIX_OF_SESSION      = "sq_star_fire_sessionId_";        //KEY：会话ID，VALUE：shiro Session对象
	private static final String KEY_PREFIX_OF_SESSIONID   = "sq_star_fire_sessionIds_Of_"; //KEY：登录账户的KEY，VALUE：此账户的所有会话ID（以Set形式存储）


	@Autowired
	private MyDataSourceService myDataSourceService;
	@Autowired
	private RedisTemplate<String, Serializable>    redisTemplate;
	public void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}



	/*private AuthorizingRealm authorizingRealm;

	public void setAuthorizingRealm(AuthorizingRealm authorizingRealm) {
		this.authorizingRealm = authorizingRealm;
	}*/

	@Autowired
	@Qualifier("usernamePasswordRealm")
	private UsernamePasswordRealm authorizingRealm;

	public  void setAuthorizingRealm(UsernamePasswordRealm authorizingRealm) {
		this.authorizingRealm = authorizingRealm;
	}

	@Autowired
	private RedisCache activeSessions;

	public void setActiveSessions(RedisCache activeSessions) {
		this.activeSessions = activeSessions;
	}




	@Override
	protected void doUpdate(Session session) {
		redisTemplate.opsForValue().set(KEY_PREFIX_OF_SESSION+session.getId(), (Serializable)session,30,TimeUnit.MINUTES);
//        RedisCacheUtil.set(SESSION_KEY+session.getId(), session, 1*60*60 );
	}

	@Override
	protected void doDelete(Session session) {
		if (session == null || session.getId() == null) {
			return;
		}
		redisTemplate.delete(KEY_PREFIX_OF_SESSION+session.getId());
//        RedisCacheUtil.delete(SESSION_KEY+session.getId());
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		redisTemplate.opsForValue().set(KEY_PREFIX_OF_SESSION+sessionId, (Serializable)session,30,TimeUnit.MINUTES);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = (Session)redisTemplate.opsForValue().get(KEY_PREFIX_OF_SESSION+sessionId);
		return session;
	}

	/********************************************自动清理会话BEGIN****************************************************************************************************************/
	/**一、保存当前登录用户的会话ID**/
	@SuppressWarnings("unchecked")
	public void saveSessionIdOfLoginUser( String loginAccount, String sessionId ){
		Set<String> allSessionIds =  (Set<String>) redisTemplate.opsForValue().get(KEY_PREFIX_OF_SESSIONID+loginAccount);
		if(allSessionIds == null ) {
			allSessionIds =  new HashSet<String>(4);
		}
		allSessionIds.add(sessionId);
		redisTemplate.opsForValue().set(KEY_PREFIX_OF_SESSIONID+loginAccount, (Serializable)allSessionIds, 24, TimeUnit.HOURS);
	}
	/**二、当权限信息、角色信息、用户信息发生变化时，同时清理与之相关联的会话**/
	@MyDataSource(value = DataSourceType.DRIVERSPARK_MASTER)
	public void clearRelativeSession( final Integer permissionId, final  Integer roleId, final  Integer userId ) {
		//final Cache<Serializable, Session> cache = super.getActiveSessionsCache();
		final Cache<Serializable, Session> cache = activeSessions;
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try{
					//A：如果当权限发生变化时，查询所关联的全部角色ID
					List<Integer> roleIds = new ArrayList<Integer>();
					if( permissionId!=null ) {
						roleIds = myDataSourceService.queryRoleIdsOfPermission( permissionId );
					}
					//B：如果当角色发生变化时，查询所关联的用户ID
					if( roleId !=null ) {
						roleIds.add(roleId);
					}
					List<Integer> userIds = new ArrayList<Integer>();
					if( roleIds.size()>0 ) {
						userIds = myDataSourceService.queryUserIdsOfRole( roleIds );
					}
					//C：如果当用户发生变化时，查询出这些用户的登录账户名称
					if( userId != null ) {
						logger.info("当用户发生变化时清除缓存userId={}",userId);
						userIds.add(userId);
					}
					List<String> accounts = new ArrayList<String>();
					if(userIds.size()>0) {
						accounts = myDataSourceService.queryAccountsOfUsers(userIds);
					}
					//D：汇总需要清理的REDIS KEY 和 sessionId
					if(accounts.size() ==0) {
						return;
					}
					Set<String> redisKeysNeedDelete = new HashSet<String>();//这是需要清除的所有REDIS KEY
					Set<String> allSessionIds              = new HashSet<String>();//这是需要清除的所有的sessionId
					for( String account : accounts) {
						redisKeysNeedDelete.add( KEY_PREFIX_OF_SESSIONID + account );
						Set<String> sessionIds  =  (Set<String>) redisTemplate.opsForValue().get(KEY_PREFIX_OF_SESSIONID+account);
						if(sessionIds!=null && sessionIds.size()>0) {
							allSessionIds.addAll(sessionIds);
						}
					}

					//E1：执行清除执久化的会话(这里是保存在REDIS中的)
					for( String sessionId : allSessionIds) {
						logger.info("执行清除REDIS的会话缓存sessionId={}",sessionId);
						redisKeysNeedDelete.add( KEY_PREFIX_OF_SESSION + sessionId );
					}
					redisTemplate.delete(redisKeysNeedDelete);
					//E2：执行清理shiro会话缓存
					if(cache!=null) {
						for(String sessionId : allSessionIds ){
							SimpleSession session = (SimpleSession)cache.get(sessionId);
							if(session!=null) {
								session.setExpired(true);
							}
							logger.info("执行清理shiro会话缓存sessionId={}",sessionId);
							cache.remove(sessionId);
						}
					}
					//E3：执行清理shiro 认证与授权缓存
					for( String account : accounts) {
						logger.info("执行清理shiro 认证与授权缓存account={}",account);
						//todo 此处不合理,应该用下面的代码 这个是临时方案:执行退出的操作 相当于手动点击退出
						Subject subject = SecurityUtils.getSubject();
						if(subject.isAuthenticated()) {
							subject.logout();
						}

						/*SSOLoginUser principal  = new SSOLoginUser();
						principal.setLoginName(  account );

						SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection( );
						simplePrincipalCollection.add(principal, authorizingRealm.getName() );
						((UsernamePasswordRealm)authorizingRealm).clearCache( simplePrincipalCollection );*/
					}
				}catch(Exception ex) {
					logger.error("清除缓存异常",ex);
				}finally {
					//DynamicRoutingDataSource.setDefault("mdbcarmanage-DataSource");
				}
			}
		}).start();
	}
	/********************************************自动清理会话END******************************************************************************************************************/

}