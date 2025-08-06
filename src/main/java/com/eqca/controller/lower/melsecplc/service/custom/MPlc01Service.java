package com.eqca.controller.lower.melsecplc.service.custom;

import com.altiall.plc.mitsubishi.MelsecData;
import com.altiall.plc.mitsubishi.format.MelsecSeries;
import com.altiall.plc.mitsubishi.protocol.qmelsec.q3e.MelsecQ3E;
import com.altiall.socket.java.SyncSocketClient;
import com.eqca.common.utils.LogUtils;
import com.eqca.controller.base.event.EqInjectionEventPublish;
import com.eqca.controller.lower.melsecplc.service.definition.IMplcManage;
import com.eqca.repository.dto.entity.MdDevice;
import com.eqca.repository.mapper.a.equipment.EquipmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MPlc01Service implements IMplcManage {

    private final EquipmentMapper equipmentMapper;
    private final EqInjectionEventPublish eqInjectionEventPublish;

    private String cfgCode;
    private MelsecSeries.CommunicateFormat dataType;

    List<MdDevice> mdDeviceList;

    @Override
    public void init(String cfgCode, MelsecSeries.CommunicateFormat dataType) {
        this.cfgCode = cfgCode;
        this.dataType = dataType;

        this.equipmentMapper.selectMdDeviceCtrl();
        this.mdDeviceList = this.equipmentMapper.selectMdDeviceByCfgCode("mplc1");
    }

    @Override
    public void clean(String cfgCode, MelsecSeries.CommunicateFormat dataType) {

    }

    @Override
    public void readProcess(Object socketObject, int readAddress, Map<Integer, MelsecData> readData) throws Exception {
        try{

            // Set Track Status (ref 'Melsec Plc Interface.Address Map)
            if(readAddress == 10001) {
                setTrackDeviceInfo(readAddress, readData);
            }

        }catch (Exception e){
            log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Set Track Device Information
     */
    private void setTrackDeviceInfo(int readAddress, Map<Integer, MelsecData> readData) throws Exception {
        try{
            log.info(LogUtils.basic("setTrackDeviceInfo", 
                "[TRACK_PROCESS_START] StartAddr:{}, DataSize:{}", readAddress, readData.size()));

            List<MdDevice> newMdDeviceList = new ArrayList<>();

            // Set Device Status
            int trackNo = 1;
            int currentAddr = readAddress;
            while (readData.containsKey(currentAddr)) {
                String ctrlStatus = String.format("%04d", readData.get(currentAddr).getDecimal());
                String motionStatus = String.format("%04d", readData.get(currentAddr + 1).getDecimal());
                String statusCode = String.format("%04d", readData.get(currentAddr + 2).getDecimal());
                String workId = readData.get(currentAddr + 3).getHexString();
                int destCode = readData.get(currentAddr + 4).getDecimal();
                String deviceCode = "11-" + String.format("%03d", trackNo);

                // Track별 상세 데이터 로그
                log.info(LogUtils.basic("setTrackDeviceInfo", 
                    "[TRACK_DATA][{}] Device:{}, Ctrl:{}, Motion:{}, Status:{}, Work:{}, Dest:{}", 
                    this.cfgCode, deviceCode, ctrlStatus, motionStatus, statusCode, workId, destCode));
                // 각 주소별 Raw 데이터 로그 (DEBUG)
                log.debug(LogUtils.basic("setTrackDeviceInfo", 
                    "[TRACK_RAW_DATA][{}] Addr{}:{}, Addr{}:{}, Addr{}:{}, Addr{}:{}, Addr{}:{}", 
                    this.cfgCode,
                    currentAddr, readData.get(currentAddr).getDecimal(),
                    currentAddr + 1, readData.get(currentAddr + 1).getDecimal(),
                    currentAddr + 2, readData.get(currentAddr + 2).getDecimal(),
                    currentAddr + 3, readData.get(currentAddr + 3).getHexString(),
                    currentAddr + 4, readData.get(currentAddr + 4).getDecimal()));

                newMdDeviceList.add(
                        MdDevice.builder()
                                .deviceCode(deviceCode).ctrlStatus(ctrlStatus).motionStatus(motionStatus)
                                .statusCode(statusCode).workId(workId).destCode(destCode).build()
                );

                currentAddr += 5;
                trackNo++;
            }

            log.info(LogUtils.basic("setTrackDeviceInfo", 
                "[TRACK_PROCESS_COMPLETE] TotalTracks:{}, ProcessedAddresses:{}-{}", 
                newMdDeviceList.size(), readAddress, currentAddr - 5));

            // Get Change Status Device List
            List<MdDevice> changeInfos = newMdDeviceList.stream().filter(cur ->
                    this.mdDeviceList.stream().noneMatch(old ->
                            old.getDeviceCode().equals(cur.getDeviceCode())
                            && Objects.equals(old.getCtrlStatus(), cur.getCtrlStatus())
                            && Objects.equals(old.getMotionStatus(), cur.getMotionStatus())
                            && Objects.equals(old.getStatusCode(), cur.getStatusCode())
                            && Objects.equals(old.getWorkId(), cur.getWorkId())
                            && Objects.equals(old.getDestCode(), cur.getDestCode())
                    )
            ).toList();

            // 변경 감지 결과 로그
            log.info(LogUtils.basic("setTrackDeviceInfo", 
                "[CHANGE_DETECTION] TotalDevices:{}, ChangedDevices:{}", 
                newMdDeviceList.size(), changeInfos.size()));

            if(changeInfos.size() <= 0) {
                log.debug(LogUtils.basic("setTrackDeviceInfo", "[NO_CHANGES] All devices status unchanged"));
                return;
            }

            // 변경된 디바이스 상세 로그
            changeInfos.forEach(changed -> {
                MdDevice oldDevice = this.mdDeviceList.stream()
                    .filter(old -> old.getDeviceCode().equals(changed.getDeviceCode()))
                    .findFirst().orElse(null);
                if (oldDevice != null) {
                    log.info(LogUtils.basic("setTrackDeviceInfo", 
                        "[DEVICE_CHANGED][{}] Old[Ctrl:{},Motion:{},Status:{},Work:{},Dest:{}] -> New[Ctrl:{},Motion:{},Status:{},Work:{},Dest:{}]",
                        changed.getDeviceCode(),
                        oldDevice.getCtrlStatus(), oldDevice.getMotionStatus(), oldDevice.getStatusCode(), oldDevice.getWorkId(), oldDevice.getDestCode(),
                        changed.getCtrlStatus(), changed.getMotionStatus(), changed.getStatusCode(), changed.getWorkId(), changed.getDestCode()));
                } else {
                    log.info(LogUtils.basic("setTrackDeviceInfo", 
                        "[DEVICE_NEW][{}] Ctrl:{}, Motion:{}, Status:{}, Work:{}, Dest:{}", 
                        changed.getDeviceCode(), changed.getCtrlStatus(), changed.getMotionStatus(), 
                        changed.getStatusCode(), changed.getWorkId(), changed.getDestCode()));
                }
            });

            // Status Change Event Publish
            this.mdDeviceList.stream().filter(old ->
                    changeInfos.stream().anyMatch(changed -> old.getDeviceCode().equals(changed.getDeviceCode()))
            ).forEach(h -> {
                MdDevice changeInfo = changeInfos.stream().filter(changed -> h.getDeviceCode().equals(changed.getDeviceCode())).findFirst().orElse(null);
                if (changeInfo != null) {
                    h.setCtrlStatus(changeInfo.getCtrlStatus());
                    h.setMotionStatus(changeInfo.getMotionStatus());
                    h.setStatusCode(changeInfo.getStatusCode());
                    h.setWorkId(changeInfo.getWorkId());
                    h.setDestCode(changeInfo.getDestCode());
                    // 이벤트 발행 로그
                    log.info(LogUtils.basic("setTrackDeviceInfo", 
                        "[EVENT_PUBLISH][{}] EqCode:{}, DeviceCode:{}, CfgCode:{}, EventType:LOADING", 
                        h.getDeviceCode(), h.getEqCode(), h.getDeviceCode(), this.cfgCode));
                    eqInjectionEventPublish.publish(h.getEqCode(), h.getDeviceCode(), this.cfgCode, "LOADING", h);
                }
            });

            log.info(LogUtils.basic("setTrackDeviceInfo", "[TRACK_UPDATE_COMPLETE] Updated {} devices", changeInfos.size()));
        } catch (Exception e){
            log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void writeProcess(Object socketObject, String deviceCode, int writeStartAddr, int[] writeData) throws Exception {
        //지정의 어드레스에 지정의 데이터쓰기
        try{

            String sendMessage = new MelsecQ3E(this.dataType).writeWord(deviceCode, writeStartAddr, writeData);
            ((SyncSocketClient) socketObject).sendMsgForMelsecQ3e(sendMessage.getBytes(), 1000, 4096, dataType);

        }catch (Exception e){
            log.error(LogUtils.errorLog(e, "{}", e.getMessage()));
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void featureProcess(Object socketObject) throws Exception {

    }
}
