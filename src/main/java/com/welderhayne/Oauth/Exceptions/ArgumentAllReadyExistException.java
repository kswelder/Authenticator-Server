package com.welderhayne.Oauth.Exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ArgumentAllReadyExistException extends DataIntegrityViolationException {
    public ArgumentAllReadyExistException(String messager) {
        super(messager);
    }

    public ArgumentAllReadyExistException(String messager, Throwable error) {
        super(messager, error);
    }
}
