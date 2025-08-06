package com.eqca.common.enums.reg.eq;

import lombok.Getter;

/**
 * 제어컨트롤러 모델
 */
@Getter
public enum EMdDeviceCtrlModel {

    //code("value")

    /** 미쓰비시 PLC */
    M_PLC("1001"),

    /** 지멘스 PLC */
    S_PLC("1002"),

    /** LS 산전 PLC */
    L_PLC("1003"),

    /* NORMAL */
    NORMAL("0001"),

    /* NONE */
    NONE("0000"),

    ;

    /** 값 */
    private final String value;
    EMdDeviceCtrlModel(String value){
        this.value = value;
    }

    public static EMdDeviceCtrlModel getCode(String value){
        for(EMdDeviceCtrlModel oCode : values()){
            if(oCode.getValue().equals(value)){
                return oCode;
            }
        }

        return EMdDeviceCtrlModel.NONE;
    }
}
