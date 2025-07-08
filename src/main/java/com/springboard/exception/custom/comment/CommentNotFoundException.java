package com.springboard.exception.custom.comment;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class CommentNotFoundException extends BusinessException {
    public CommentNotFoundException(){
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
