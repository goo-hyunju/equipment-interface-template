package com.eqca.common.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO : 위치 config/websocket or interceptor
 */
@Slf4j
@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    
	@Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        
		/*
    	StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);        
		StompCommand stompCommand = stompHeaderAccessor.getCommand();
        Map<String, Object> sessionAttributes = stompHeaderAccessor.getSessionAttributes();
        log.debug("STOMP attributes={}", sessionAttributes.toString());
        log.debug("STOMP headerAccessor ={}", stompHeaderAccessor.toString());
        switch (stompCommand) {
            case CONNECT:
                log.debug("STOMP Connect sessionId => {}", stompHeaderAccessor.getSessionId());
                break;
            case SUBSCRIBE:
                log.debug("STOMP Subscribe sessionId => {}", stompHeaderAccessor.getSessionId());
                break;
            case DISCONNECT:
                log.debug("STOMP Disconnected sessionId => {}", stompHeaderAccessor.getSessionId());
                break;
            default:
                break;
        }
        */
		
        return message;
    }

    @Override
    public void postSend(@NonNull Message<?> message, @NonNull MessageChannel channel, boolean sent) {

    }
}
