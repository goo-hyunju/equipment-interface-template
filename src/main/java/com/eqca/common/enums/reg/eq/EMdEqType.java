package com.eqca.common.enums.reg.eq;

import lombok.Getter;

/**
 * 설비 타입 구분 값
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Getter
public enum EMdEqType {

    //code("value")

    /** 피킹설비 또는 플레이스설비 */
    PICK_A_PLACE("PICK"),
    
    /** 라우트, 이동설비 */
    ROUTE("ROUTE"),
    
    /** 소트, 분류설비 */
    SORT("SORT"),
    
    /** 스토리지, 보관설비 */
    STORAGE("STORAGE"),

    /** unknown, 알 수 없는 설비 */
    NONE("NONE"),
    ;

    /** 값 */
    private final String value;
    EMdEqType(String value){
        this.value = value;
    }

    public static EMdEqType getCode(String value){
        for(EMdEqType oCode : values()){
            if(oCode.getValue().equals(value)){
                return oCode;
            }
        }

        return EMdEqType.NONE;
    }
}
