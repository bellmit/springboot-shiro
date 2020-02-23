package com.sq.transportmanage.gateway.service.common.enums;


/**
 * ClassName: EnumOrder 
 * date: 2017年9月4日 上午11:15:17 
 * 
 * @author zhulingling 
 * @version  
 * @since JDK 1.6 
 */
public enum AuditStatus {

	AUDIT_INIT(0, "待审核"),
	AUDIT_PASS(1, "通过"),
	AUDIT_NOTPASS(2, "未通过");

	public int value;
	private String i18n;

	AuditStatus(int value, String i18n) {
		this.value = value;
		this.i18n = i18n;
	}
	
	public String getI18n() {
		return this.i18n;
	}
	public int getValue() {
		return this.value;
	}
	
	public String getValueStr() {
		return String.valueOf(this.value);
	}
	// 普通方法
	public static String getDis(int value) {
		for (AuditStatus status : AuditStatus.values()) {
			if (status.getValue() == value) {
				return status.getI18n();
			}
		}
		return null;
	}
}
  