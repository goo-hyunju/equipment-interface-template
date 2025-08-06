package com.eqca.config.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Task 및 Scheduler Configuration
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Configuration
public class SchedulerConfig {

    @Primary
    @Bean
    ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(8);   // 스레드 풀 크기
        scheduler.setThreadNamePrefix("ORCA-Task-");   // 스케쥴러 이름 프리픽스
        scheduler.setWaitForTasksToCompleteOnShutdown(true); // 종료 시 남은 작업 완료 후 종료
        scheduler.setAwaitTerminationSeconds(30); // 애플리케이션 종료 시 대기 시간
        return scheduler;
    }
}
