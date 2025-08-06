package com.eqca.repository.dto.domain.api;

import com.eqca.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * API 오류 응답
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Getter
public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;
    private final LocalDateTime timestamp;

    @Builder
    protected ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * API 오류 응답 생성
     * @param errorCode 오류 코드
     * @param message 오류 메시지
     * @return 오류 응답 객체
     */
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

}