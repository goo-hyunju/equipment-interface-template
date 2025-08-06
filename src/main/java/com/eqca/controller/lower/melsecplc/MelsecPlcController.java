package com.eqca.controller.lower.melsecplc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.altiall.plc.mitsubishi.MelsecData;
import com.altiall.plc.mitsubishi.format.MelsecSeries;
import com.altiall.plc.mitsubishi.protocol.qmelsec.q3e.MelsecQ3E;
import com.altiall.scheduler.ISchedulerTask;
import com.altiall.socket.java.ClientListener;
import com.altiall.socket.java.SyncSocketClient;
import com.eqca.common.utils.LogUtils;
import com.eqca.config.core.BeanUtils;
import com.eqca.controller.base.event.EqInjectionEventPublish;
import com.eqca.controller.lower.event.EqControlEvent;
import com.eqca.controller.lower.event.EqControlEventListener;
import com.eqca.controller.lower.melsecplc.service.custom.PlcRelayService;
import com.eqca.repository.dto.entity.MdDeviceCtrl;
import com.eqca.repository.dto.entity.MdDeviceCtrlMelsec;
import com.eqca.repository.mapper.a.equipment.EquipmentMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 *
 */
@Slf4j
public class MelsecPlcController implements ISchedulerTask, ClientListener, EqControlEventListener {

	private final EquipmentMapper equipmentMapper;
	private final EqInjectionEventPublish eqInjectionEventPublish;
	private final PlcRelayService plcRelayService;
	private SyncSocketClient syncSocketClient;
	private String eqCtrlCode;
	private String eqCtrlDesc;
	private String hostIp;
	private int hostPort;
	private MelsecSeries.CommunicateFormat eqCtrlDataType;
	private List<MdDeviceCtrlMelsec> mdDeviceCtrlMelsec;
	protected StringBuffer totalRecvStr;
	private boolean writeTest = false;

	public MelsecPlcController() {
		this.equipmentMapper = (EquipmentMapper) BeanUtils.getBean(EquipmentMapper.class);
		this.eqInjectionEventPublish = (EqInjectionEventPublish) BeanUtils.getBean(EqInjectionEventPublish.class);
		this.plcRelayService = (PlcRelayService) BeanUtils.getBean(PlcRelayService.class);
	}

	// region [+] ClientListener (ISchedulerTask Event)
	@Override
	public void initScheduler(String schedulerId) {
		try {
			List<MdDeviceCtrl> ctrlList = this.equipmentMapper.selectMdDeviceCtrl();
			MdDeviceCtrl ctrlInfo = ctrlList.stream().filter(t -> t.getCfgCode().equals(schedulerId)).findFirst().orElse(new MdDeviceCtrl());

			this.eqCtrlCode = ctrlInfo.getCfgCode();
			this.eqCtrlDesc = ctrlInfo.getCfgName();
			this.eqCtrlDataType = MelsecSeries.CommunicateFormat.getCode(ctrlInfo.getDataType());
			this.hostIp = ctrlInfo.getCtrlIp();
			this.hostPort = ctrlInfo.getCtrlPort();
			this.mdDeviceCtrlMelsec = this.equipmentMapper.selectMdDeviceCtrlMelsec(this.eqCtrlCode);
			this.syncSocketClient = new SyncSocketClient(this.eqCtrlCode, this.hostIp, this.hostPort, this);

			log.info(LogUtils.basic("initScheduler", "[SCHEDULER.INIT:{},{}]", this.eqCtrlCode, this.eqCtrlDesc));
		} catch (Exception e) {
			log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
		}
	}

	@Override
	public void shutdownScheduler() {
		try {
			this.syncSocketClient.disconnect();
			log.info(LogUtils.basic("shutdownScheduler", "[SCHEDULER.SHUTDOWN:{},{}]", this.eqCtrlCode, this.eqCtrlDesc));
		} catch (Exception e) {
			log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
		}
	}

	@Override
	public Runnable getTask() {
		return this::taskService;
	}
	// endregion

	private void taskService() {
		try {
			this.syncSocketClient.connect(10000);
			statusReadProc();
			motionWriteProc();
		} catch (Exception e) {
			// taskService에서는 연결 실패 로그를 기본 레벨(INFO)로 남겨서 불필요한 에러 스택을 줄입니다.
			log.info(LogUtils.basic("taskService", "[CONNECTION_FAIL] {}:{} 연결 실패. PLC 서버가 실행중인지 확인하세요. ({})", this.hostIp, this.hostPort, e.getMessage()));
		}
	}

	/**
	 * 디바이스 상태 조회 프로세스
	 */
	public void statusReadProc() throws Exception {
		try {


			for (MdDeviceCtrlMelsec readConfig : mdDeviceCtrlMelsec) {
				String readDeviceCode = readConfig.getMemorytype();
				int address = readConfig.getReadAddr();
				int count = readConfig.getReadCount();
				MelsecSeries.CommunicateFormat dataType = readConfig.getDataType().equals("1") ? MelsecSeries.CommunicateFormat.ASCII : MelsecSeries.CommunicateFormat.BINARY;

				// Send Melsec Protocol (read)
				MelsecQ3E melsecQ3E = new MelsecQ3E(dataType);
				String sendMessage = melsecQ3E.readWord(readDeviceCode, address, count);
				String recvStr = this.syncSocketClient.sendMsgForMelsecQ3e(sendMessage.getBytes(), 10000, 4096, dataType);

				// Analysis Response.Message.frame
				Map<Integer, MelsecData> readData = melsecQ3E.conversionResponseMessageFrame(recvStr.substring(22), address, count);

				// ============================ 로그 확인 구간 ============================
				log.info("========== PLC RAW DATA 확인 ({}) ==========", this.eqCtrlCode);
				if (readData != null && !readData.isEmpty()) {
					// 전체 데이터를 한 줄로 요약해서 보고 싶을 때
					log.info(LogUtils.basic("statusReadProc", "[RAW_DATA_SUMMARY] {}", readData.toString()));

					// 각 주소별 데이터를 상세하게 보고 싶을 때
					readData.forEach((addr, data) -> {
						log.info(LogUtils.basic("statusReadProc", "[RAW_DATA_DETAIL] 주소: {} | 10진수: {} | 16진수: {}", addr, data.getDecimal(), data.getHexString()));
					});
				} else {
					log.warn(LogUtils.basic("statusReadProc", "[RAW_DATA_EMPTY] PLC로부터 읽어온 데이터가 없습니다. ({})", this.eqCtrlCode));
				}
				log.info("==========================================================");
				// =====================================================================

				// Process Relay Service
				plcRelayService.getPlcService(this.eqCtrlCode).readProcess(this.syncSocketClient, address, readData);
			}

		} catch (Exception e) {
			log.error(LogUtils.errorLog(e, "statusReadProc() 실행 중 오류 발생: {}", e.getMessage()));
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 디바이스 동작 제어 프로세스
	 */
	public void motionWriteProc() throws Exception {
		try {
			//TODO: equipment motion order 처리
		} catch (Exception e) {
			log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
			throw new Exception(e.getMessage());
		}
	}

	// region [+] ClientListener (Socket Client Event)
	@Override
	public void onConnected(String hostId, String hostIp, int hostPort) {
		plcRelayService.getPlcService(this.eqCtrlCode).init(this.eqCtrlCode, this.eqCtrlDataType);
		log.info(LogUtils.basic("onConnected", "[ON.CONNECTED][{},{},{},{}]", this.eqCtrlCode, this.eqCtrlDesc, this.hostIp, this.hostPort));
	}

	@Override
	public void onDisconnected(Exception e) {
		log.info(LogUtils.basic("onDisconnected", "[ON.DISCONNECTED][{},{},{},{}]", this.eqCtrlCode, this.eqCtrlDesc, this.hostIp, this.hostPort));
	}

	@Override
	public void onMessageReceived(String s) {
	}

	@Override
	public void onError(Exception e) {
		log.error(LogUtils.errorLog(e, "Socket Client Error: {}", e.getMessage()));
	}
	// endregion

	// region [+] EqControlEventListener Event
	@Override
	@EventListener
	@Async
	public void handleEqInjectionEvent(EqControlEvent event) {
		try {
			String eventId = event.getEventId();
			switch (eventId) {
				case "SETORDER": {
					//TODO: equipment motion order 처리
				}
				break;

				case "TEST": {
					int writeStartAddr = 11006;
					int[] writeData;
					if (writeTest) {
						writeData = new int[]{1, 0, 0, 0, 0}; // 자동, 대기, 상태코드, 화물작번, 목적지
					} else {
						writeData = new int[]{1, 0, 0, 10, 1}; // 자동, 동작, 상태코드, 화물작번, 목적지
					}
					writeTest = !writeTest;
					plcRelayService.getPlcService(this.eqCtrlCode).writeProcess(this.syncSocketClient, "D", writeStartAddr, writeData);
				}
				break;

				default: {
					log.info(LogUtils.basic("handleEqInjectionEvent", "[UNKNOWN EVENT][EqControlEvent:{}][{}]", eventId, event.getParameter()));
				}
			}
		} catch (Exception e) {
			log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
		}
	}
	//endregion
}