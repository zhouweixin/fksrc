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
	ADD_FAILED_DICID_DUPLICATE(22, "新增失败, 编号重复"),
	ADD_FAILED_DICNAME_DUPLICATE(23, "新增失败, 名称重复"),
	ADD_FAILED_DICCONTENT_DUPLICATE(24, "新增失败, 值重复"),
	ADD_FAILED_DATAPARENT_NOT_EXISTS(25,"新增失败,父编码不存在"),
    UPDATE_FAILED_SON_NOT_PARENT(26,"部门修改失败, 不可把子部门设置为父部门"),
    ASSIGN_FAILED_ROLE_NOT_EXISTS(27,"分配失败, 角色不存在"),
	SEARCH_FAILED_NOT_EXIST(28,"查询失败，不存在"),
    UPDATE_DEPARTMENT_FAILED_USER_NOT_EXISTS(29,"修改部门失败, 用户不存在"),
    UPDATE_DEPARTMENT_FAILED_DEPARTMENT_NOT_EXISTS(30,"修改部门失败, 部门不存在"),
    UPDATE_ENABLE_FAILED_USER_EXISTS(31,"修改启用失败, 用户不存在"),
    ADD_FAILED_MATERAIL_TYPE_NOT_EXISTS(32, "新增失败, 物料类型不存在"),
    UPDATE_FAILED_MATERAIL_TYPE_NOT_EXISTS(33, "更新失败, 物料类型不存在"),
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
