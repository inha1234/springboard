package com.springboard.exception.custom.postlike;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class LikeNotFoundException extends BusinessException {
    public LikeNotFoundException(){
        super(ErrorCode.LIKE_NOT_FOUND);
    }
}
