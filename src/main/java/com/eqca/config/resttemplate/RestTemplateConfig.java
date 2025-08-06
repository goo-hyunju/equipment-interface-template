package com.eqca.config.resttemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP 통신을 위한 Rest Template Configuration
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .connectTimeout(Duration.ofMillis(5000))  // 연결 최대 시간
                .readTimeout(Duration.ofMillis(5000))   // 데이터 주고받는 시간
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
//                .errorHandler(new RestTemplateErrorHandler())
                .build()
                ;
    }
}
