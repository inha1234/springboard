package com.springboard.exception.custom.postlike;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class AlreadyLikedException extends BusinessException {
    public AlreadyLikedException(){
        super(ErrorCode.ALREADY_LIKED);
    }
}
