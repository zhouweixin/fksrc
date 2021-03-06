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
    DELETE_FAILED_REF_KEY_EXISTS(6, "外键约束错误"),
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
	ADD_FAILED_DICVALUE_DUPLICATE(24, "新增失败, 值重复"),
	ADD_FAILED_DATAPARENT_NOT_EXISTS(25,"新增失败,父编码不存在"),
    UPDATE_FAILED_SON_NOT_PARENT(26,"部门修改失败, 不可把子部门设置为父部门"),
    ASSIGN_FAILED_ROLE_NOT_EXISTS(27,"分配失败, 角色不存在"),
	SEARCH_FAILED_NOT_EXIST(28,"查询失败，不存在"),
    UPDATE_DEPARTMENT_FAILED_USER_NOT_EXISTS(29,"修改部门失败, 用户不存在"),
    UPDATE_DEPARTMENT_FAILED_DEPARTMENT_NOT_EXISTS(30,"修改部门失败, 部门不存在"),
    UPDATE_ENABLE_FAILED_USER_EXISTS(31,"修改启用失败, 用户不存在"),
    ADD_FAILED_MATERAIL_TYPE_NOT_EXISTS(32, "新增失败, 物料类型不存在"),
    UPDATE_FAILED_MATERAIL_TYPE_NOT_EXISTS(33, "修改失败, 物料类型不存在"),
    ADD_FAILED_ENTER_USER_NOT_EXISTS(34, "录入失败, 录入人不存在"),
    UPDATE_FAILED_MODIFY_USER_NOT_EXISTS(35, "修改失败, 修改人不存在"),
	SEARCH_FAILED_NAME_NULL(36,"查询失败，名称不能为空"),
    EXPORT_FAILED_FIELD_NULL(37,"导出失败, 字段为空"),
    EXPORT_FAILED_DATA_NULL(38,"导出失败, 数据为空"),
	ADD_FAILED_STANDING_BOOK_TYPE_NOT_EXISTS(39, "新增失败, 台账项目类别不存在"),
	UPDATE_FAILED_STANDING_BOOK_TYPE_NOT_EXISTS(40,"修改失败,台账项目类别不存在"),
	ADD_FAILED_SECTION_NOT_EXISTS(41,"新增失败,工段类型不存在"),
	UPDATE_FAILED_SECTION_NOT_EXISTS(42,"更新失败,工段类型不存在"),
	ADD_FAILED_USER_CODE_ENTER_NOT_EXISTS(43, "录入失败, 调度员不存在"),
	ADD_FAILED_BanCi_NOT_EXISTS(44,"录入失败,班次不存在"),
    DATA_ENTERED(45,"数据已录入, 不要重复录入"),
    REPORT_GENERATED(46,"该月报表已生成, 不要重复生成"),
    REPORT_GENERATED_FAILED_PLAN_DATA_NOT_ENTERED(47,"报表生成失败, 该月计划数据未录入"),
    REPORT_GENERATED_FAILED_PLAN_DATA_NULL(48,"报表生成失败, 该月计划数据为空"),
    REPORT_GENERATED_FAILED_MONTH_NOT_EXISTS(49,"报表生成失败, 月统计时间为空"),
    REPORT_GENERATED_FAILED_ENTER_USER_NOT_EXISTS(50, "报表生成失败, 录入人不存在"),

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
