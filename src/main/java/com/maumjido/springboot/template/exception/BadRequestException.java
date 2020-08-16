
package com.maumjido.springboot.template.exception;

public class BadRequestException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9218894624533726526L;

    public BadRequestException(String msg) {
        super(msg);
    }
}
