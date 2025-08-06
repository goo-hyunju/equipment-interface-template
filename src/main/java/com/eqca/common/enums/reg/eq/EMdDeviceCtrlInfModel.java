package com.eqca.common.enums.reg.eq;

import lombok.Getter;

/**
 * 제어컨트롤러 모델
 */
@Getter
public enum EMdDeviceCtrlInfModel {

    //code("value")

    /** 미쓰비시 PLC */
    CLIENT("C"),

    /** 지멘스 PLC */
    SERVER("S"),
    /* NONE */
    NONE("0000"),

    ;

    /** 값 */
    private final String value;
    EMdDeviceCtrlInfModel(String value){
        this.value = value;
    }

    public static EMdDeviceCtrlInfModel getCode(String value){
        for(EMdDeviceCtrlInfModel oCode : values()){
            if(oCode.getValue().equals(value)){
                return oCode;
            }
        }

        return EMdDeviceCtrlInfModel.NONE;
    }
}
