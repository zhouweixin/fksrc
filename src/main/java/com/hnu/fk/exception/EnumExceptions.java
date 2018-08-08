package com.hnu.fk.exception;

/**
 * 所有异常类型
 * 
 * @author zhouweixin
 *
 */
public enum EnumExceptions {
	UNKOWN_ERROR(-1, "未知错误"),
	SUCCESS(0, "操作成功"),
	REQUEST_METHOD(1, "请求方法不匹配"),
	ARGU_MISMATCH_EXCEPTION(2, "参数类型不匹配错误, 请检查"),
    ADD_FAILED_DUPLICATE(3, "新增失败, 主键重复"),
    UPDATE_FAILED_NOT_EXIST(4, "修改失败, 不存在"),
    DELETE_FAILED_NOT_EXIST(5, "删除失败, 不存在"),
    DELETE_FAILED_REF_KEY_EXISTS(6, "删除失败, 有外键引用"),
    DELETE_FAILED_SYSTEM_VALUE_NOT_ALLOW(7, "删除失败, 系统值禁止删除"),
	ADD_UPDATE_FAILED_PARENT_NOT_EXIST(8,"父部门不存在"),
    ADD_FAILED_DEPARTMENT_NOT_EXISTS(9, "新增失败, 部门不存在"),
    LOGIN_FAILED_USER_NOT_EXISTS(10, "登录失败, 用户不存在"),
    LOGIN_FAILED_USER_PASSWORD_NOT_MATCHER(11, "登录失败, 用户名和密码不匹配"),
    MENU_SHIFT_FAILED_NOT_EXISTS(12, "菜单移动失败, 不存在"),
    RESET_PASSWORD_FAILED_NOT_EXIST(13, "重置密码失败, 不存在"),
    UPDATE_PASSWORD_FAILED_USER_NOT_EIXSTS(14, "修改密码失败, 用户不存在"),
    UPDATE_PASSWORD_FAILED_NEW_PASSWORD_NULL(15, "修改密码失败, 新密码为空"),
    UPDATE_PASSWORD_FAILED_TWICE_PASSWORD_DIFF(16, "修改密码失败, 两次密码不相同"),
    UPDATE_PASSWORD_FAILED_LEN_LESS_SIX(17, "修改密码失败, 密码长度小于6"),
    UPDATE_PASSWORD_FAILED_CHECK_FAILED(18, "修改密码失败, 旧密码错误"),
    CHECK_PASSWORD_FAILED_NOT_EXISTS(19, "密码校验失败, 用户不存在"),
    QUERY_FAILED_DEPARTMENT_NOT_EXISTS(20, "查询失败, 部门不存在"),
    CHECK_FAILED_USER_NOT_EXISTS(21, "权限校验失败, 用户不存在"),

	;

    /** 编码 */
	private Integer code;
	/** 信息 */
	private String message;

	/**
	 * 构造函数
	 * 
	 * @param code
	 * @param message
	 */
	private EnumExceptions(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
