package com.eqca.common.exception.handler;

import com.eqca.common.exception.CustomException;
import com.eqca.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * RestTemplate Error Handler (필요시 사용)
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Slf4j
@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(@NonNull URI url, @NonNull HttpMethod method, @NonNull ClientHttpResponse response) throws IOException {
        String body = getResponseBody(response);

        // Client Error, Server 구분 하고 싶을 때 사용
        if (response.getStatusCode().is4xxClientError()) {
            log.error("API Call ClientError URI ={}:{}, StatusCode={}, StatusText ={}, body ={}", url, method, response.getStatusCode().toString(), response.getStatusText(), body);
            throw new CustomException(ErrorCode.CUSTOM_MESSAGE, response.getStatusCode());
        } else if (response.getStatusCode().is5xxServerError()) {
            log.error("API Call ServerError URI ={}:{}, StatusCode={}, StatusText ={}, body ={}", url, method, response.getStatusCode().toString(), response.getStatusText(), body);
            throw new CustomException(ErrorCode.ERROR_REST_TEMPLATE);
        }

        log.error("API Call Error URI ={}:{}, StatusCode={}, StatusText ={}, body ={}", url, method, response.getStatusCode().toString(), response.getStatusText(), body);
        throw new CustomException(ErrorCode.ERROR_REST_TEMPLATE);

    }

    private String getResponseBody(ClientHttpResponse response) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
