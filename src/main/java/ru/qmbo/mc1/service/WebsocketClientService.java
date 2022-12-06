package ru.qmbo.mc1.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.qmbo.mc1.model.Message;

/**
 * WebsocketClientService
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Service
@Log4j2
public class WebsocketClientService {

    private final SessionHandlerService sessionHandlerNoResponse;

    /**
     * Instantiates a new Websocket client service.
     *
     * @param sessionHandlerNoResponse the session handler no response
     */
    public WebsocketClientService(SessionHandlerService sessionHandlerNoResponse) {
        this.sessionHandlerNoResponse = sessionHandlerNoResponse;
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(Message message) {
        this.sessionHandlerNoResponse.subscribeAndSend(message);
    }
}
