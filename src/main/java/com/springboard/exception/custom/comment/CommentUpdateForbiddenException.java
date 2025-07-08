package com.springboard.exception.custom.comment;

import com.springboard.exception.BusinessException;
import com.springboard.exception.ErrorCode;

public class CommentUpdateForbiddenException extends BusinessException {
    public CommentUpdateForbiddenException(){
        super(ErrorCode.COMMENT_UPDATE_FORBIDDEN);
    }
}
