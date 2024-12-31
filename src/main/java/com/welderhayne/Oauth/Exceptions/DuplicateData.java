package com.welderhayne.Oauth.Exceptions;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateData extends DuplicateKeyException {
    public DuplicateData(String message) {
        super(message);
    }

    public DuplicateData(String message, Throwable error) {
        super(message, error);
    }
}
