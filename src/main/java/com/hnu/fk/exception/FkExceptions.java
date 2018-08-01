package com.hnu.fk.exception;

/**
 * SecurityException
 * 
 * @author zhouweixin
 *
 */
public class FkExceptions extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Integer code;

	public FkExceptions(EnumExceptions exceptionsEnum) {
		super(exceptionsEnum.getMessage());

		this.code = exceptionsEnum.getCode();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
