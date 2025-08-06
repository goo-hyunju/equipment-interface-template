package com.eqca.common.exception.handler;

import com.eqca.repository.dto.domain.api.ErrorResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.eqca.common.exception.CustomException;
import com.eqca.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.io.IOException;
import java.util.Locale;

/**
 * Global Excpetion Handler
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Slf4j
@RestControllerAdvice("com.eqca.template")
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, Locale locale) {
        log.error("[GlobalException] handleMethodArgumentNotValidException ::\n"+ ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        // String fieldMessage = String.valueOf(stringBuilder); // TODO 활용 ..

        ErrorCode errCode = ErrorCode.ERROR_INVALID_VALUE;

        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex, Locale locale) {
        log.error("[GlobalException] MissingRequestHeaderException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_MISSING_HEADER;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 Body  '객체' 데이터가 넘어오지 않았을 경우
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, Locale locale) {
        log.error("[GlobalException] HttpMessageNotReadableException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_MISSING_BODY;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 request 에 '파라미터로' 데이터가 넘어오지 않았을 경우
     *
     * @param ex MissingServletRequestParameterException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
            MissingServletRequestParameterException ex, Locale locale) {
        log.error("[GlobalException] handleMissingServletRequestParameterException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_MISSING_PARAM;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     *
     * @param ex HttpClientErrorException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException ex, Locale locale) {
        log.error("[GlobalException] HttpClientErrorException.BadRequest ::\n", ex);

        ErrorCode errCode = ErrorCode.ERROR_BAD_REQUEST;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     *
     * @param ex NoHandlerFoundException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException ex, Locale locale) {
        log.error("[GlobalException] handleNoHandlerFoundExceptionException ::\n", ex);

        ErrorCode errCode = ErrorCode.ERROR_NOT_FOUND;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.NOT_FOUND);
    }

    /**
     * [Exception] NULL 값이 발생한 경우
     *
     * @param ex NullPointerException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex, Locale locale) {
        log.error("[GlobalException] handleNullPointerException ::\n", ex);

        ErrorCode errCode = ErrorCode.ERROR_BAD_REQUEST;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * Input / Output 내에서 발생한 경우
     *
     * @param ex IOException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException ex, Locale locale) {
        log.error("[GlobalException] handleIOException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_INVALID_IO;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * com.fasterxml.jackson.core 발생하는 경우
     *
     * @param ex JsonParseException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseExceptionException(JsonParseException ex, Locale locale) {
        log.error("[GlobalException] handleJsonParseExceptionException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_BAD_REQUEST;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);

    }

    /**
     * sql 문법에 오류가 있는 경우
     * @param ex BadSqlGrammarException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    protected ResponseEntity<ErrorResponse> handleBadSqlGrammarException(BadSqlGrammarException ex, Locale locale) {
        log.error("[GlobalException] handleBadSqlGrammarException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_SQL_GRAMMER;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * 중복 키 등록
     * @param ex DuplicateKeyException
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex, Locale locale) {
        log.error("[GlobalException] handleDuplicateKeyException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_BAD_REQUEST;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * '인증' 요청 중 오류 발생 시
     * @param ex AuthenticationException
     * @return ResponseEntity
     *//*
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, Locale locale) {
        log.error("[GlobalException] handleAuthenticationException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_BAD_REQUEST;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageConversionException(HttpMessageConversionException ex, Locale locale) {
        log.error("[GlobalException] handleHttpMessageConversionException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_BAD_REQUEST;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceAccessException.class)
    protected ResponseEntity<ErrorResponse> handleResourceAccessExceptionException(ResourceAccessException ex, Locale locale) {
        log.error("[GlobalException] handleResourceAccessExceptionException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_NOT_FOUND;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, Locale locale) {
        log.error("[GlobalException] IllegalArgumentException ::\n"+ ex.getMessage());

        ErrorCode errCode = ErrorCode.ERROR_BAD_REQUEST;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.NOT_FOUND);
    }

    /**
     * [Exception] 모든 Exception 경우 발생
     *
     * @param ex Exception
     * @return ResponseEntity APIResponse
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, Locale locale) {
        log.error("[GlobalException] Global Exception ::\n ", ex);

        ErrorCode errCode = ErrorCode.INTERNAL_SERVER_ERROR;
        String localizedMessage = messageSource.getMessage(
                errCode.getCode(),
                null,
                errCode.getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(errCode, localizedMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, Locale locale) {

        // Locale 기반으로 메시지 가져오기
        String localizedMessage = messageSource.getMessage(
                ex.getErrorCode().getCode(),
                ex.getArgs(),
                ex.getErrorCode().getDefaultMessage(),
                locale
        );
        return new ResponseEntity<>(ErrorResponse.of(ex.getErrorCode(),localizedMessage), HttpStatus.BAD_REQUEST);
    }

}
