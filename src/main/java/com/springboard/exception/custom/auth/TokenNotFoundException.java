package com.springboard.exception.custom.auth;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class TokenNotFoundException extends BusinessException {
    public TokenNotFoundException() {
        super(ErrorCode.TOKEN_NOT_FOUND);
    }
}
