package com.webserver.exception;

/**
 * @author LeafDust
 * @create 2019-08-23 10:13
 */
public class NullRequestException extends Exception{
    public NullRequestException() {
        super();
    }

    public NullRequestException(String message) {
        super(message);
    }

    public NullRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullRequestException(Throwable cause) {
        super(cause);
    }

    protected NullRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
