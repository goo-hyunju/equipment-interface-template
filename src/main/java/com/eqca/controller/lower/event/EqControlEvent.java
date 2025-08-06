package com.eqca.controller.lower.event;

import lombok.Getter;

@Getter
public class EqControlEvent {
    private final String eventId;
    private final Object parameter;

    public EqControlEvent(String eventId, Object parameter) {
        this.eventId = eventId;
        this.parameter = parameter;
    }

    /* EventId Definition
     * Equipment(ASRS, BOXCV, SHUTTLE 등 설비) To Device eventId
     *  SETORDER 제어 지시
     *  상태 요청... (todo 상태 요청을.. event로 처리하는게 맞는지 고민중)
     */
}
