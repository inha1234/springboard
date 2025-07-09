package com.springboard.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_USER_INFORMATION("INVALID_USER_INFORMATION", "유저 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH("PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    POST_NOT_FOUND("POST_NOT_FOUND", "해당 게시글이 존재하지 않거나 삭제된 게시글입니다.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", "댓글이 존재하지 않거나 삭제된 댓글입니다.", HttpStatus.NOT_FOUND),
    COMMENT_ALREADY_DELETED("COMMENT_ALREADY_DELETED","삭제된 댓글은 수정할 수 없습니다.", HttpStatus.CONFLICT),
    COMMENT_UPDATE_FORBIDDEN("COMMENT_UPDATE_FORBIDDEN","작성자만 댓글을 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    COMMENT_DELETE_FORBIDDEN("COMMENT_DELETE_FORBIDDEN","작성자만 댓글을 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    PARENT_COMMENT_NOT_FOUND("PARENT_COMMENT_NOT_FOUND","부모 댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ALREADY_LIKED("ALREADY_LIKED", "이미 좋아요를 눌렀습니다.",HttpStatus.CONFLICT),
    LIKE_NOT_FOUND("LIKE_NOT_FOUND", "해당 게시글에 대한 좋아요 기록이 없습니다.", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "토큰이 헤더에 포함되어 있지 않습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_MISMATCH("TOKEN_MISMATCH", "AccessToken과 RefreshToken 사용자가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getHttpStatus() { return httpStatus; }
}
