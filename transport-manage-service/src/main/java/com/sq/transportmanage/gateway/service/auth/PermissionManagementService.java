package com.sq.transportmanage.gateway.service.auth;


import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.SaasPermissionMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasPermissionExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasRolePermissionRalationExMapper;
import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.util.BeanUtil;
import org.apache.tools.ant.filters.StringInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author fanht
 * 权限管理功能
 */
@Service
public class PermissionManagementService {
	@Autowired
	private SaasPermissionMapper saasPermissionMapper;
	@Autowired
	private SaasPermissionExMapper saasPermissionExMapper;
	@Autowired
	private SaasRolePermissionRalationExMapper saasRolePermissionRalationExMapper;
	@Resource(name = "sessionDAO")
	private RedisSessionDAO redisSessionDAO;

	/**
	 * 一、增加一个权限
	 **/
	public AjaxResponse addSaasPermission(SaasPermission pemission) {
		/**父权限不存在*/
		if (pemission.getParentPermissionId() != null && pemission.getParentPermissionId().intValue() > 0) {
			SaasPermission parentPermission = saasPermissionMapper.selectByPrimaryKey(pemission.getParentPermissionId());
			if (parentPermission == null) {
				return AjaxResponse.fail(RestErrorCode.PARENT_PERMISSION_NOT_EXIST);
			}
		}
		/***权限代码已经存在*/
		List<SaasPermission> pos = saasPermissionExMapper.queryPermissions(null, null, pemission.getPermissionCode(), null, null, null);
		if (pos != null && pos.size() > 0) {
			return AjaxResponse.fail(RestErrorCode.PERMISSION_CODE_EXIST);
		}
		/**权限类型不合法*/
		List<Byte> permTypes = Arrays.asList(new Byte[]{SaasConst.PermissionType.MENU, SaasConst.PermissionType.BUTTON, SaasConst.PermissionType.DATA_AREA});
		if (!permTypes.contains(pemission.getPermissionType())) {
			return AjaxResponse.fail(RestErrorCode.PERMISSION_TYPE_WRONG);
		}
		/**自动生成排序的序号*/
		int sortSeq = saasPermissionExMapper.selectMaxSortSeq(pemission.getParentPermissionId()).intValue() + 1;
		pemission.setValid(true);
		pemission.setSortSeq(sortSeq);
		saasPermissionMapper.insertSelective(pemission);
		return AjaxResponse.success(null);
	}

	/**
	 * 二、禁用一个权限
	 **/
	public AjaxResponse disableSaasPermission(Integer permissionId) {
		/**权限不存在*/
		SaasPermission thisPermission = saasPermissionMapper.selectByPrimaryKey(permissionId);
		if (thisPermission == null) {
			return AjaxResponse.fail(RestErrorCode.PERMISSION_NOT_EXIST);
		}
		/**系统预置权限，不能禁用、修改*/
		if (SaasConst.SYSTEM_PERMISSIONS.contains(thisPermission.getPermissionCode())) {
			return AjaxResponse.fail(RestErrorCode.SYSTEM_PERMISSION_CANOT_CHANGE, thisPermission.getPermissionCode());
		}
		/**存在已经生效的子权限，请先禁用子权限*/
		List<SaasPermission> validChildren = saasPermissionExMapper.queryPermissions(null, permissionId, null, null, null, (byte) 1);
		if (validChildren != null && validChildren.size() > 0) {
			return AjaxResponse.fail(RestErrorCode.PERMISSION_DISABLE_CANT);
		}
		/**执行禁用此权限*/
		SaasPermission pemissionForUpdate = new SaasPermission();
		pemissionForUpdate.setPermissionId(permissionId);
		pemissionForUpdate.setValid(false);
		saasPermissionMapper.updateByPrimaryKeySelective(pemissionForUpdate);
		/**自动清理用户会话*/
		redisSessionDAO.clearRelativeSession(permissionId, null, null);
		return AjaxResponse.success(null);
	}

	/**
	 * 三、启用一个权限
	 **/
	public AjaxResponse enableSaasPermission(Integer permissionId) {
		/**权限不存在*/
		SaasPermission thisPermission = saasPermissionMapper.selectByPrimaryKey(permissionId);
		if (thisPermission == null) {
			return AjaxResponse.fail(RestErrorCode.PERMISSION_NOT_EXIST);
		}
		/**查询其父权限，判断父权限状态*/
		/**父权限已经被禁用，请先启用父权限*/
		if (thisPermission.getParentPermissionId() != null && thisPermission.getParentPermissionId().intValue() > 0) {
			SaasPermission parentPermission = saasPermissionMapper.selectByPrimaryKey(thisPermission.getParentPermissionId());
			if (parentPermission != null && parentPermission.getValid() == false) {
				return AjaxResponse.fail(RestErrorCode.PERMISSION_ENABLE_CANT);
			}
		}
		/**执行启用此权限**/
		SaasPermission pemissionForUpdate = new SaasPermission();
		pemissionForUpdate.setPermissionId(permissionId);
		pemissionForUpdate.setValid(true);
		saasPermissionMapper.updateByPrimaryKeySelective(pemissionForUpdate);
		/**自动清理用户会话*/
		redisSessionDAO.clearRelativeSession(permissionId, null, null);
		return AjaxResponse.success(null);
	}

	/**
	 * 四、修改一个权限
	 **/
	public AjaxResponse changeSaasPermission(SaasPermission pemission) {
		/**权限不存在*/
		SaasPermission thisPermission = saasPermissionMapper.selectByPrimaryKey(pemission.getPermissionId());
		if (thisPermission == null) {
			return AjaxResponse.fail(RestErrorCode.PERMISSION_NOT_EXIST);
		}
		/**系统预置权限，不能禁用、修改*/
		if (SaasConst.SYSTEM_PERMISSIONS.contains(thisPermission.getPermissionCode())) {
			return AjaxResponse.fail(RestErrorCode.SYSTEM_PERMISSION_CANOT_CHANGE, thisPermission.getPermissionCode());
		}
		/**权限代码已经存在   (如果发生变化时 )*/
		if (pemission.getPermissionCode() != null && pemission.getPermissionCode().length() > 0 && !pemission.getPermissionCode().equalsIgnoreCase(thisPermission.getPermissionCode())) {
			List<SaasPermission> pos = saasPermissionExMapper.queryPermissions(null, null, pemission.getPermissionCode(), null, null, null);
			if (pos != null && pos.size() > 0) {
				return AjaxResponse.fail(RestErrorCode.PERMISSION_CODE_EXIST);
			}
		}
		/**权限类型不合法*/
		if (pemission.getPermissionType() != null) {
			List<Byte> permTypes = Arrays.asList(new Byte[]{SaasConst.PermissionType.MENU, SaasConst.PermissionType.BUTTON, SaasConst.PermissionType.DATA_AREA});
			if (!permTypes.contains(pemission.getPermissionType())) {
				return AjaxResponse.fail(RestErrorCode.PERMISSION_TYPE_WRONG);
			}
		}
		pemission.setParentPermissionId(null);
		saasPermissionMapper.updateByPrimaryKeySelective(pemission);
		/**自动清理用户会话*/
		redisSessionDAO.clearRelativeSession(pemission.getPermissionId(), null, null);
		return AjaxResponse.success(null);
	}


	/**
	 * 五、查询所有的权限信息（返回的数据格式：列表、树形）
	 **/
	public List<SaasPermissionDTO> getAllPermissions(String dataFormat) {
		if (SaasConst.PermissionDataFormat.LIST.equalsIgnoreCase(dataFormat)) {
			return this.getAllPermissionsList();
		} else if (SaasConst.PermissionDataFormat.TREE.equalsIgnoreCase(dataFormat)) {
			return this.getAllPermissionsTree();
		}
		return null;
	}

	/**
	 * 返回的数据格式：列表
	 **/
	private List<SaasPermissionDTO> getAllPermissionsList() {
		//查询当前商户最小的角色所有的权限
		List<Integer> minRolePermissionList = saasRolePermissionRalationExMapper.queryPermissionByMerchantId(WebSessionUtil.getCurrentLoginUser().getMerchantId());
		List<SaasPermission> allPos = saasPermissionExMapper.queryPermissions(minRolePermissionList, null, null, null, null, SaasConst.IsValid.VALID_TRUE);
		List<SaasPermissionDTO> saasPerList = BeanUtil.copyList(allPos, SaasPermissionDTO.class);
		saasPerList.stream().forEach(p -> {
			/**设置是否为系统预置权限*/
			if (SaasConst.SYSTEM_PERMISSIONS.contains(p.getPermissionCode())) {
				p.setPresetPerm(true);
			}
		});
		return saasPerList;
	}

	/**
	 * 返回的数据格式：树形
	 **/
	private List<SaasPermissionDTO> getAllPermissionsTree() {
		/**查询商户最小的角色时候 查询一次即可*/
		List<Integer> minRolePermissionList = saasRolePermissionRalationExMapper.queryPermissionByMerchantId(WebSessionUtil.getCurrentLoginUser().getMerchantId());
		return this.getChildren(0,minRolePermissionList);
	}

	private List<SaasPermissionDTO> getChildren(Integer parentPermissionId,List<Integer> minRolePermissionList) {

		List<SaasPermission> childrenPos = saasPermissionExMapper.queryPermissions(minRolePermissionList, parentPermissionId, null, null, null, SaasConst.IsValid.VALID_TRUE);
		if (childrenPos == null || childrenPos.size() == 0) {
			return null;
		}
		/**递归*/
		List<SaasPermissionDTO> childrenDtos = BeanUtil.copyList(childrenPos, SaasPermissionDTO.class);
		childrenDtos.stream().forEach(p -> {
			/**设置是否为系统预置权限*/
			if (SaasConst.SYSTEM_PERMISSIONS.contains(p.getPermissionCode())) {
				p.setPresetPerm(true);
			}
		});
		for (SaasPermissionDTO childrenDto : childrenDtos) {
			List<SaasPermissionDTO> childs = this.getChildren(childrenDto.getPermissionId(),minRolePermissionList);
			childrenDto.setChildPermissions(childs);
		}
		return childrenDtos;
	}

	/**
	 * 六、删除一个权限
	 **/
	public AjaxResponse deleteSaasPermission(Integer permissionId) {
		/**权限不存在*/
		SaasPermission thisPermission = saasPermissionMapper.selectByPrimaryKey(permissionId);
		if (thisPermission == null) {
			return AjaxResponse.fail(RestErrorCode.PERMISSION_NOT_EXIST);
		}
		/**系统预置权限，不能禁用、修改*/
		if (SaasConst.SYSTEM_PERMISSIONS.contains(thisPermission.getPermissionCode())) {
			return AjaxResponse.fail(RestErrorCode.SYSTEM_PERMISSION_CANOT_CHANGE, thisPermission.getPermissionCode());
		}
		/**自动清理用户会话*/
		redisSessionDAO.clearRelativeSession(permissionId, null, null);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		/**目的是等待一会儿，因会话清理也要查表的*/
		saasRolePermissionRalationExMapper.deleteRoleIdsOfPermission(permissionId);
		saasPermissionMapper.deleteByPrimaryKey(permissionId);
		return AjaxResponse.success(null);
	}


	/**
	 * 七、修改一个权限 包含父菜单
	 **/
	public AjaxResponse changeSaasPermissionContainParent(SaasPermission pemission) {
		/**权限不存在*/
		SaasPermission thisPermission = saasPermissionMapper.selectByPrimaryKey(pemission.getPermissionId());
		if (thisPermission == null) {
			return AjaxResponse.fail(RestErrorCode.PERMISSION_NOT_EXIST);
		}
		/**系统预置权限，不能禁用、修改*/
		if (SaasConst.SYSTEM_PERMISSIONS.contains(thisPermission.getPermissionCode())) {
			return AjaxResponse.fail(RestErrorCode.SYSTEM_PERMISSION_CANOT_CHANGE, thisPermission.getPermissionCode());
		}
		/**权限代码已经存在   (如果发生变化时 )*/
		if (pemission.getPermissionCode() != null && pemission.getPermissionCode().length() > 0 && !pemission.getPermissionCode().equalsIgnoreCase(thisPermission.getPermissionCode())) {
			List<SaasPermission> pos = saasPermissionExMapper.queryPermissions(null, null, pemission.getPermissionCode(), null, null, null);
			if (pos != null && pos.size() > 0) {
				return AjaxResponse.fail(RestErrorCode.PERMISSION_CODE_EXIST);
			}
		}
		/**权限类型不合法*/
		if (pemission.getPermissionType() != null) {
			List<Byte> permTypes = Arrays.asList(new Byte[]{SaasConst.PermissionType.MENU, SaasConst.PermissionType.BUTTON, SaasConst.PermissionType.DATA_AREA});
			if (!permTypes.contains(pemission.getPermissionType())) {
				return AjaxResponse.fail(RestErrorCode.PERMISSION_TYPE_WRONG);
			}
		}
		saasPermissionMapper.updateByPrimaryKeySelective(pemission);
		/**自动清理用户会话*/
		redisSessionDAO.clearRelativeSession(pemission.getPermissionId(), null, null);
		return AjaxResponse.success(null);
	}

	/**
	 * 查询一个角色的权限，用父节点-子节点-子节点 展示给端上
	 * @param roleId
	 * @return
	 */
	public List<String> queryPermissionsOfRoleId(Integer roleId){
		List<SaasPermission> permissionList = saasPermissionExMapper.queryPermissionsOfRoleId(roleId);
		List<String> strPermissionIds = new ArrayList<>();
		if(!CollectionUtils.isEmpty(permissionList)){
			String varble = "";
			StringJoiner stringJoiner = new StringJoiner(varble);
			Map<Integer,Integer> map = Maps.newHashMap();
			permissionList.forEach(permission ->{
				map.put(permission.getPermissionId(),permission.getParentPermissionId());
			});
			permissionList.forEach(permission->{
				StringBuffer sb = getKey(map,permission.getPermissionId());
				if(sb != null && sb.length() > 0){
					String value = sb.substring(0,sb.length()-1);
					String newStr = replaceStr(value);
					strPermissionIds.add(newStr);
				}
			});
		}
		return strPermissionIds;
	}


	/**
	 * 使用递归获取菜单id 和端上对应
	 * @param map
	 * @param permissionId
	 * @return
	 */
	public StringBuffer getKey(Map<Integer,Integer> map,Integer permissionId){
		StringBuffer sb = new StringBuffer();
		if(permissionId != 0 && permissionId > 0){
			sb.append(permissionId).append("-");
			sb.append(getKey(map,map.get(permissionId)));
		}
		return sb;
	}

	private String replaceStr(String oldStr){
		String[]  str = oldStr.split("-");
		StringBuffer buffer = new StringBuffer();
		for(int i = str.length-1;i>=0;i--){
			buffer.append(str[i]).append("-");
		}
		return  buffer.length() == 0 ? "" : buffer.substring(0,buffer.length()-1);
	}
}