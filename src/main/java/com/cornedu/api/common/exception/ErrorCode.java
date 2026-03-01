package com.cornedu.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    DUPLICATE_ID(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
