package com.wr.exe;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -5598865415547474216L;

    public ServiceException() {
        super();
    }
    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }

}
