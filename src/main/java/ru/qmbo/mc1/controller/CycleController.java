package ru.qmbo.mc1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.qmbo.mc1.servise.CycleService;

/**
 * CycleController
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Controller
public class CycleController {
    private final CycleService service;

    /**
     * Instantiates a new Cycle controller.
     *
     * @param service the service
     */
    public CycleController(CycleService service) {
        this.service = service;
    }

    /**
     * Start cycle.
     *
     * @return the string
     */
    @GetMapping("/start")
    public @ResponseBody String startCycle() {
        return this.service.start();
    }

    /**
     * Stop cycle.
     *
     * @return the string
     */
    @GetMapping("/stop")
    public @ResponseBody String stopCycle() {
        return this.service.stop();
    }
}
