package com.eqca.controller.lower.melsecplc.service.custom;

import com.altiall.plc.mitsubishi.MelsecData;
import com.altiall.plc.mitsubishi.format.MelsecSeries;
import com.altiall.plc.mitsubishi.protocol.qmelsec.q3e.MelsecQ3E;
import com.altiall.socket.java.SyncSocketClient;
import com.eqca.controller.lower.melsecplc.service.definition.IMplcManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class MPlc02Service implements IMplcManage {

    private String cfgCode;
    private MelsecSeries.CommunicateFormat dataType;

    @Override
    public void init(String cfgCode, MelsecSeries.CommunicateFormat dataType) {
        this.cfgCode = cfgCode;
        this.dataType = dataType;
    }

    @Override
    public void clean(String cfgCode, MelsecSeries.CommunicateFormat dataType) {

    }

    @Override
    public void readProcess(Object socketObject, int readAddress, Map<Integer, MelsecData> readData) throws Exception {

    }

    @Override
    public void writeProcess(Object socketObject, String deviceCode, int writeStartAddr, int[] writeData) throws Exception {

    }

    @Override
    public void featureProcess(Object socketObject) throws Exception {

    }
}
