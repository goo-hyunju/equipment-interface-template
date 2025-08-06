package com.eqca.config.scheduler;

import com.altiall.scheduler.SchedulerObject;
import com.eqca.common.enums.reg.eq.EMdDeviceCtrlModel;
import com.eqca.common.utils.LogUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 스케쥴러 작업 목록과 실행 상태를 관리하기 위한 매니저
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerTaskService {

    private final ThreadPoolTaskScheduler scheduler;

    /** 등록된 작업 목록 */
    @Getter
    private final Map<String, SchedulerObject> taskRegistry = new ConcurrentHashMap<>();

    /**  실행 중인 작업 목록 */
    @Getter
    private final Map<String, ScheduledFuture<?>> runningTasks = new ConcurrentHashMap<>();

    /**
     * Task 등록
     * @param schedulerObject 스케쥴러 정보
     */
    public void registerTask(SchedulerObject schedulerObject) {

        try {
            if (taskRegistry.containsKey(schedulerObject.getId())) {
                throw new IllegalArgumentException("Task with ID " + schedulerObject.getId() + " already exists.");
            }
            taskRegistry.put(schedulerObject.getId(), schedulerObject);
            log.info(LogUtils.basic("registerTask", "[SCHEDULER.REG:{}]", schedulerObject));
        }catch (Exception e){
            log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
        }
    }

    /**
     * Task 시작
     * @param schedulerId 스케쥴러 ID
     */
    public void startTask(String schedulerId) {

        try {
            if (!taskRegistry.containsKey(schedulerId)) {
                throw new IllegalArgumentException("Task with ID " + schedulerId + " is not registered.");
            }
            if (runningTasks.containsKey(schedulerId)) {
                throw new IllegalStateException("Task with ID " + schedulerId + " is already running.");
            }

            SchedulerObject schedulerObject = taskRegistry.get(schedulerId);
            schedulerObject.getISchedulerTask().initScheduler(schedulerId);

            ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(schedulerObject.getTask(), Instant.now(), Duration.ofMillis(schedulerObject.getPeriod()));
            runningTasks.put(schedulerObject.getId(), future);
            schedulerObject.setActive(true);

            log.info(LogUtils.basic("startTask", "[SCHEDULER.START:{}]", schedulerObject));
        }catch (Exception e){
            log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
        }
    }

    /**
     * Task 중지
     * @param schedulerId 스케쥴러 ID
     */
    public void stopTask(String schedulerId) {

        try {
            ScheduledFuture<?> future = runningTasks.get(schedulerId);
            if (future != null) {
                future.cancel(false);
                runningTasks.remove(schedulerId);

                SchedulerObject schedulerObject = taskRegistry.get(schedulerId);
                schedulerObject.getISchedulerTask().shutdownScheduler();
                schedulerObject.setActive(false);

                log.info(LogUtils.basic("stopTask", "[SCHEDULER.STOP:{}]", schedulerObject));
            }
        }catch (Exception e){
            log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
        }
    }

    /**
     * 모든 Task 시작
     */
    public void startAllTasks(long intervalMillis) {

        for (String schedulerId : taskRegistry.keySet()) {
            startTask(schedulerId);
        }
    }

    /**
     * 모든 Task 중지
     */
    public void stopAllTasks() {
        for (String taskId : runningTasks.keySet()) {
            stopTask(taskId);
        }
    }

    /**
     * Task 상태 조회
     * @return TaskId, 상태를 포함한 Map 객체
     */
    public Map<String, Boolean> getTaskStatuses() {
        Map<String, Boolean> statuses = new ConcurrentHashMap<>();
        taskRegistry.forEach((taskId, task) -> statuses.put(taskId, runningTasks.containsKey(taskId)));
        return statuses;
    }
}
