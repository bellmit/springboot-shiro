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
    /**??????UI????????????*/
	@Value(value="${loginpage.url}")
    private String loginpageUrl;
	/**??????UI????????????*/
	@Value("${homepage.url}")
	private String homepageUrl;
	/**??????????????????????????????????????????*/
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


    /**???????????????????????? **/
    @RequestMapping("/nginx")
    public String nginx(HttpServletResponse response ) throws IOException{
    	response.getWriter().append("true");
    	response.getWriter().close();
        return null;
    }
	/**????????????**/
    @RequestMapping("/index")
    public String index(HttpServletRequest request , HttpServletResponse response, Model model) throws Exception {
		logger.info(">>>>>>>>>>>>>>>>>??????????????????log4j?????????logback????????????");
		response.sendRedirect(homepageUrl);
		return null;
    }

    /**?????????????????? **/
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

    /**?????????????????????**/
    @ExceptionHandler(UnauthenticatedException.class)
	@RequestMapping("/unauthorized")
    public AjaxResponse unauthorized(HttpServletRequest request , HttpServletResponse response) throws Exception{
		AjaxResponse ajaxResponse = AjaxResponse.fail(RestErrorCode.HTTP_FORBIDDEN);
		this.outJson(response, ajaxResponse);
		return null;
    }

    /**???????????????404?????????SPRING?????????????????????????????????????????????**/
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


	/**??????JSON??????**/
	private void outJson( HttpServletResponse response , AjaxResponse ajaxResponse ) {
		PrintWriter out = null;
		try{
			response.setStatus(HttpStatus.SC_OK);
			response.setHeader("pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			/**????????????????????????????????????????????????text/html???????????????????????????utf-8???????????????*/
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




	/**????????????????????????????????????????????????*/
	@RequestMapping(value = "/getMsgCode" )
	@ResponseBody
	@MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
	public AjaxResponse getMsgCode( @Verify(param="username",rule="required") String username,
									@Verify(param="password",rule="required") String password ){
		/**A: ???????????? */
		String redisGetmsgcodeKey = "star_fire_getmsgcode_key_"+username;
		long score = System.currentTimeMillis();
		/**zset???????????????????????????????????????????????????????????????*/
		redisTemplate.opsForZSet().add(redisGetmsgcodeKey, String.valueOf(score), score);
		/**??????30??????????????????????????????*/
		int statistics = 30;
		redisTemplate.expire(redisGetmsgcodeKey, statistics, TimeUnit.MINUTES);

		/**????????????30??????????????????????????????*/
		long max = score;
		long min = max - (statistics * 60 * 1000);
		long count = redisTemplate.opsForZSet().count(redisGetmsgcodeKey, min, max);
		logger.info("???????????????-??????"+username+"???"+statistics+"????????????"+count+"??????????????????????????????");
		String flag = redisTemplate.opsForValue().get(CACHE_PREFIX_MSGCODE_CONTROL+username) ==null? null: redisTemplate.opsForValue().get(CACHE_PREFIX_MSGCODE_CONTROL+username).toString();

		if(flag!=null ) {
			return AjaxResponse.fail(RestErrorCode.GET_MSGCODE_EXCEED);
		}
		/**B:??????????????????*/
 		CarAdmUser user = carAdmUserExMapper.queryByAccount(username,null);
		if(user==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		/**C:???????????????*/
		String encPwd = PasswordUtil.md5(password, user.getAccount());
		if(!encPwd.equalsIgnoreCase(user.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.USER_PASSWORD_WRONG) ;
		}
		/**D: ?????????????????????????????????????????????????????????????????????*/
		String msgcode = redisUtil.get(CACHE_PREFIX_MSGCODE+username);
		if(msgcode==null) {
			msgcode = NumberUtil.genRandomCode(6);
		}
		String mobile   = user.getPhone();
		String content  = "?????????????????????"+msgcode+"?????????"+msgcodeTimeoutMinutes+"????????????????????????";
		SmsSendUtil.send(mobile, content);
		/**E: ????????????*/
		redisUtil.set(CACHE_PREFIX_MSGCODE+username, msgcode,  msgcodeTimeoutMinutes * 60 );
		Map<String,Object> result = new HashMap<String,Object>(4);
		/**????????????????????????*/
		result.put("timeout",  60 );
		result.put("tipText", "??????????????????????????????????????????"+mobile.substring(7)+"???????????????" );
		return AjaxResponse.success( result );
	}

	/**????????????*/
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
		/**A:??????????????????*/
		if(currentLoginUser.isAuthenticated()) {
			Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
			if(  isAjax  ) {
				return AjaxResponse.success( null );
			}else {
				response.sendRedirect(homepageUrl);
				return null;
			}
		}
		/**B:??????????????????*/
		CarAdmUser user = carAdmUserExMapper.queryByAccount(username,null);
		if(user==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		/**C:???????????????*/
		String encPwd = PasswordUtil.md5(password, user.getAccount());
		if(!encPwd.equalsIgnoreCase(user.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.USER_PASSWORD_WRONG) ;
		}
		/**D: ???????????????????????????????????????*/
		if(com.sq.transportmanage.gateway.api.common.Constants.ISTRUE.equalsIgnoreCase(loginCheckMsgCodeSwitch)) {
			long score = System.currentTimeMillis();
			/**zset???????????????????????????????????????????????????????????????*/
			redisTemplate.opsForZSet().add(redisLoginKey, String.valueOf(score), score);
			/**??????30???????????????????????????*/
			int statistics = 30;
			redisTemplate.expire(redisLoginKey, statistics, TimeUnit.MINUTES);

			/**????????????30????????????????????????*/
			long max = score;
			long min = max - (statistics * 60 * 1000);
			long count = redisTemplate.opsForZSet().count(redisLoginKey, min, max);
			logger.info("??????-??????"+username+"???"+statistics+"????????????"+count+"?????????");
			/**???????????????????????????*/
			String  msgcodeInCache = redisUtil.get(CACHE_PREFIX_MSGCODE+username);

			if(msgcodeInCache==null) {
				return AjaxResponse.fail(RestErrorCode.MSG_CODE_INVALID) ;
			}
			if(!msgcodeInCache.equals(msgcode)) {
				return AjaxResponse.fail(RestErrorCode.MSG_CODE_WRONG) ;
			}

		}
		/**E: ????????????*/
		if(user.getStatus()!=null && user.getStatus().intValue()==Constants.USER_STATUS ){
			return AjaxResponse.fail(RestErrorCode.USER_INVALID) ;
		}
		//TODO ????????????????????? ???????????????????????? ??????????????????????????????????????????
		if(AuthEnum.SUPER_MANAGE.getAuthId().equals(user.getAccountType())){
			logger.info("?????????????????????" + JSONObject.toJSONString(user));
			UsernamePasswordToken token = new UsernamePasswordToken( username, password.toCharArray() );
			currentLoginUser.login(token);
			return AjaxResponse.success( "superManager" );
		}
		try {
			/**shiro??????*/
			UsernamePasswordToken token = new UsernamePasswordToken( username, password.toCharArray() );
			currentLoginUser.login(token);
			/**?????????????????????????????????ID????????????????????????????????????????????????????????????*/
			String sessionId =  (String)currentLoginUser.getSession().getId() ;
			redisSessionDAO.saveSessionIdOfLoginUser(username, sessionId);

			redisTemplate.delete(redisLoginKey);
			redisTemplate.delete(redisGetmsgcodeKey);

		}catch(AuthenticationException aex) {
			aex.getStackTrace();
			logger.info(aex.getMessage());
			return AjaxResponse.fail(RestErrorCode.USER_LOGIN_FAILED) ;
		}
		/**??????????????????*/
		Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
		if(  isAjax  ) {
			return AjaxResponse.success( null );
		}else {
			response.sendRedirect(homepageUrl);
			return null;
		}
	}

	/**?????????????????????????????????????????????????????? */
	@RequestMapping("/getAllMerchants")
	@ResponseBody
	public AjaxResponse getAllMerchants( HttpServletRequest request ,
										 HttpServletResponse response,
										 String merchantName,
										 Integer status) throws Exception{
		SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
		if( ssoLoginUser != null && AuthEnum.SUPER_MANAGE.getAuthId().equals(ssoLoginUser.getAccountType())){
 			/**?????????????????????id??????????????????h5*/
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
			logger.info("???????????????");
			return  AjaxResponse.fail(RestErrorCode.USER_LOGIN_FAILED);
		}
	}


	/**?????????????????????id???????????? */
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
				/**shiro??????*/
				UsernamePasswordToken token = new UsernamePasswordToken( ssoLoginUser.getLoginName(), ssoLoginUser.getLoginName().toCharArray() );
				currentLoginUser.login(token);
				/**?????????????????????????????????ID????????????????????????????????????????????????????????????*/
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
			logger.info("???????????????");
			return  null;
		}
 	}


	/**???????????? */
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

	/**????????????*/
	@RequestMapping("/changePassword")
	@ResponseBody
	public AjaxResponse changePassword( @Verify(param="oldPassword",rule="required") String oldPassword, @Verify(param="newPassword",rule="required") String newPassword ){
		SSOLoginUser ssoLoginUser  =  WebSessionUtil.getCurrentLoginUser();
		CarAdmUser   carAdmUser    =  carAdmUserMapper.selectByPrimaryKey( ssoLoginUser.getId()  );
		/**A:???????????????*/
		if(carAdmUser==null){
			return AjaxResponse.fail(RestErrorCode.USER_NOT_EXIST) ;
		}
		/**B:???????????????*/
		String encPwd = PasswordUtil.md5(oldPassword, carAdmUser.getAccount());
		if(!encPwd.equalsIgnoreCase(carAdmUser.getPassword())) {
			return AjaxResponse.fail(RestErrorCode.OLD_PASSWORD_WRONG) ;
		}
		/**C:??????*/
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
			/**????????????????????????*/
			ajaxLoginUserDTO.setId(ssoLoginUser.getId());
			ajaxLoginUserDTO.setLoginName(ssoLoginUser.getLoginName());
			ajaxLoginUserDTO.setMobile(ssoLoginUser.getMobile());
			ajaxLoginUserDTO.setName(ssoLoginUser.getName());
			ajaxLoginUserDTO.setEmail(ssoLoginUser.getEmail());
			ajaxLoginUserDTO.setStatus(ssoLoginUser.getStatus());
			ajaxLoginUserDTO.setMerchantId(ssoLoginUser.getMerchantId());
			ajaxLoginUserDTO.setDataLevel(ssoLoginUser.getDataLevel());
			/**???????????????????????????      (  ??????Session???????????? ??????????????????   )*/
			if( !SaasConst.PermissionDataFormat.TREE.equalsIgnoreCase(menuDataFormat) && !SaasConst.PermissionDataFormat.LIST.equalsIgnoreCase(menuDataFormat) ) {
                /**???????????????*/
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

			/**??????????????????????????? ( ??????shiro?????????????????? )*/
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
			/**???????????????????????????*/
			ajaxLoginUserDTO.setSupplierIds(ssoLoginUser.getSupplierIds());

			/**??????????????????*/
			Map<String, Object > configs = new HashMap<String,Object>(4);
			/**?????????????????????*/
			configs.put("mobileRegex",  SaasConst.MOBILE_REGEX);
			/**????????????????????????*/
			configs.put("accountRegex", SaasConst.ACCOUNT_REGEX);
			/**??????????????????????????????*/
			configs.put("emailRegex",    SaasConst.EMAIL_REGEX);
			ajaxLoginUserDTO.setConfigs(configs);
			return AjaxResponse.success( ajaxLoginUserDTO );
		} catch (CacheException e) {
			logger.error("????????????" + e);
			return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
		}
	}

	/**??????????????????????????????????????????????????????????????????????????????*/
	private List<SaasPermissionDTO> getAllPermissions( Integer userId,  List<Byte> permissionTypes,  String dataFormat ){
		/**?????????????????????????????????????????????ID*/
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
	/**??????????????????????????????*/
	private List<SaasPermissionDTO> getAllPermissionsList( List<Integer> permissionIds,  List<Byte> permissionTypes ){
		List<SaasPermission> allPos = saasPermissionExMapper.queryPermissions(permissionIds, null, null, permissionTypes, null, null);
		List<SaasPermissionDTO> allDtos = BeanUtil.copyList(allPos, SaasPermissionDTO.class);
		return allDtos;
	}
	/**??????????????????????????????*/
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
     * ????????????????????????????????????
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
	 * ???????????????????????????????????????
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







	/**??????????????????id???????????????????????????**/
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
			/**????????????????????????*/
			ajaxLoginUserDTO.setId(ssoLoginUser.getId());
			ajaxLoginUserDTO.setLoginName(ssoLoginUser.getLoginName());
			ajaxLoginUserDTO.setMobile(ssoLoginUser.getMobile());
			ajaxLoginUserDTO.setName(ssoLoginUser.getName());
			ajaxLoginUserDTO.setEmail(ssoLoginUser.getEmail());
			ajaxLoginUserDTO.setStatus(ssoLoginUser.getStatus());
			/**???????????????????????????      (  ??????Session???????????? ??????????????????   )*/
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

			/**??????????????????????????? ( ??????shiro?????????????????? )*/
			Subject subject = SecurityUtils.getSubject();
			/**?????????????????????????????????shiro????????????????????????????????????*/
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
			/**???????????????????????????*/
			ajaxLoginUserDTO.setSupplierIds(ssoLoginUser.getSupplierIds());

			/**??????????????????*/
			Map<String, Object > configs = new HashMap<String,Object>(4);
			configs.put("mobileRegex",  SaasConst.MOBILE_REGEX);
			configs.put("accountRegex", SaasConst.ACCOUNT_REGEX);
			configs.put("emailRegex",    SaasConst.EMAIL_REGEX);
			ajaxLoginUserDTO.setConfigs(configs);
			return AjaxResponse.success( ajaxLoginUserDTO );
		} catch (CacheException e) {
			logger.error("????????????" + e);
			return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
		}
	}



}