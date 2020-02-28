package com.sq.transportmanage.gateway.api.controller;

import com.alibaba.fastjson.JSON;
import com.sq.transportmanage.gateway.api.util.BeanUtil;
import com.sq.transportmanage.gateway.api.util.SmsSendUtil;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.CarAdmUserMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.CarAdmUserExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasPermissionExMapper;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.cache.RedisCacheUtil;
import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.common.dto.AjaxLoginUserDTO;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import com.sq.transportmanage.gateway.service.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.shiro.realm.UsernamePasswordRealm;
import com.sq.transportmanage.gateway.service.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.util.NumberUtil;
import com.sq.transportmanage.gateway.service.util.PasswordUtil;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @Value(value="${loginpage.url}")
    private String loginpageUrl;  //前端UI登录页面
	@Value("${homepage.url}")
	private String homepageUrl; //前端UI首页页面
	@Value("${login.checkMsgCode.switch}")
	private String loginCheckMsgCodeSwitch = "ON";//登录时是否进行短信验证的开关


	private static final String CACHE_PREFIX_MSGCODE_CONTROL = "mp_star_fire_login_cache_msgcode_control_";
	private static final String CACHE_PREFIX_MSGCODE                   = "mp_star_fire_login_cache_msgcode_";
	private int msgcodeTimeoutMinutes = 2;


	@Autowired
	private CarAdmUserMapper carAdmUserMapper;
	@Autowired
	private CarAdmUserExMapper carAdmUserExMapper;
	@Autowired
	private SaasPermissionExMapper saasPermissionExMapper;
	@Autowired
	private UsernamePasswordRealm usernamePasswordRealm;
	@Autowired
	private RedisSessionDAO redisSessionDAO;

	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;


    /**运维监控心跳检测 **/
    @RequestMapping("/nginx")
    public String nginx(HttpServletResponse response ) throws IOException{
    	response.getWriter().append("true");
    	response.getWriter().close();
        return null;
    }
	/**显示首页**/
    @RequestMapping("/index")
    public String index(HttpServletRequest request , HttpServletResponse response, Model model) throws Exception {
		logger.info(">>>>>>>>>>>>>>>>>跳转至首页（log4j桥接至logback成功！）");
		response.sendRedirect(homepageUrl);
		return null;
    }

    /**显示登录页面 **/
	@RequestMapping("/login")
    public String login(HttpServletRequest request , HttpServletResponse response) throws Exception{
		Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
		if(  isAjax  ) {
			AjaxResponse ajaxResponse = AjaxResponse.fail(RestErrorCode.HTTP_INVALID_SESSION);
			this.outJson(response, ajaxResponse);
		}else {
			response.sendRedirect(loginpageUrl);
		}
		return null;
	}

    /**显示无权限页面**/
    @ExceptionHandler(UnauthenticatedException.class)
	@RequestMapping("/unauthorized")
    public String unauthorized(HttpServletRequest request , HttpServletResponse response) throws Exception{
		Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
		if(  isAjax  ) {
			AjaxResponse ajaxResponse = AjaxResponse.fail(RestErrorCode.HTTP_FORBIDDEN);
			this.outJson(response, ajaxResponse);
		}else {
			response.sendRedirect(homepageUrl);
		}
		return null;
    }

    /**自定义实现404（利用SPRING的匹配优先级，最后是模糊匹配）**/
	@RequestMapping("/**")
    public String process404( HttpServletRequest request , HttpServletResponse response ) throws IOException{
		Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
		if(  isAjax  ) {
			AjaxResponse ajaxResponse = AjaxResponse.fail(RestErrorCode.HTTP_NOT_FOUND);
			this.outJson(response, ajaxResponse);
		}else {
			response.sendRedirect(homepageUrl);
			//return "notFound";//------------返回页面不存在的错误页
		}
		return null;
    }


	/**响应JSON数据**/
	private void outJson( HttpServletResponse response , AjaxResponse ajaxResponse ) {
		PrintWriter out = null;
		try{
			response.setStatus(HttpStatus.SC_OK);
			out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");
			response.setHeader("pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			out.write( JSON.toJSONString(ajaxResponse, true) );
			out.close();
		} catch (Exception e) {
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}




	/*通过用户名、密码，获取短信验证码*/
	@RequestMapping(value = "/getMsgCode" )
	@ResponseBody
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse getMsgCode( @Verify(param="username",rule="required") String username,
									@Verify(param="password",rule="required") String password ){
		//A: 频率检查 ,
		String redis_getmsgcode_key = "mp_manager_getmsgcode_key_"+username;
		long score = System.currentTimeMillis();
		//zset内部是按分数来排序的，这里用当前时间做分数
		redisTemplate.opsForZSet().add(redis_getmsgcode_key, String.valueOf(score), score);
		//统计30分钟内获取验证码次数
		int statistics = 30;
		redisTemplate.expire(redis_getmsgcode_key, statistics, TimeUnit.MINUTES);

		//统计用户30分钟内获取验证码次数
		long max = score;
		long min = max - (statistics * 60 * 1000);
		long count = redisTemplate.opsForZSet().count(redis_getmsgcode_key, min, max);

		int countLimit = 5;
		logger.info("获取验证码-用户"+username+"在"+statistics+"分钟内第"+count+"次进行获取验证码操作");
		if(count  > countLimit) {
			logger.info("获取验证码-用户"+username+"在"+statistics+"分钟内进行获取验证码"+count+"次,超过限制"+countLimit+",需要等待"+statistics+"分钟");
			return AjaxResponse.fail(RestErrorCode.GET_MSGCODE_EXCEED,statistics);
		}

//		String flag = RedisCacheUtil.get(CACHE_PREFIX_MSGCODE_CONTROL+username, String.class);
//		if(flag!=null ) {
//			return AjaxResponse.fail(RestErrorCode.GET_MSGCODE_EXCEED);
//		}
		//B:查询用户信息
		SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
		CarAdmUser user = carAdmUserExMapper.queryByAccount(username,loginUser.getUuid());
		if(user==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		//C:密码不正确
		String enc_pwd = PasswordUtil.md5(password, user.getAccount());
		if(!enc_pwd.equalsIgnoreCase(user.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.USER_PASSWORD_WRONG) ;
		}
		//D: 查询验证码，或新生成验证码，而后发送验证码短信
		String  msgcode = RedisCacheUtil.get(CACHE_PREFIX_MSGCODE+username, String.class);
		if(msgcode==null) {
			msgcode = NumberUtil.genRandomCode(6);
		}
		String mobile   = user.getPhone();
		String content  = "登录验证码为："+msgcode+"，请在"+msgcodeTimeoutMinutes+"分钟内进行登录。";
		SmsSendUtil.send(mobile, content);
		//E: 写入缓存
//		RedisCacheUtil.set(CACHE_PREFIX_MSGCODE_CONTROL+username, "Y",  60 );
		RedisCacheUtil.set(CACHE_PREFIX_MSGCODE+username, msgcode,  msgcodeTimeoutMinutes * 60 );
		//返回结果
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("timeout",  60 );//验证码有效的秒数
		result.put("tipText", "短信验证码已成功发送至尾号为"+mobile.substring(7)+"的手机上。" );//成功信息
		return AjaxResponse.success( result );
	}

	/*执行登录*/
	@RequestMapping(value = "/dologin" )
	@ResponseBody
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse dologin(HttpServletRequest request , HttpServletResponse response,
								@Verify(param="username",rule="required") String username,
								@Verify(param="password",rule="required") String password,
								@Verify(param="msgcode",rule="required") String msgcode ) throws IOException{

		String redis_login_key = "mp_manager_login_key_"+username;
		String redis_getmsgcode_key = "mp_manager_getmsgcode_key_"+username;

		Subject currentLoginUser = SecurityUtils.getSubject();
		//A:是否已经登录
		if(currentLoginUser.isAuthenticated()) {
			Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
			if(  isAjax  ) {
				return AjaxResponse.success( null );
			}else {
				response.sendRedirect(homepageUrl);
				return null;
			}
		}
		//B:查询用户信息
		CarAdmUser user = carAdmUserExMapper.queryByAccount(username,null);
		if(user==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		//C:密码不正确
		String enc_pwd = PasswordUtil.md5(password, user.getAccount());
		if(!enc_pwd.equalsIgnoreCase(user.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.USER_PASSWORD_WRONG) ;
		}
		//D: 查询验证码，并判断是否正确
		if("ON".equalsIgnoreCase(loginCheckMsgCodeSwitch)) {


			long score = System.currentTimeMillis();
			//zset内部是按分数来排序的，这里用当前时间做分数
			redisTemplate.opsForZSet().add(redis_login_key, String.valueOf(score), score);
			//统计30分钟内用户登录次数
			int statistics = 30;
			redisTemplate.expire(redis_login_key, statistics, TimeUnit.MINUTES);

			//统计用户30分钟内登录的次数
			long max = score;
			long min = max - (statistics * 60 * 1000);
			long count = redisTemplate.opsForZSet().count(redis_login_key, min, max);

			int countLimit = 5;
			logger.info("登录-用户"+username+"在"+statistics+"分钟内第"+count+"次登录");
			if(count  > countLimit) {
				logger.info("登录-用户"+username+"在"+statistics+"分钟内登录"+count+"次,超过限制"+countLimit+",需要等待"+statistics+"分钟");
				return AjaxResponse.fail(RestErrorCode.DO_LOGIN_FREQUENTLY,statistics);
			}
			//验证验证码是否正确
			String  msgcodeInCache = RedisCacheUtil.get(CACHE_PREFIX_MSGCODE+username, String.class);
			if(msgcodeInCache==null) {
				return AjaxResponse.fail(RestErrorCode.MSG_CODE_INVALID) ;
			}
			if(!msgcodeInCache.equals(msgcode)) {
				return AjaxResponse.fail(RestErrorCode.MSG_CODE_WRONG) ;
			}

		}
		//E: 用户状态
		if(user.getStatus()!=null && user.getStatus().intValue()==100 ){
			return AjaxResponse.fail(RestErrorCode.USER_INVALID) ;
		}
		//F: 执行登录
		try {
			//shiro登录
			UsernamePasswordToken token = new UsernamePasswordToken( username, password.toCharArray() );
			currentLoginUser.login(token);
			//记录登录用户的所有会话ID，以支持“系统管理”功能中的自动会话清理
			String sessionId =  (String)currentLoginUser.getSession().getId() ;
			redisSessionDAO.saveSessionIdOfLoginUser(username, sessionId);

			redisTemplate.delete(redis_login_key);
			redisTemplate.delete(redis_getmsgcode_key);

		}catch(AuthenticationException aex) {
			return AjaxResponse.fail(RestErrorCode.USER_LOGIN_FAILED) ;
		}
		//返回登录成功
		Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
		if(  isAjax  ) {
			return AjaxResponse.success( null );
		}else {
			response.sendRedirect(homepageUrl);
			return null;
		}
	}

	/*执行登出 */
	@RequestMapping("/dologout")
	@ResponseBody
	public AjaxResponse dologout( HttpServletRequest request , HttpServletResponse response ) throws Exception{
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()) {
			subject.logout();
		}
		Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
		if(  isAjax  ) {
			return AjaxResponse.success( null );
		}else {
			response.sendRedirect(loginpageUrl);
			return null;
		}
	}

	/*修改密码*/
	@RequestMapping("/changePassword")
	@ResponseBody
	public AjaxResponse changePassword( @Verify(param="oldPassword",rule="required") String oldPassword, @Verify(param="newPassword",rule="required") String newPassword ){
		SSOLoginUser ssoLoginUser  =  WebSessionUtil.getCurrentLoginUser();
		CarAdmUser   carAdmUser    =  carAdmUserMapper.selectByPrimaryKey( ssoLoginUser.getId()  );
		//A:用户不存在
		if(carAdmUser==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		//B:密码不正确
		String enc_pwd = PasswordUtil.md5(oldPassword, carAdmUser.getAccount());
		if(!enc_pwd.equalsIgnoreCase(carAdmUser.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.USER_PASSWORD_WRONG) ;
		}
		//C:执行
		String new_enc_pwd = PasswordUtil.md5(newPassword, carAdmUser.getAccount());
		CarAdmUser   carAdmUserForUpdate = new  CarAdmUser();
		carAdmUserForUpdate.setUserId(carAdmUser.getUserId());
		carAdmUserForUpdate.setPassword(new_enc_pwd);
		carAdmUserMapper.updateByPrimaryKeySelective(carAdmUserForUpdate);
		return AjaxResponse.success( null );
	}


	//-------------------------------------------------------------------------------------------------------------------------------------当前登录用户信息BEGIN
	@RequestMapping("/currentLoginUserInfo")
	@ResponseBody
	@SuppressWarnings("unchecked")
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse currentLoginUserInfo( String menuDataFormat ){
		SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();

		AjaxLoginUserDTO ajaxLoginUserDTO = new AjaxLoginUserDTO();
		//一、用户基本信息
		ajaxLoginUserDTO.setId(ssoLoginUser.getId());
		ajaxLoginUserDTO.setLoginName(ssoLoginUser.getLoginName());
		ajaxLoginUserDTO.setMobile(ssoLoginUser.getMobile());
		ajaxLoginUserDTO.setName(ssoLoginUser.getName());
		ajaxLoginUserDTO.setEmail(ssoLoginUser.getEmail());
		ajaxLoginUserDTO.setStatus(ssoLoginUser.getStatus());
		//二、用户的菜单信息      (  具有Session缓存机制 ，以提升性能   )
		if( !SaasConst.PermissionDataFormat.TREE.equalsIgnoreCase(menuDataFormat) && !SaasConst.PermissionDataFormat.LIST.equalsIgnoreCase(menuDataFormat) ) {
			menuDataFormat = SaasConst.PermissionDataFormat.TREE;//默认为树形
		}
		List<SaasPermissionDTO> menuPerms = (List<SaasPermissionDTO>)WebSessionUtil.getAttribute("xxx_menu_"+menuDataFormat);
		if( menuPerms ==null ) {
			List<Byte> permissionTypes =  Arrays.asList( new Byte[] { SaasConst.PermissionType.MENU });
			menuPerms = this.getAllPermissions( ssoLoginUser.getId()  , permissionTypes, menuDataFormat);
			if(menuPerms!=null && menuPerms.size()>0) {
				WebSessionUtil.setAttribute("xxx_menu_"+menuDataFormat, menuPerms);
			}
		}
		ajaxLoginUserDTO.setPermissionDTOS( menuPerms );

		//三、用户的权限信息 ( 参照shiro原码中的逻辑 )
		Subject subject = SecurityUtils.getSubject();
		subject.isPermitted("XXXX-XXXXXXXXX-XXXXXXXX-123456");//这里随意调用一下，确保shiro授权缓存已经被加载！！！
		PrincipalCollection principalCollection =subject.getPrincipals();
		if(principalCollection!=null) {
			Cache<Object, AuthorizationInfo> cache = usernamePasswordRealm.getAuthorizationCache();
			if(cache!=null) {
				AuthorizationInfo info = cache.get(  usernamePasswordRealm.getAuthorizationCacheKey(principalCollection)  );
				if(info!=null) {
					Collection<String> pemissionStrings = info.getStringPermissions();
					Collection<String> roles = info.getRoles();
					if(pemissionStrings!=null && pemissionStrings.size()>0 ) {
						ajaxLoginUserDTO.setHoldPerms( new  HashSet<String>( pemissionStrings )  );
					}
					if (roles != null && roles.size() > 0) {
						ajaxLoginUserDTO.setHoldRoles(new HashSet<String>(roles));
					}
				}
			}
		}
		//四、用户的数据权限
		ajaxLoginUserDTO.setCityIds( ssoLoginUser.getCityIds()  );
		ajaxLoginUserDTO.setSupplierIds( ssoLoginUser.getSupplierIds() );
		//ajaxLoginUserDTO.setTeamIds( ssoLoginUser.getTeamIds() );

		//五、配置信息
		Map<String, Object > configs = new HashMap<String,Object>();
		configs.put("mobileRegex",  SaasConst.MOBILE_REGEX);       //手机号码正则式
		configs.put("accountRegex", SaasConst.ACCOUNT_REGEX);  //账号的正则表达式
		configs.put("emailRegex",    SaasConst.EMAIL_REGEX);         //电子邮箱的正则表达式
		ajaxLoginUserDTO.setConfigs(configs);
		return AjaxResponse.success( ajaxLoginUserDTO );
	}

	/*五、查询一个用户的菜单（返回的数据格式：列表、树形）*/
	private List<SaasPermissionDTO> getAllPermissions( Integer userId,  List<Byte> permissionTypes,  String dataFormat ){
		List<Integer> validPermissionIdsOfCurrentLoginUser =  saasPermissionExMapper.queryPermissionIdsOfUser( userId ); //查询用户所拥有的所有有效的权限ID
		if(validPermissionIdsOfCurrentLoginUser==null || validPermissionIdsOfCurrentLoginUser.size()==0) {
			return null;
		}
		if(  SaasConst.PermissionDataFormat.LIST.equalsIgnoreCase(dataFormat) ) {
			return this.getAllPermissions_list( validPermissionIdsOfCurrentLoginUser, permissionTypes );
		}else if( SaasConst.PermissionDataFormat.TREE.equalsIgnoreCase(dataFormat) ) {
			return this.getAllPermissions_tree( validPermissionIdsOfCurrentLoginUser, permissionTypes );
		}
		return null;
	}
	/*返回的数据格式：列表*/
	private List<SaasPermissionDTO> getAllPermissions_list( List<Integer> permissionIds,  List<Byte> permissionTypes ){
		List<SaasPermission> allPos = saasPermissionExMapper.queryPermissions(permissionIds, null, null, permissionTypes, null, null);
		List<SaasPermissionDTO> allDtos = BeanUtil.copyList(allPos, SaasPermissionDTO.class);
		return allDtos;
	}
	/*返回的数据格式：树形*/
	private List<SaasPermissionDTO> getAllPermissions_tree( List<Integer> permissionIds,  List<Byte> permissionTypes ){
		return this.getChildren(permissionIds,  0 , permissionTypes);
	}
	private List<SaasPermissionDTO> getChildren( List<Integer> permissionIds,  Integer parentPermissionId,  List<Byte> permissionTypes ){
		List<SaasPermission> childrenPos = saasPermissionExMapper.queryPermissions(permissionIds, parentPermissionId, null, permissionTypes, null, null);
		if(childrenPos==null || childrenPos.size()==0) {
			return null;
		}
		//递归
		List<SaasPermissionDTO> childrenDtos = BeanUtil.copyList(childrenPos, SaasPermissionDTO.class);

		/*实时运力监控 权限过滤*/
		Iterator<SaasPermissionDTO> iterator = childrenDtos.iterator();
		while (iterator.hasNext()) {
			SaasPermissionDTO childrenDto = iterator.next();
			if (childrenDto.getPermissionCode().equals("Capacity") && !authCapacity()) {
				iterator.remove();
				continue;
			}
			List<SaasPermissionDTO> childs = this.getChildren( permissionIds, childrenDto.getPermissionId() ,  permissionTypes );
			childrenDto.setChildPermissions(childs);
		}
		return childrenDtos;
	}
	//-------------------------------------------------------------------------------------------------------------------------------------当前登录用户信息END





	public boolean authCapacity(){
		SSOLoginUser user = WebSessionUtil.getCurrentLoginUser();
		Set<Integer> userCityIds = user.getCityIds();
		if(userCityIds.isEmpty()){
			return true;
		}
		Set<String> authCityIdSet = getAuthCityId();
		for (String cityId : authCityIdSet) {
			if(userCityIds.contains(Integer.valueOf(cityId))){
				return true;
			}
		}
		return false;
	}


	public Set<String> getAuthCityId(){
		/*String authCityIdStr = Dicts.getString("driverMonitoring_authCityIdStr", "44,66,79,82,84,107,119,72,93,94,101,67,78,95,71,111,113,81,109,80,83");
		String[] strArray = authCityIdStr.split(",");
		List<String> strList =  Arrays.asList(strArray);
		Set<String> authCityIdSet = new HashSet<>(strList);
		return authCityIdSet;*/
		return null;
	}




}