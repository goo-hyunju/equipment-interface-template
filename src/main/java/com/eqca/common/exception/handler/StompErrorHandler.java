package com.eqca.common.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

/**
 * Websocket STOMP 통신 Error Handler
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {


    @Override
    public Message<byte[]> handleClientMessageProcessingError(
            @Nullable Message<byte[]> clientMessage,
            @NonNull Throwable ex) {

        Throwable firstEx = ex.getCause();
        if(firstEx instanceof RuntimeException){
            Throwable secondEx = firstEx.getCause();
            String message = secondEx.getMessage();

            if (message.equals("UNAUTHORIZED"))
                return errorMessage("유효하지 않은 권한입니다.");
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }


//    private Message<byte[]> handleUnauthorizedException(Message<byte[]> clientMessage, Throwable ex)
//    {
//
//        ApiError apiError = new ApiError(
//                ex.getMessage());
//
//        return prepareErrorMessage(clientMessage, apiError, String.valueOf(ErrorCodeConstants.UNAUTHORIZED_STRING));
//    }
//
//    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, ApiError apiError, String errorCode)
//    {
//        String job = apiError.getErrorMessage();
//
//        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
//
//        accessor.setMessage(errorCode);
//        accessor.setLeaveMutable(true);
//
//        return MessageBuilder.createMessage(job.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
//    }

    private Message<byte[]> errorMessage(String errorMessage) {

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(errorMessage);
        accessor.setLeaveMutable(true);

        log.debug("Accessor ={}", accessor.getMessage());

        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }

}
