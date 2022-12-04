package ru.qmbo.mc1.servise;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.qmbo.mc1.config.SessionHandler;
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

    private final WebSocketStompClient client;
    private final BufferService buffer;
    private final SessionHandler sessionHandlerNoResponse;
    private final String urlEndpointNoResponse;

    /**
     * Instantiates a new Websocket client service.
     *
     * @param client                   the client
     * @param buffer                   the buffer
     * @param sessionHandlerNoResponse the session handler no response
     * @param urlEndpointNoResponse    the url endpoint no response
     */
    public WebsocketClientService(
            WebSocketStompClient client, BufferService buffer, SessionHandler sessionHandlerNoResponse,
            @Value("${websocket.server.url-no-response}") String urlEndpointNoResponse) {
        this.client = client;
        this.buffer = buffer;
        this.sessionHandlerNoResponse = sessionHandlerNoResponse;
        this.urlEndpointNoResponse = urlEndpointNoResponse;
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(Message message) {
        this.buffer.setMessage(message);
        client.connect(urlEndpointNoResponse, sessionHandlerNoResponse);
    }
}
