package com.eqca.config.core;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.eqca.common.interceptor.APILoggingInterceptor;
import com.eqca.common.interceptor.LanguageInterceptor;

/**
 * 웹 설정
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 정적 리소스(ex.이미지, CSS, js 등) 처리 정의
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/resources/");
    }

    /**
     * 인터셉터 등록
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new APILoggingInterceptor())
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/assets/**", "/**.html", "/**.svg", "/**.ico");
        registry.addInterceptor(new LanguageInterceptor());
    }

    /**
     * 커스텀 포맷이다 컨버터 등록
     * (ex 날짜, 숫자 등)
     * @param registry FormatterRegistry
     */
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ISO_DATE);
        registrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME);
    }


    /**
     * CORS (Cross-Origin Resource Sharing) 설정
     * TODO : CORS 뀨칙
     * @param registry CorsRegistry
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name());
    }

}
