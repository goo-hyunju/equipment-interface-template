package com.eqca;

import com.eqca.common.constants.ApplicationConstatns;
import com.eqca.common.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.eqca.config.scheduler.SchedulerTaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * 시스템 종료시 동작하는 이벤트
 * @since 1.0
 * @author  kth
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AppStopEventListener implements ApplicationListener<ContextClosedEvent>{
	
	private final SchedulerTaskService taskManager;
	
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		
		taskManager.stopAllTasks();
		//log.info(LogUtils.basic("[SHUTDOWN][Server-{}]", ApplicationConstatns.applicationName));
	}

}
