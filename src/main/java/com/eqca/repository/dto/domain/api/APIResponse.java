package com.eqca.repository.dto.domain.api;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.BindingResult;



import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Objects;

/**
 * API공통 응답
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Schema(title = "API 공통응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class APIResponse<T> {

    /** 결과 상태 */
    @Schema(title = "결과 상태")
    private int status;
    
    /** 메시지 */
    @Schema(title = "메시지")
    private String message;

    /** 결과 데이터 */
    @Schema(title = "결과 데이터")
    private T data;

    /** 요청 응답 타임스탬프 */
    @Schema(title = "요청 응답 타임스탬프")
    private LocalDateTime timestamp;

    @Builder
    protected APIResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 성공 응답
     * @return APIResponse Object
     * @param <T> 빈값
     */
    public static <T> APIResponse<T> success() {
        return new APIResponse<>(200, "success", null);
    }

    /**
     * 성공 응답
     * @param data 응답 데이터
     * @return APIReponse Object
     * @param <T> 응답 데이터 타입
     */
    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(200, "success", data);
    }

    /**
     * 응답 객체 생성
     * @param status HttpStatus 값
     * @param message 응답 메시지
     * @return APIReponse Object
     * @param <T> 응답 데이터 타입
     */
    public static <T> APIResponse<T> of(int status, String message) {
        return new APIResponse<>(status, message, null);
    }

    /**
     * 응답 객체 생성
     * @param status HttpStatus 값
     * @param message 응답 메시지
     * @param data 응답 데이터
     * @return APIReponse Object
     * @param <T> 응답 데이터 타입
     */
    public static <T> APIResponse<T> of(int status, String message, T data) {
        return new APIResponse<>(status, message, data);
    }


    /**
     * 에러를 e.getBindingResult() 형태로 전달 받는 경우 해당 내용을 상세 내용으로 변경하는 기능을 수행한다.
     */
    @Getter
	public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        @Builder
        FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        @SuppressWarnings("unused")
		private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            Objects.toString(error.getRejectedValue(), ""),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
