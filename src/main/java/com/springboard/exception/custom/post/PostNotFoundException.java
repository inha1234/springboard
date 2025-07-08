package com.springboard.exception.custom.post;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class PostNotFoundException extends BusinessException {
    public PostNotFoundException(){
        super(ErrorCode.POST_NOT_FOUND);
    }
}
