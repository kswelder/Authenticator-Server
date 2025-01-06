package com.welderhayne.Oauth.Exceptions;

public class XssCharTokenInvalidException extends IllegalArgumentException {
    public XssCharTokenInvalidException(String message) {
        super(message);
    }

    public XssCharTokenInvalidException(String message, Throwable error) {
        super(message, error);
    }
}
