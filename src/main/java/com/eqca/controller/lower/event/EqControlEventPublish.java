package com.eqca.controller.lower.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EqControlEventPublish {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(String eventId, Object param){
        log.info("[EVENT.PUBLISH][eventId:{}][param:{}]", eventId, param);

        eventPublisher.publishEvent(new EqControlEvent(eventId, param));
    }
}
