package com.eqca.common.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        // OPTIONS 요청은 필터링 제외
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String[] excludePath = {
                "/favicon.ico",
                "/assets",
                "/view",
                "/docs",
                "/ws",
        };

        String path = normalizePath(request.getRequestURI());
        return Arrays.stream(excludePath)
                .anyMatch(exclude -> normalizePath(exclude).equals(path) || path.startsWith(normalizePath(exclude)));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("CustomAuthenticationFilter   RequestUri=[{}]", request.getRequestURI());

        if(request.getRequestURI().equals("/")){
            filterChain.doFilter(request, response);
            return;
        }

        // TODO : 인증 정보 확인, 권한 정보 확인

        filterChain.doFilter(request, response);
    }

    /**
     * 경로 정규화: 끝에 "/"가 있을 경우 제거
     */
    private String normalizePath(String path) {
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }
}
