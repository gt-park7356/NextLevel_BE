package com.example.NextLevel.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Member errors
    DUPLICATE_MEMBER(HttpStatus.CONFLICT,       "DUPLICATE_MEMBER",       "이미 사용 중인 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT,        "DUPLICATE_EMAIL",        "이미 사용 중인 이메일입니다."),
    NOT_MATCHED_PASSWORD(HttpStatus.BAD_REQUEST,"NOT_MATCHED_PASSWORD",  "현재 비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,        "NOT_FOUND_USER",         "사용자를 찾을 수 없습니다."),

    // Post errors
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,        "POST_NOT_FOUND",         "요청하신 게시글을 찾을 수 없습니다."),
    FORBIDDEN_POST(HttpStatus.FORBIDDEN,        "FORBIDDEN_POST",         "해당 게시글에 접근 권한이 없습니다."),
    NOT_MATCHED_AUTHOR(HttpStatus.FORBIDDEN,    "NOT_MATCHED_AUTHOR",     "사용자에 관한 접근 권한이 없습니다."),

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST,       "INVALID_INPUT",          "잘못된 입력값입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",    "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status  = status;
        this.code    = code;
        this.message = message;
    }

    public HttpStatus getStatus()  { return status;  }
    public String     getCode()    { return code;    }
    public String     getMessage() { return message; }
}
