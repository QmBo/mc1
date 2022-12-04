package ru.qmbo.mc1.service;

import org.springframework.stereotype.Service;
import ru.qmbo.mc1.model.Message;
import ru.qmbo.mc1.repository.MessageRepository;

/**
 * MessageService
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Service
public class MessageService {
    private final MessageRepository repository;

    /**
     * Instantiates a new Message service.
     *
     * @param repository the repository
     */
    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Save message.
     *
     * @param message the message
     */
    public void saveMessage(Message message) {
        this.repository.save(message);
    }
}
