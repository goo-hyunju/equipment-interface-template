package com.eqca.repository.mapper.a.equipment;

import com.eqca.repository.dto.entity.MdDevice;
import com.eqca.repository.dto.entity.MdDeviceCtrl;
import com.eqca.repository.dto.entity.MdDeviceCtrlMelsec;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentMapper {

    /**
     * 디바이스 제어기. MELSEC.PLC의 수집 데이터 영역 조회  
     */
    List<MdDeviceCtrlMelsec> selectMdDeviceCtrlMelsec( String cfgCode );
    
    /**
     * 디바이스의 제어기 정보 조회
     */
    List<MdDeviceCtrl> selectMdDeviceCtrl();

    /**
     * 디바이스 정보 조회 (for 디바이스 제어기)
     */
    List<MdDevice> selectMdDeviceByCfgCode(String cfgCode);

}
