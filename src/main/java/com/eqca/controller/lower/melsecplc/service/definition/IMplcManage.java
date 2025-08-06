package com.eqca.controller.lower.melsecplc.service.definition;

import com.altiall.plc.mitsubishi.MelsecData;
import com.altiall.plc.mitsubishi.format.MelsecSeries;

import java.util.Map;

public interface IMplcManage {

    void init(String cfgCode, MelsecSeries.CommunicateFormat dataType);

    void clean(String cfgCode, MelsecSeries.CommunicateFormat dataType);

    /**
     * PLC.READ.Data Processing
     */
    void readProcess(Object socketObject, int readAddress, Map<Integer, MelsecData> readData) throws Exception;

    /**
     * PLC.WRITE.Data Processing
     */
    void writeProcess(Object socketObject, String deviceCode, int writeStartAddr, int[] writeData) throws Exception;

    /**
     * PLC.고유기능처리
     */
    void featureProcess(Object socketObject) throws Exception;

    /*

    //PLC.고유기능처리
    void extractionProcess(Object socketObject, DeviceOrder deviceOrder) throws Exception;

    String getWorkId(String eqCode, String deviceCode);

    List<MdDevice> getDeviceInfos();

    List<PropPlcPathInfo> getDevicePathInfos();

    List<PropPltzInfo> getPltzInfo();*/
}
