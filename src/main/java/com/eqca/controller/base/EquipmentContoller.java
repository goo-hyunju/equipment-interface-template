package com.eqca.controller.base;

import com.altiall.scheduler.ISchedulerTask;
import com.eqca.common.utils.LogUtils;
import com.eqca.controller.base.event.EqInjectionEvent;
import com.eqca.controller.base.event.EqInjectionEventListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

/**
 * 설비 제어를 위한 Controller
 * @since 1.0
 * @author  kth
 * @version 1.0
 */
@Controller
@Slf4j
public class EquipmentContoller implements ISchedulerTask, EqInjectionEventListener {
	
	//private final EquipmentService equipmentService;
	
	public EquipmentContoller() {
		//this.equipmentService = equipmentService;
	}

	@Override
	public void initScheduler(String s) {

	}

	@Override
	public void shutdownScheduler() {

	}

	@Override
	public Runnable getTask() {
		return this::taskService;
	}

	private void taskService() {

	}


	@Override
	@EventListener
	@Async
	public void handleEqInjectionEvent(EqInjectionEvent event) {
		try {
			log.info("[이벤트 수신] EqInjectionEvent: eqCode={}, deviceCode={}, eqCtrlCode={}, eventId={}", event.getEqCode(), event.getDevicCode(), event.getEqCtrlCode(), event.getEventId());
			log.info("[이벤트 수신] event data: {}", event.getParameter());
			Thread.sleep(5000);
			log.info("[이벤트 수신] event end");
			String eventId = event.getEventId();
			switch (eventId) {
				case "LOADING":{

				}break;
				case "UNLOADING":{

				}break;
				case "REQLOADING":{

				}break;
				case "RESULT":{

				}break;
				default:{
					log.info(LogUtils.basic("startTask", "[UNKNOWN EVENT][EqInjectionEvent:{}][{}, {}, {}, {}]", event.getEqCode(), event.getDevicCode(), event.getEqCtrlCode(), eventId, event.getParameter()));
				}
			}
		}catch (Exception e) {
			log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
		}
	}

}
