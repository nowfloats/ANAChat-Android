package com.nowfloats.chatsdk.internal.network;

import com.nowfloats.chatsdk.internal.utils.ExceptionType;

public class RootAPIException extends RuntimeException {
    public final String message;
    public final Exception exception;
    public final ExceptionType exceptionType;

    private RootAPIException(Exception exception, ExceptionType exceptionType, String message) {
        this.exception = exception;
        this.exceptionType = exceptionType;
        this.message = message;
    }

    public static RootAPIException wrap(Exception e) {
        return wrap(e, (ExceptionType)null);
    }

    public static RootAPIException wrap(Exception e, ExceptionType type) {
        return wrap(e, type, (String)null);
    }

    public static RootAPIException wrap(Exception e, ExceptionType type, String message) {
        Exception exception;
        Object exceptionType;
        if(e instanceof RootAPIException) {
            RootAPIException rootAPIException = (RootAPIException)e;
            exception = rootAPIException.exception;
            if(type == null) {
                exceptionType = rootAPIException.exceptionType;
            } else {
                exceptionType = type;
            }

            if(message == null) {
                message = rootAPIException.message;
            }
        } else {
            exception = e;
            if(type == null) {
                exceptionType = UnexpectedException.GENERIC;
            } else {
                exceptionType = type;
            }
        }

        return new RootAPIException(exception, (ExceptionType)exceptionType, message);
    }

    public int getServerStatusCode() {
        int statusCode = 0;
        if(this.exceptionType instanceof NetworkException) {
            statusCode = ((NetworkException)this.exceptionType).serverStatusCode;
        }

        return statusCode;
    }

    public boolean shouldLog() {
        return this.exception != null;
    }
}
