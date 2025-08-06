package com.eqca.common.enums.reg.eq;

import lombok.Getter;

/**
 * 제어컨트롤러 모델
 */
@Getter
public enum EMdDeviceCtrlInfType {

    //code("value")

    /** 기타 */
    ETC("0"),

    /** SOCKET */
    SOCKET("1"),

    /** SERIAL */
    SERIAL("2"),

    /** NONE */
    NONE("9"),

    ;

    /** 값 */
    private final String value;
    EMdDeviceCtrlInfType(String value){
        this.value = value;
    }

    public static EMdDeviceCtrlInfType getCode(String value){
        for(EMdDeviceCtrlInfType oCode : values()){
            if(oCode.getValue().equals(value)){
                return oCode;
            }
        }

        return EMdDeviceCtrlInfType.NONE;
    }
}
