package com.springboard.exception.custom.comment;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class ParentCommentNotFoundException extends BusinessException {
    public ParentCommentNotFoundException(){
        super(ErrorCode.PARENT_COMMENT_NOT_FOUND);
    }
}
