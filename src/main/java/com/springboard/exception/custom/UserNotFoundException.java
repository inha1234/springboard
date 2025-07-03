package com.springboard.exception.custom;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(){
        super(ErrorCode.USER_NOT_FOUND);
    }
}
