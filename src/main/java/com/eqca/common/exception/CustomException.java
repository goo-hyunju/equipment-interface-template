package com.eqca.common.exception;

import lombok.Getter;

/**
 * 사용자 Exception
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Getter
public class CustomException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2323654815144755990L;
	
	private final ErrorCode errorCode;
    private final Object[] args;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.args = null;
    }

    public CustomException(ErrorCode errorCode, Object... args) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.args = args;
    }

}
