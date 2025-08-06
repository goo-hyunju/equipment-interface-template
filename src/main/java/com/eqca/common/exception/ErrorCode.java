package com.eqca.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Error Code
 * (다국어 code 연결)
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    CUSTOM_MESSAGE("CUSTOM_MESSAGE", "[DefaultMessage]FAIL"),

    ERROR_BAD_REQUEST("ERROR_BAD_REQUEST","[DefaultMessage]ERROR_BAD_REQUEST"),
    ERROR_INVALID_IO("ERROR_INVALID_IO","[DefaultMessage]ERROR_INVALID_IO"),
    ERROR_INVALID_VALUE("ERROR_INVALID_VALUE","[DefaultMessage]ERROR_INVALID_VALUE"),
    ERROR_MISSING_PARAM("ERROR_MISSING_PARAM","[DefaultMessage]ERROR_MISSING_PARAM"),
    ERROR_MISSING_HEADER("ERROR_MISSING_HEADER","[DefaultMessage]ERROR_MISSING_HEADER"),
    ERROR_MISSING_BODY("ERROR_MISSING_BODY","[DefaultMessage]ERROR_MISSING_BODY"),
    ERROR_JSON_PARSE("ERROR_JSON_PARSE","[DefaultMessage]ERROR_JSON_PARSE"),
    ERROR_SQL_GRAMMER("ERROR_SQL_GRAMMER","[DefaultMessage]ERROR_SQL_GRAMMER"),
    ERROR_NOT_FOUND("ERROR_NOT_FOUND","[DefaultMessage]ERROR_NOT_FOUND"),
    ERROR_RESOURCE_FAIL("ERROR_RESOURCE_FAIL","[DefaultMessage]ERROR_RESOURCE_FAIL"),
    ERROR_NULL("ERROR_NULL","[DefaultMessage]ERROR_NULL"),
    ERROR_REST_TEMPLATE("ERROR_REST_TEMPLATE","[DefaultMessage]ERROR_REST_TEMPLATE"),
    ERROR_MESSAGE_CONVERT("ERROR_MESSAGE_CONVERT","[DefaultMessage]ERROR_MESSAGE_CONVERT"),

    USER_NOT_FOUND("USER_NOT_FOUND", "[DefaultMessage]User not found"),
    INVALID_REQUEST("INVALID_REQUEST", "[DefaultMessage]Invalid request parameter"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "[DefaultMessage]Internal server error");

    private final String code;
    private final String defaultMessage;


}
