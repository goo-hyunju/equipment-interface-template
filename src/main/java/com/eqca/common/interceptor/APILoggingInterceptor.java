package com.eqca.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APILoggingInterceptor implements HandlerInterceptor {

	@Autowired
	ObjectMapper objectMapper;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
//		log.debug("Request RequestUri={}, RequestUrl ={}, RequestMethod ={}, QueryString ={}, RequestAddr ={}, ContentLength ={}",
//				request.getRequestURI(),
//				request.getRequestURL(),
//				request.getMethod(),
//				request.getQueryString(),
//				request.getRemoteAddr(),
//				request.getContentLength());

//		Enumeration<String> headerNames = request.getHeaderNames();
//		headerNames.asIterator().forEachRemaining(headerName -> {
//			log.debug("\t[Header] {}= {}", headerName, request.getHeader(headerName));
//		});

        log.debug("APILoggingInterceptor        RequestUri=[{}], QueryString=[{}], ContentLength=[{}], ResponseStatus=[{}]",
                request.getRequestURI(),
                request.getQueryString(),
                request.getContentLength(),
                response.getStatus());

        return true;
    }
}