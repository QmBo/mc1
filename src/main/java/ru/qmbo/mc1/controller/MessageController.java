package ru.qmbo.mc1.controller;

import org.springframework.web.bind.annotation.*;
import ru.qmbo.mc1.model.Message;
import ru.qmbo.mc1.servise.CycleService;

/**
 * MessageController
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@RestController
@RequestMapping("/messages")
public class MessageController {
    private final CycleService service;

    /**
     * Instantiates a new Message controller.
     *
     * @param service the service
     */
    public MessageController(CycleService service) {
        this.service = service;
    }


    /**
     * Back message.
     *
     * @param message the message
     */
    @PostMapping
    public void backMessage(@RequestBody Message message) {
        this.service.messageBack(message);
    }
}
