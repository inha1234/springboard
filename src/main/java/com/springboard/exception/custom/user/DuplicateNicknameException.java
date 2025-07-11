package com.springboard.exception.custom.user;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class DuplicateNicknameException extends BusinessException {
    public DuplicateNicknameException(){
        super(ErrorCode.DUPLICATE_NICKNAME);
    }
}
