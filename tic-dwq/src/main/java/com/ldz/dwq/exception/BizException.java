package com.ldz.dwq.exception;

/**
 * 业务异常类，抛出了业务异常，将不主动关闭连接通道
 * @author Lee
 *
 */
public class BizException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BizException(String msg) {
		super(msg);
	}
}
