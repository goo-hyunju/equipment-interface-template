package com.eqca.controller.lower.melsecplc.service.custom;

import com.eqca.common.utils.LogUtils;
import com.eqca.controller.lower.melsecplc.service.definition.IMplcManage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlcRelayService {
    
    private final MPlc01Service mPlc01Service;
    private final MPlc02Service mPlc02Service;

    public IMplcManage getPlcService(String cfgCode) {
        
        log.info(LogUtils.basic("getPlcService", 
            "[PLC_SERVICE_ROUTING] 요청된 cfgCode: {}", cfgCode));
        
        IMplcManage service = switch (cfgCode) {
            case "mplc1" -> {
                log.info(LogUtils.basic("getPlcService", 
                    "[PLC_SERVICE_MAPPED] mplc1 -> MPlc01Service"));
                yield mPlc01Service;
            }
            case "mplc2" -> {
                log.info(LogUtils.basic("getPlcService", 
                    "[PLC_SERVICE_MAPPED] mplc2 -> MPlc02Service"));
                yield mPlc02Service;
            }
            // 추가 매핑 - 현재 DB에 있는 다른 cfg_code들
            case "test_pick", "test_route", "real_plc" -> {
                log.info(LogUtils.basic("getPlcService", 
                    "[PLC_SERVICE_MAPPED] {} -> MPlc01Service (기본 매핑)", cfgCode));
                yield mPlc01Service; // 기본적으로 MPlc01Service 사용
            }
            default -> {
                log.warn(LogUtils.basic("getPlcService", 
                    "[PLC_SERVICE_UNMAPPED] 매핑되지 않은 cfgCode: {} (사용 가능한 코드: mplc1, mplc2, test_pick, test_route, real_plc)", 
                    cfgCode));
                yield null;
            }
        };
        
        if (service == null) {
            log.error(LogUtils.basic("getPlcService", 
                "[PLC_SERVICE_ERROR] cfgCode '{}'에 대한 서비스를 찾을 수 없습니다", cfgCode));
        } else {
            log.info(LogUtils.basic("getPlcService", 
                "[PLC_SERVICE_SUCCESS] cfgCode '{}' -> 서비스: {}", 
                cfgCode, service.getClass().getSimpleName()));
        }
        
        return service;
    }
}