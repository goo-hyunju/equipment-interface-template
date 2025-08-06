package com.eqca.controller.base.event;

import lombok.Getter;

@Getter
public class EqInjectionEvent {

    private String eqCode;
    private String devicCode;
    private String eqCtrlCode;
    private final String eventId;
    private final Object parameter;

    public EqInjectionEvent(String eqCode, String devicCode, String eqCtrlCode, String eventId, Object parameter) {
        this.eqCode = eqCode;
        this.devicCode = devicCode;
        this.eqCtrlCode = eqCtrlCode;
        this.eventId = eventId;
        this.parameter = parameter;
    }

    // todo async event 기능 검증

    /* "EventId" Definition
     * Device To Equipment(ASRS, BOXCV, SHUTTLE.. 등 설비) eventId
     *  LOADING / UNLOADING :  로딩 / 언로딩보고
     *  REQLOADING 로딩 요청
     *  RESULT : 지시에 대한 실적 보고
     */

}
