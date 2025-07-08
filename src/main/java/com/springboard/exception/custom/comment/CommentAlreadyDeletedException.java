package com.springboard.exception.custom.comment;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class CommentAlreadyDeletedException extends BusinessException {
    public CommentAlreadyDeletedException(){
        super(ErrorCode.COMMENT_ALREADY_DELETED);
    }
}
