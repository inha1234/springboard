package com.springboard.exception;

public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "해당 사용자를 찾을 수 없습니다."),
    INVALID_USER_INFORMATION("INVALID_USER_INFORMATION", "유저 정보가 유효하지 않습니다."),
    PASSWORD_MISMATCH("PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다."),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "토큰이 헤더에 포함되어 있지 않습니다."),
    TOKEN_MISMATCH("TOKEN_MISMATCH", "AccessToken과 RefreshToken 사용자가 일치하지 않습니다."),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}
