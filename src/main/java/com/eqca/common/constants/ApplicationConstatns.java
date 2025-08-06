package com.eqca.common.constants;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * YML 값 출력 테스트용
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Component
@Slf4j
public class ApplicationConstatns {

    /** TODO : 어플리케이션 이름 */
    @Value("${spring.application.name}")
    public String applicationName;

    /**
     * 어플리케이션 명을 출력한다.
     */
    @PostConstruct
    public void printApplicationName() {

        log.info("applicationName: {}", applicationName);
    }
}
