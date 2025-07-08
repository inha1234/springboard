package com.springboard.exception.custom.auth;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class TokenMismatchException extends BusinessException {
    public TokenMismatchException() {
        super(ErrorCode.TOKEN_MISMATCH);
    }
}
