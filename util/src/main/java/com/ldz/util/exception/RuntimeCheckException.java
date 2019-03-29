package com.ldz.util.exception;

public class RuntimeCheckException extends RuntimeException{
    private static final long serialVersionUID = -9221941718593423481L;

    public RuntimeCheckException(String message) {
        super(message);
    }

    /**
     * 重写fillInStackTrace方法会使得这个自定义的异常不会收集线程的整个异常栈信息，会大大
     * 减少异常开销。
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
