package com.jnafamily.winproctools.exception;

public class JnaOperationException extends RuntimeException {

    public JnaOperationException() {
    }

    public JnaOperationException(String message) {
        super(message);
    }

    public JnaOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JnaOperationException(Throwable cause) {
        super(cause);
    }

    public JnaOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
