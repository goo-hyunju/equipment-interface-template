package com.eqca.controller.base.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EqInjectionEventPublish {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(String eqCode, String deviceCode, String eqCtrlCode, String eventId, Object param){
        log.info("[EVENT.PUBLISH][eqCode:{}][deviceCode:{}][eqCtrlCode:{}][eventId:{}][param:{}]", eqCode, deviceCode, eqCtrlCode, eventId, param);
        eventPublisher.publishEvent(new EqInjectionEvent(eqCode, deviceCode, eqCtrlCode, eventId, param));
    }
}
