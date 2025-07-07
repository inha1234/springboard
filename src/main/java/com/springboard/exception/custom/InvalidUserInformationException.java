package com.springboard.exception.custom;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class InvalidUserInformationException extends BusinessException {
    public InvalidUserInformationException(){
        super(ErrorCode.INVALID_USER_INFORMATION);
    }
}
