package com.eqca.common.enums;

import lombok.Getter;

/**
 * 사용여부(Y/N)에 대한 ENUM TYPE
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Getter
public enum EUseYn {
    /** 사용, Y */
    USE_Y("Y"),

    /** 사용안함, "N" */
    USE_N("N")
    ;

    /** 값 */
    private final String value;
    EUseYn(String value){
        this.value = value;
    }

    public static EUseYn getCfgModel(String code){
        for(EUseYn oCode : values()){
            if(oCode.getValue().equals(code)){
                return oCode;
            }
        }

        return EUseYn.USE_N;
    }
}
