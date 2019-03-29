package com.ldz.obd.exception;

/**
 * 通信有异常时，在这里抛出异常
 */
public class IotException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3436821628329600678L;

	public IotException(String message) {
		super(message);
	}
}
