package com.springboard.exception.custom.auth;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class PasswordMismatchException extends BusinessException {
    public PasswordMismatchException(){
        super(ErrorCode.PASSWORD_MISMATCH);
    }
}
