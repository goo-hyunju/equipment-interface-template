package com.eqca.repository.dto.domain.api;

import lombok.Getter;

/**
 * API 요청 시 BASE DTO (고정..)
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Getter
public class APIRequestBase {
    private String apiKey;
    private String messageId;
    private String telegramId;
    private String messageNo;
}
