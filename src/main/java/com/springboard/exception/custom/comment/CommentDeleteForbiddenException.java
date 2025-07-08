package com.springboard.exception.custom.comment;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class CommentDeleteForbiddenException extends BusinessException {
    public CommentDeleteForbiddenException(){
        super(ErrorCode.COMMENT_DELETE_FORBIDDEN);
    }
}
