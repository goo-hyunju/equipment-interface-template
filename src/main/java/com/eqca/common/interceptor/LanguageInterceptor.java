package com.eqca.common.interceptor;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LanguageInterceptor implements HandlerInterceptor {

    @SuppressWarnings("unused")
	@Autowired
    private MessageSource messageSource;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String language = request.getHeader("Accept-Language");
        Locale locale = (language != null && !language.isEmpty()) ? Locale.forLanguageTag(language.replace("_", "-")) : Locale.ENGLISH;

        LocaleContextHolder.setLocale(locale);
        return true;
    }
}