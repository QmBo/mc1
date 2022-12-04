package ru.qmbo.mc1.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import ru.qmbo.mc1.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * WebSocketClientConfig
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Configuration
public class WebSocketClientConfig {

    /**
     * Web socket client web socket client.
     *
     * @return the web socket client
     */
    @Bean
    public WebSocketClient webSocketClient()
    {
        List<Transport> transports = new ArrayList<Transport>(1);
        transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
        return new SockJsClient(transports);
    }

    /**
     * Web socket stomp client.
     *
     * @param webSocketClient the web socket client
     * @return the web socket stomp client
     */
    @Bean
    public WebSocketStompClient webSocketStompClient(WebSocketClient webSocketClient) {
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        return  stompClient;
    }
}
