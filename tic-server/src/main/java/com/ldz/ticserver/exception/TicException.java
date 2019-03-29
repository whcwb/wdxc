package com.ldz.ticserver.exception;

/**
 * 业务异常处理
 * @author Lee
 *
 */
public class TicException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 735284531191832483L;

	public TicException(String message) {
		super(message);
	}
}
