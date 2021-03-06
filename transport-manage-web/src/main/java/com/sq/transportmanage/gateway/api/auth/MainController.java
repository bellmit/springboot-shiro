package com.sq.transportmanage.gateway.api.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.api.common.AuthEnum;
import com.sq.transportmanage.gateway.api.util.BeanUtil;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRole;
import com.sq.transportmanage.gateway.dao.entity.driverspark.base.Merchant;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.CarAdmUserMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.CarAdmUserExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasPermissionExMapper;
import com.sq.transportmanage.gateway.service.auth.SaasRoleService;
import com.sq.transportmanage.gateway.service.auth.SaasUserRoleRalationService;
import com.sq.transportmanage.gateway.service.base.BaseMerchantService;
import com.sq.transportmanage.gateway.service.base.BaseSupplierService;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.cache.RedisUtil;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.common.dto.AjaxLoginUserDTO;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.realm.UsernamePasswordRealm;
import com.sq.transportmanage.gateway.service.common.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RequestFunction;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import com.sq.transportmanage.gateway.service.util.NumberUtil;
import com.sq.transportmanage.gateway.service.util.PasswordUtil;
import com.sq.transportmanage.gateway.service.util.SmsSendUtil;
import com.sq.transportmanage.gateway.service.vo.BaseSupplierVo;
import com.zhuanche.http.MpOkHttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.sq.transportmanage.gateway.service.common.enums.MenuEnum.USER_RESET_PASSWORD;

/**
 * @author fanht
 */
@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    /**前端UI登录页面*/
	@Value(value="${loginpage.url}")
    private String loginpageUrl;
	/**前端UI首页页面*/
	@Value("${homepage.url}")
	private String homepageUrl;
	/**登录时是否进行短信验证的开关*/
	@Value("${login.checkMsgCode.switch}")
	private String loginCheckMsgCodeSwitch;

	@Value("${query.merchant.url}")
	private String queryMerchantUrl;

	@Value("${zuul.routes.mpapi.url}")
	private String zuulMpApiUrl;


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
	@Resource(name = "sessionDAO")
	private RedisSessionDAO redisSessionDAO;

	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;

	@Autowired
	private RedisUtil redisUtil;


	@Autowired
	private SaasUserRoleRalationService saasUserRoleRalationService;

	@Autowired
	private SaasRoleService saasRoleService;

	@Autowired
	private BaseMerchantService merchantService;


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
    public AjaxResponse unauthorized(HttpServletRequest request , HttpServletResponse response) throws Exception{
		AjaxResponse ajaxResponse = AjaxResponse.fail(RestErrorCode.HTTP_FORBIDDEN);
		this.outJson(response, ajaxResponse);
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
		}
		return null;
    }


	/**响应JSON数据**/
	private void outJson( HttpServletResponse response , AjaxResponse ajaxResponse ) {
		PrintWriter out = null;
		try{
			response.setStatus(HttpStatus.SC_OK);
			response.setHeader("pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			/**通知浏览器服务器发送的数据格式是text/html，并要求浏览器使用utf-8进行解码。*/
			response.setHeader("contentType", "text/html;charset=utf-8");
			response.setDateHeader("Expires", 0);
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			out.write( JSON.toJSONString(ajaxResponse, true));
			out.close();
		} catch (Exception e) {
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}




	/**通过用户名、密码，获取短信验证码*/
	@RequestMapping(value = "/getMsgCode" )
	@ResponseBody
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse getMsgCode( @Verify(param="username",rule="required") String username,
									@Verify(param="password",rule="required") String password ){
		/**A: 频率检查 */
		String redisGetmsgcodeKey = "star_fire_getmsgcode_key_"+username;
		long score = System.currentTimeMillis();
		/**zset内部是按分数来排序的，这里用当前时间做分数*/
		redisTemplate.opsForZSet().add(redisGetmsgcodeKey, String.valueOf(score), score);
		/**统计30分钟内获取验证码次数*/
		int statistics = 30;
		redisTemplate.expire(redisGetmsgcodeKey, statistics, TimeUnit.MINUTES);

		/**统计用户30分钟内获取验证码次数*/
		long max = score;
		long min = max - (statistics * 60 * 1000);
		long count = redisTemplate.opsForZSet().count(redisGetmsgcodeKey, min, max);
		logger.info("获取验证码-用户"+username+"在"+statistics+"分钟内第"+count+"次进行获取验证码操作");
		String flag = redisTemplate.opsForValue().get(CACHE_PREFIX_MSGCODE_CONTROL+username) ==null? null: redisTemplate.opsForValue().get(CACHE_PREFIX_MSGCODE_CONTROL+username).toString();

		if(flag!=null ) {
			return AjaxResponse.fail(RestErrorCode.GET_MSGCODE_EXCEED);
		}
		/**B:查询用户信息*/
 		CarAdmUser user = carAdmUserExMapper.queryByAccount(username,null);
		if(user==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		/**C:密码不正确*/
		String encPwd = PasswordUtil.md5(password, user.getAccount());
		if(!encPwd.equalsIgnoreCase(user.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.USER_PASSWORD_WRONG) ;
		}
		/**D: 查询验证码，或新生成验证码，而后发送验证码短信*/
		String msgcode = redisUtil.get(CACHE_PREFIX_MSGCODE+username);
		if(msgcode==null) {
			msgcode = NumberUtil.genRandomCode(6);
		}
		String mobile   = user.getPhone();
		String content  = "登录验证码为："+msgcode+"，请在"+msgcodeTimeoutMinutes+"分钟内进行登录。";
		SmsSendUtil.send(mobile, content);
		/**E: 写入缓存*/
		redisUtil.set(CACHE_PREFIX_MSGCODE+username, msgcode,  msgcodeTimeoutMinutes * 60 );
		Map<String,Object> result = new HashMap<String,Object>(4);
		/**验证码有效的秒数*/
		result.put("timeout",  60 );
		result.put("tipText", "短信验证码已成功发送至尾号为"+mobile.substring(7)+"的手机上。" );
		return AjaxResponse.success( result );
	}

	/**执行登录*/
	@RequestMapping(value = "/dologin" )
	@ResponseBody
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse dologin(HttpServletRequest request , HttpServletResponse response,
								@Verify(param="username",rule="required") String username,
								@Verify(param="password",rule="required") String password,
								@Verify(param="msgcode",rule="required") String msgcode,
								HttpSession session) throws IOException{

		String redisLoginKey = "star_fire_login_key_"+username;
		String redisGetmsgcodeKey = "star_fire_getmsgcode_key_"+username;

		Subject currentLoginUser = SecurityUtils.getSubject();
		/**A:是否已经登录*/
		if(currentLoginUser.isAuthenticated()) {
			Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
			if(  isAjax  ) {
				return AjaxResponse.success( null );
			}else {
				response.sendRedirect(homepageUrl);
				return null;
			}
		}
		/**B:查询用户信息*/
		CarAdmUser user = carAdmUserExMapper.queryByAccount(username,null);
		if(user==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		/**C:密码不正确*/
		String encPwd = PasswordUtil.md5(password, user.getAccount());
		if(!encPwd.equalsIgnoreCase(user.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.USER_PASSWORD_WRONG) ;
		}
		/**D: 查询验证码，并判断是否正确*/
		if(com.sq.transportmanage.gateway.api.common.Constants.ISTRUE.equalsIgnoreCase(loginCheckMsgCodeSwitch)) {
			long score = System.currentTimeMillis();
			/**zset内部是按分数来排序的，这里用当前时间做分数*/
			redisTemplate.opsForZSet().add(redisLoginKey, String.valueOf(score), score);
			/**统计30分钟内用户登录次数*/
			int statistics = 30;
			redisTemplate.expire(redisLoginKey, statistics, TimeUnit.MINUTES);

			/**统计用户30分钟内登录的次数*/
			long max = score;
			long min = max - (statistics * 60 * 1000);
			long count = redisTemplate.opsForZSet().count(redisLoginKey, min, max);
			logger.info("登录-用户"+username+"在"+statistics+"分钟内第"+count+"次登录");
			/**验证验证码是否正确*/
			String  msgcodeInCache = redisUtil.get(CACHE_PREFIX_MSGCODE+username);

			if(msgcodeInCache==null) {
				return AjaxResponse.fail(RestErrorCode.MSG_CODE_INVALID) ;
			}
			if(!msgcodeInCache.equals(msgcode)) {
				return AjaxResponse.fail(RestErrorCode.MSG_CODE_WRONG) ;
			}

		}
		/**E: 用户状态*/
		if(user.getStatus()!=null && user.getStatus().intValue()==Constants.USER_STATUS ){
			return AjaxResponse.fail(RestErrorCode.USER_INVALID) ;
		}
		//TODO 添加超级管理员 如果是超级管理员 可以先选择用户赋值给那个商户
		if(AuthEnum.SUPER_MANAGE.getAuthId().equals(user.getAccountType())){
			logger.info("超级管理员登录" + JSONObject.toJSONString(user));
			UsernamePasswordToken token = new UsernamePasswordToken( username, password.toCharArray() );
			currentLoginUser.login(token);
			return AjaxResponse.success( "superManager" );
		}
		try {
			/**shiro登录*/
			UsernamePasswordToken token = new UsernamePasswordToken( username, password.toCharArray() );
			currentLoginUser.login(token);
			/**记录登录用户的所有会话ID，以支持“系统管理”功能中的自动会话清理*/
			String sessionId =  (String)currentLoginUser.getSession().getId() ;
			redisSessionDAO.saveSessionIdOfLoginUser(username, sessionId);

			redisTemplate.delete(redisLoginKey);
			redisTemplate.delete(redisGetmsgcodeKey);

		}catch(AuthenticationException aex) {
			aex.getStackTrace();
			logger.info(aex.getMessage());
			return AjaxResponse.fail(RestErrorCode.USER_LOGIN_FAILED) ;
		}
		/**返回登录成功*/
		Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
		if(  isAjax  ) {
			return AjaxResponse.success( null );
		}else {
			response.sendRedirect(homepageUrl);
			return null;
		}
	}

	/**如果是超级管理员，获取其下有哪些商户 */
	@RequestMapping("/getAllMerchants")
	@ResponseBody
	public AjaxResponse getAllMerchants( HttpServletRequest request ,
										 HttpServletResponse response,
										 String merchantName,
										 Integer status) throws Exception{
		SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
		if( ssoLoginUser != null && AuthEnum.SUPER_MANAGE.getAuthId().equals(ssoLoginUser.getAccountType())){
 			/**获取该商户下的id和名称返回给h5*/
			List<Merchant> merchantList = merchantService.queryMerchantNames(ssoLoginUser.getMerchantArea(),merchantName,status);
			JSONArray jsonArray = new JSONArray();
			if(!CollectionUtils.isEmpty(merchantList)){
				merchantList.forEach(list ->{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("merchantId",list.getMerchantId());
					jsonObject.put("merchantName",list.getMerchantName());
					jsonObject.put("status",list.getStatus());
					jsonArray.add(jsonObject);
				});
			}
			return  AjaxResponse.success(jsonArray);
		}else {
			logger.info("用户未登录");
			return  AjaxResponse.fail(RestErrorCode.USER_LOGIN_FAILED);
		}
	}


	/**选择某个商户后id点击进入 */
	@RequestMapping("/loginByMerchantId")
	@ResponseBody
	public AjaxResponse loginByMerchantId(HttpServletRequest request ,
										  HttpServletResponse response,
										  @Verify(param = "merchantId",rule = "required") Integer merchantId) throws Exception{


		SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
		if(ssoLoginUser != null && AuthEnum.SUPER_MANAGE.getAuthId().equals(ssoLoginUser.getAccountType())){
			ssoLoginUser.setMerchantId(merchantId);
			carAdmUserExMapper.updateMerchantId(merchantId,ssoLoginUser.getLoginName());

			String redisLoginKey = "star_fire_login_key_"+ssoLoginUser.getLoginName();
			String redisGetmsgcodeKey = "star_fire_getmsgcode_key_"+ssoLoginUser.getLoginName();
			Subject currentLoginUser = SecurityUtils.getSubject();
			try {
				/**shiro登录*/
				UsernamePasswordToken token = new UsernamePasswordToken( ssoLoginUser.getLoginName(), ssoLoginUser.getLoginName().toCharArray() );
				currentLoginUser.login(token);
				/**记录登录用户的所有会话ID，以支持“系统管理”功能中的自动会话清理*/
				String sessionId =  (String)currentLoginUser.getSession().getId() ;
				redisSessionDAO.saveSessionIdOfLoginUser(ssoLoginUser.getLoginName(), sessionId);

				redisTemplate.delete(redisLoginKey);
				redisTemplate.delete(redisGetmsgcodeKey);

			}catch(AuthenticationException aex) {
				aex.getStackTrace();
				logger.info(aex.getMessage());
				return AjaxResponse.fail(RestErrorCode.USER_LOGIN_FAILED) ;
			}
			Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
			if(  isAjax  ) {
				return AjaxResponse.success( null );
			}else {
				response.sendRedirect(homepageUrl);
				return null;
			}
		}else {
			logger.info("用户未登录");
			return  null;
		}
 	}


	/**执行登出 */
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

	/**修改密码*/
	@RequestMapping("/changePassword")
	@ResponseBody
	public AjaxResponse changePassword( @Verify(param="oldPassword",rule="required") String oldPassword, @Verify(param="newPassword",rule="required") String newPassword ){
		SSOLoginUser ssoLoginUser  =  WebSessionUtil.getCurrentLoginUser();
		CarAdmUser   carAdmUser    =  carAdmUserMapper.selectByPrimaryKey( ssoLoginUser.getId()  );
		/**A:用户不存在*/
		if(carAdmUser==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		/**B:密码不正确*/
		String encPwd = PasswordUtil.md5(oldPassword, carAdmUser.getAccount());
		if(!encPwd.equalsIgnoreCase(carAdmUser.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.OLD_PASSWORD_WRONG) ;
		}
		/**C:执行*/
		String newEncPwd = PasswordUtil.md5(newPassword, carAdmUser.getAccount());
		CarAdmUser   carAdmUserForUpdate = new  CarAdmUser();
		carAdmUserForUpdate.setUserId(carAdmUser.getUserId());
		carAdmUserForUpdate.setPassword(newEncPwd);
		carAdmUserMapper.updateByPrimaryKeySelective(carAdmUserForUpdate);
		redisSessionDAO.clearRelativeSession(null,null,carAdmUser.getUserId());
		return AjaxResponse.success( null );
	}


	@RequestMapping("/currentLoginUserInfo")
	@ResponseBody
	@SuppressWarnings("unchecked")
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse currentLoginUserInfo( String menuDataFormat,HttpServletResponse response ){
		try {
			SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
			if(ssoLoginUser == null){
				try {
					response.sendRedirect(homepageUrl);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			AjaxLoginUserDTO ajaxLoginUserDTO = new AjaxLoginUserDTO();
			/**一、用户基本信息*/
			ajaxLoginUserDTO.setId(ssoLoginUser.getId());
			ajaxLoginUserDTO.setLoginName(ssoLoginUser.getLoginName());
			ajaxLoginUserDTO.setMobile(ssoLoginUser.getMobile());
			ajaxLoginUserDTO.setName(ssoLoginUser.getName());
			ajaxLoginUserDTO.setEmail(ssoLoginUser.getEmail());
			ajaxLoginUserDTO.setStatus(ssoLoginUser.getStatus());
			ajaxLoginUserDTO.setMerchantId(ssoLoginUser.getMerchantId());
			ajaxLoginUserDTO.setDataLevel(ssoLoginUser.getDataLevel());
			/**二、用户的菜单信息      (  具有Session缓存机制 ，以提升性能   )*/
			if( !SaasConst.PermissionDataFormat.TREE.equalsIgnoreCase(menuDataFormat) && !SaasConst.PermissionDataFormat.LIST.equalsIgnoreCase(menuDataFormat) ) {
                /**默认为树形*/
				menuDataFormat = SaasConst.PermissionDataFormat.TREE;
            }
			List<SaasPermissionDTO> menuPerms = (List<SaasPermissionDTO>)WebSessionUtil.getAttribute("xxx_menu_"+menuDataFormat);
			if( menuPerms ==null || Constants.SUPER_MANAGE.equals(ssoLoginUser.getAccountType())) {
				List<Byte> permissionTypes =  Arrays.asList( new Byte[] { SaasConst.PermissionType.MENU,SaasConst.PermissionType.BUTTON,SaasConst.PermissionType.DATA_AREA });
				menuPerms = this.getAllPermissions( ssoLoginUser.getId()  , permissionTypes, menuDataFormat);
                if(menuPerms!=null && menuPerms.size()>0) {
                    WebSessionUtil.setAttribute("xxx_menu_"+menuDataFormat, menuPerms);
                }
            }
			ajaxLoginUserDTO.setPermissionDTOS( menuPerms );

			/**三、用户的权限信息 ( 参照shiro原码中的逻辑 )*/
			Subject subject = SecurityUtils.getSubject();
			subject.isPermitted("XXXX-XXXXXXXXX-XXXXXXXX-123456");
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
			/**四、用户的数据权限*/
			ajaxLoginUserDTO.setSupplierIds(ssoLoginUser.getSupplierIds());

			/**五、配置信息*/
			Map<String, Object > configs = new HashMap<String,Object>(4);
			/**手机号码正则式*/
			configs.put("mobileRegex",  SaasConst.MOBILE_REGEX);
			/**账号的正则表达式*/
			configs.put("accountRegex", SaasConst.ACCOUNT_REGEX);
			/**电子邮箱的正则表达式*/
			configs.put("emailRegex",    SaasConst.EMAIL_REGEX);
			ajaxLoginUserDTO.setConfigs(configs);
			return AjaxResponse.success( ajaxLoginUserDTO );
		} catch (CacheException e) {
			logger.error("登录异常" + e);
			return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
		}
	}

	/**五、查询一个用户的菜单（返回的数据格式：列表、树形）*/
	private List<SaasPermissionDTO> getAllPermissions( Integer userId,  List<Byte> permissionTypes,  String dataFormat ){
		/**查询用户所拥有的所有有效的权限ID*/
		List<Integer> validPermissionIdsOfCurrentLoginUser =  saasPermissionExMapper.queryPermissionIdsOfUser( userId );
		if(validPermissionIdsOfCurrentLoginUser==null || validPermissionIdsOfCurrentLoginUser.size()==0) {
			return null;
		}
		if(  SaasConst.PermissionDataFormat.LIST.equalsIgnoreCase(dataFormat) ) {
			return this.getAllPermissionsList( validPermissionIdsOfCurrentLoginUser, permissionTypes );
		}else if( SaasConst.PermissionDataFormat.TREE.equalsIgnoreCase(dataFormat) ) {
			return this.getAllPermissionsTree( validPermissionIdsOfCurrentLoginUser, permissionTypes );
		}
		return null;
	}
	/**返回的数据格式：列表*/
	private List<SaasPermissionDTO> getAllPermissionsList( List<Integer> permissionIds,  List<Byte> permissionTypes ){
		List<SaasPermission> allPos = saasPermissionExMapper.queryPermissions(permissionIds, null, null, permissionTypes, null, null);
		List<SaasPermissionDTO> allDtos = BeanUtil.copyList(allPos, SaasPermissionDTO.class);
		return allDtos;
	}
	/**返回的数据格式：树形*/
	private List<SaasPermissionDTO> getAllPermissionsTree( List<Integer> permissionIds,  List<Byte> permissionTypes ){
		return this.getChildren(permissionIds,  0 , permissionTypes);
	}
	private List<SaasPermissionDTO> getChildren( List<Integer> permissionIds,  Integer parentPermissionId,  List<Byte> permissionTypes ){
		List<SaasPermission> childrenPos = saasPermissionExMapper.queryPermissions(permissionIds, parentPermissionId, null, permissionTypes, null, null);
		if(childrenPos==null || childrenPos.size()==0) {
			return null;
		}
		List<SaasPermissionDTO> childrenDtos = BeanUtil.copyList(childrenPos, SaasPermissionDTO.class);

 		Iterator<SaasPermissionDTO> iterator = childrenDtos.iterator();
		while (iterator.hasNext()) {
			SaasPermissionDTO childrenDto = iterator.next();
			if ("Capacity".equals(childrenDto.getPermissionCode()) && !authCapacity()) {
				iterator.remove();
				continue;
			}
			List<SaasPermissionDTO> childs = this.getChildren( permissionIds, childrenDto.getPermissionId() ,  permissionTypes );
			childrenDto.setChildPermissions(childs);
		}
		return childrenDtos;
	}


	public boolean authCapacity(){
 		Set<Integer> userCityIds = null;
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
		return null;
	}


    /**
     * 查询当前用户所拥有的模块
	 * @return
     */
	@RequestMapping("/queryModularPermissions")
	@ResponseBody
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse queryModularPermissions(){
		SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
		if(ssoLoginUser != null){
			Map<Integer,String> map = ssoLoginUser.getMenuPermissionMap();
			JSONArray jsonArray = new JSONArray();
			for(Integer key : map.keySet()){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("permissionId",key);
				jsonObject.put("permissionName",map.get(key));
				jsonArray.add(jsonObject);
			}
			return AjaxResponse.success(jsonArray);
		}
		return AjaxResponse.fail(RestErrorCode.EMAIL_EXIST);
	}



	/**
	 * 根据模块查询下面所有的菜单
	 * @return
	 */
	@RequestMapping("/queryPermissionsByModularId")
	@ResponseBody
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse queryPermissionsByModularId(@Verify(param = "modularId",rule = "required") Integer modularId){
		SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
		if(ssoLoginUser != null){
			Map<Integer,List<SaasPermissionDTO>> map = ssoLoginUser.getMapPermission();
			JSONArray jsonArray = new JSONArray();
			for(Integer key : map.keySet()){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("permissionId",modularId);
				jsonObject.put("permissionDTOS",map.get(modularId));
				jsonArray.add(jsonObject);
			}
			return AjaxResponse.success(jsonArray);
		}
		return AjaxResponse.fail(RestErrorCode.EMAIL_EXIST);
	}







	/**九、根据用户id查询用户有哪些角色**/
	@RequestMapping("/queryRolesByUserId")
	@RequestFunction(menu = USER_RESET_PASSWORD)
	@ResponseBody
	public AjaxResponse queryRolesByUserId( @Verify(param="userId",rule="required|min(1)") Integer userId ) {
		List<Integer> listRoleIds = saasUserRoleRalationService.queryRoleIdsOfUser(userId);
		if(!CollectionUtils.isEmpty(listRoleIds)){
			List<SaasRole> saasRoleList = saasRoleService.queryRoles(WebSessionUtil.getCurrentLoginUser().getMerchantId(),
					listRoleIds,null,null,null);
			return AjaxResponse.success(saasRoleList);
		}
		return AjaxResponse.success(null);
	}



 	@RequestMapping("/currentLoginUserInfoListMenu")
	@ResponseBody
	@SuppressWarnings("unchecked")
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse currentLoginUserInfoListMenu( String menuDataFormat,HttpServletResponse response ){
		try {
			SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
			if(ssoLoginUser == null){
				try {
					response.sendRedirect(homepageUrl);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			AjaxLoginUserDTO ajaxLoginUserDTO = new AjaxLoginUserDTO();
			/**一、用户基本信息*/
			ajaxLoginUserDTO.setId(ssoLoginUser.getId());
			ajaxLoginUserDTO.setLoginName(ssoLoginUser.getLoginName());
			ajaxLoginUserDTO.setMobile(ssoLoginUser.getMobile());
			ajaxLoginUserDTO.setName(ssoLoginUser.getName());
			ajaxLoginUserDTO.setEmail(ssoLoginUser.getEmail());
			ajaxLoginUserDTO.setStatus(ssoLoginUser.getStatus());
			/**二、用户的菜单信息      (  具有Session缓存机制 ，以提升性能   )*/
			if( !SaasConst.PermissionDataFormat.TREE.equalsIgnoreCase(menuDataFormat) && !SaasConst.PermissionDataFormat.LIST.equalsIgnoreCase(menuDataFormat) ) {
				menuDataFormat = SaasConst.PermissionDataFormat.LIST;
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

			/**三、用户的权限信息 ( 参照shiro原码中的逻辑 )*/
			Subject subject = SecurityUtils.getSubject();
			/**这里随意调用一下，确保shiro授权缓存已经被加载！！！*/
			subject.isPermitted("XXXX-XXXXXXXXX-XXXXXXXX-123456");
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
			/**四、用户的数据权限*/
			ajaxLoginUserDTO.setSupplierIds(ssoLoginUser.getSupplierIds());

			/**五、配置信息*/
			Map<String, Object > configs = new HashMap<String,Object>(4);
			configs.put("mobileRegex",  SaasConst.MOBILE_REGEX);
			configs.put("accountRegex", SaasConst.ACCOUNT_REGEX);
			configs.put("emailRegex",    SaasConst.EMAIL_REGEX);
			ajaxLoginUserDTO.setConfigs(configs);
			return AjaxResponse.success( ajaxLoginUserDTO );
		} catch (CacheException e) {
			logger.error("登录异常" + e);
			return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
		}
	}



}