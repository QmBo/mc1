package ru.qmbo.mc1.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import ru.qmbo.mc1.model.Message;

import java.lang.reflect.Type;
import java.util.concurrent.Executor;


/**
 * SessionHandler
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Component
@Log4j2
public class SessionHandlerService extends StompSessionHandlerAdapter {

    /**
     * The constant WS_ENDPOINT_PREFIX.
     */
    public static final String WS_ENDPOINT_PREFIX = "/app";
    /**
     * The constant WS_TOPIC_DESTINATION_PREFIX.
     */
    public static final String WS_TOPIC_DESTINATION_PREFIX = "/topic";
    /**
     * The constant SAMPLE_ENDPOINT_WITHOUT_RESPONSE_MESSAGE_MAPPING.
     */
    public static final String SAMPLE_ENDPOINT_WITHOUT_RESPONSE_MESSAGE_MAPPING = "/sampleEndpointWithoutResponse";
    /**
     * The constant WS_TOPIC_NO_RESPONSE.
     */
    public static final String WS_TOPIC_NO_RESPONSE = WS_TOPIC_DESTINATION_PREFIX + "/messagesNoResponse";
    private final BufferService buffer;
    private StompSession session;

    /**
     * Instantiates a new Session handler.
     *
     * @param buffer the buffer
     */
    public SessionHandlerService(BufferService buffer) {
        this.buffer = buffer;
    }


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        try {
            Runnable work = () -> {
                while (!this.buffer.isInterrupt()) {
                    Message message = this.buffer.getMessage();
                    if (message != null) {
                        this.subscribeAndSend(message);
                        this.buffer.setMessage(null);
                    } else {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            log.warn("Interrupted", e);
                        }
                    }
                }
            };
            Executor executor = (runnable) -> new Thread(runnable).start();
            executor.execute(work);
            this.session = session;
        } catch (Exception e) {
            log.error("Error while sending data");
        }

    }

    /**
     * Subscribe and send.
     *
     * @param requestMessage the request message
     */
    public void subscribeAndSend(Message requestMessage) {
        this.session.subscribe(WS_TOPIC_NO_RESPONSE, this);
        this.session.send(WS_ENDPOINT_PREFIX + SAMPLE_ENDPOINT_WITHOUT_RESPONSE_MESSAGE_MAPPING, requestMessage);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Void.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                byte[] payload, Throwable exception) {
        super.handleException(session, command, headers, payload, exception);
    }
}
