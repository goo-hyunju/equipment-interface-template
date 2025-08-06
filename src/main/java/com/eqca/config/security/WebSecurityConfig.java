package com.eqca.config.security;

/*
import com.eqca.common.filter.CustomAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
*/

/**
 * TODO : security 적용 범위
 */
//@EnableWebSecurity
//@Configuration
public class WebSecurityConfig {
/*
    //@Bean
    CustomAuthenticationFilter customAuthenticationFilter() {
        return new CustomAuthenticationFilter();
    }

    //@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .headers((headerConfig) -> headerConfig
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//            .authorizeHttpRequests((authorize) -> authorize
//                            .dispatcherTypeMatchers(FORWARD).permitAll()
//                            .requestMatchers("/assets/**", "/docs/**", "*.html", "*.svg", "*.ico").permitAll()
//                            .requestMatchers("/api/login").permitAll()
//                            .requestMatchers("/api/**").permitAll()
//                            .requestMatchers("/").permitAll()
//                    .anyRequest().authenticated())
            .authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
            // Spring Security JWT Filter Load
            .addFilterBefore(customAuthenticationFilter(), BasicAuthenticationFilter.class)
        ;
        return http.build();
    }

 */
}
