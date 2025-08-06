package com.eqca;

import java.util.List;

import com.altiall.scheduler.SchedulerObject;
import com.eqca.common.enums.reg.eq.EMdDeviceCtrlInfModel;
import com.eqca.common.enums.reg.eq.EMdDeviceCtrlInfType;
import com.eqca.common.enums.reg.eq.EMdDeviceCtrlModel;
import com.eqca.common.utils.LogUtils;
import com.eqca.controller.base.EquipmentContoller;
import com.eqca.repository.mapper.a.equipment.EquipmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.eqca.config.scheduler.SchedulerTaskService;
import com.eqca.controller.lower.melsecplc.MelsecPlcController;
import com.eqca.repository.dto.entity.MdDeviceCtrl;
import com.eqca.repository.dto.entity.MdDeviceCtrlMelsec;

import lombok.extern.slf4j.Slf4j;

/**
 * 시스템 시작시 동작하는 이벤트
 * @since 1.0
 * @author  kth
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AppStartEventListener implements ApplicationListener<ApplicationStartedEvent>{
	
	private final SchedulerTaskService taskManager;
	private final EquipmentMapper equipmentMapper;
	
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {

		try {

			EquipmentContoller equipmentContoller = new EquipmentContoller();

			taskManager.registerTask(
					SchedulerObject.builder()
							.id("EquipmentController").description("EquipmentController").period(1000)
							.task(equipmentContoller.getTask())
							.iSchedulerTask(equipmentContoller).build());

			setDeviceController();
			
			taskManager.startAllTasks(2000);
			
	    }catch(Exception e) {
			log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
	    }
	}

	/**
	 * 장비 제어 컨트롤러 등록 (ref MdDeviceCtrl)
	 */
	private void setDeviceController(){
		try{
			//제어 컨트롤러 정보 조회
			List<MdDeviceCtrl> ctrlList = equipmentMapper.selectMdDeviceCtrl();

			//제어컨트롤에 있는 데이터를 통해 실제 적용해야할 Controller를 준비
			for(MdDeviceCtrl ctrl : ctrlList) {
				if(ctrl.getUseYn().equals("N"))
					continue;

				//제어컨트롤러의 Melsec ReadData를 DB에서 읽어옴.
				switch(EMdDeviceCtrlModel.getCode(ctrl.getCfgModel())) {
					case EMdDeviceCtrlModel.NONE:{

						log.error(LogUtils.errorLog(new Exception("제어불가 제어기 모델"), "[Unknown.제어기모델:{}][{}]", ctrl.getCfgModel(), ctrl));
					}break;
					case EMdDeviceCtrlModel.M_PLC:{

						if( EMdDeviceCtrlInfModel.getCode(ctrl.getInfModel()) == EMdDeviceCtrlInfModel.CLIENT) {

							MelsecPlcController plcController = new MelsecPlcController();
							taskManager.registerTask(
									SchedulerObject.builder()
											.id(ctrl.getCfgCode()).description(ctrl.getCfgName()).period(1000)
											.task(plcController.getTask())
											.iSchedulerTask(plcController).build());
						} else {
							//todo : Melsec Server Listener
						}
					}break;
					case EMdDeviceCtrlModel.S_PLC:{
						log.info(LogUtils.basic("setDeviceController", "TODO : {}", EMdDeviceCtrlModel.S_PLC.toString()));
					}break;
					case EMdDeviceCtrlModel.L_PLC:{
						log.info(LogUtils.basic("setDeviceController", "TODO : {}", EMdDeviceCtrlModel.L_PLC.toString()));
					}break;
					case EMdDeviceCtrlModel.NORMAL:{

						if(EMdDeviceCtrlInfType.getCode(ctrl.getInfType()) == EMdDeviceCtrlInfType.SOCKET) {
							if( EMdDeviceCtrlInfModel.getCode(ctrl.getInfModel()) == EMdDeviceCtrlInfModel.CLIENT) {
								// todo : Socket Client
							}else if(EMdDeviceCtrlInfModel.getCode(ctrl.getInfModel()) == EMdDeviceCtrlInfModel.SERVER) {
								// todo : Socket Server Listener
							}
						}

						log.info(LogUtils.basic("setDeviceController", "TODO : {}", EMdDeviceCtrlModel.NORMAL.toString()));
					}break;
					default:{

					}break;
				}
			}

		}catch (Exception e) {
			log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
		}
	}
}
