package com.eqca.config.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * 다국어 지원을 위한 Configuration
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Configuration
public class MessageConfig {

    /**
     * Locale 정보를 관리 방법 정의
     * @return LocaleResolver
     */
    @Bean
    LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);

        return localeResolver;
    }

    /**
     * 메세지 소스 구성
     * @return ReloadableResourceBundleMessageSource
     */
    @Bean
    ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages/message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(180);

        return messageSource;
    }
}
