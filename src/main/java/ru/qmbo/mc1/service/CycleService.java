package ru.qmbo.mc1.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.qmbo.mc1.model.Message;

import java.util.Date;

import static java.lang.String.*;

/**
 * CycleService
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Service
@Log4j2
public class CycleService {
    public static final String IS_WORK_NOW = "Cycle is work now.";
    public static final String START_CYCLE_AT = "Start cycle at: %s";
    public static final String CYCLE_IS_STOPPED = "Cycle is stopped.Init %s cycles. Lead time: %s s.";
    public static final String UNTIL_I_STARTED = "Until I started.";
    public static final String LAST_CYCLE = "Last Cycle. Init %s cycles. Lead time: %s s.";
    private Date startDate;
    private int cycles;
    private final WebsocketClientService clientService;
    private boolean doCycle = false;
    private final MessageService messageService;
    private final int workTime;
    private Date endTime;

    /**
     * Instantiates a new Cycle service.
     *
     * @param clientService  the client service
     * @param messageService the message service
     * @param workTime       the work time
     */
    public CycleService(WebsocketClientService clientService, MessageService messageService,
                        @Value("${service.work.time}") String workTime) {
        this.clientService = clientService;
        this.messageService = messageService;
        this.workTime = Integer.parseInt(workTime);
    }

    /**
     * Start cycle.
     *
     * @return the string
     */
    public String start() {
        String startMessage = IS_WORK_NOW;
        if (!this.doCycle) {
            this.cycles = 0;
            this.doCycle = true;
            this.startDate = new Date(System.currentTimeMillis());
            startMessage = format(START_CYCLE_AT, this.startDate);
            log.info(startMessage);
            Message message = new Message().setSessionId(++cycles).setMc1Timestamp(this.startDate);
            this.sendMessage(message);
        }
        return startMessage;
    }

    /**
     * Stop cycle.
     *
     * @return the string
     */
    public String stop() {
        String message = UNTIL_I_STARTED;
        long lead;
        if (this.doCycle) {
            this.endTime = new Date(System.currentTimeMillis());
            this.doCycle = false;
            lead = (this.endTime.getTime() - this.startDate.getTime()) / 1000;
            message = format(CYCLE_IS_STOPPED, this.cycles, lead);
            this.connectionStop();
        } else if (this.startDate != null) {
            lead = (this.endTime.getTime() - this.startDate.getTime()) / 1000;
            message = format(LAST_CYCLE, this.cycles, lead);
        }
        log.info(message);
        return message;
    }

    /**
     * Message back.
     *
     * @param message the message
     */
    public void messageBack(Message message) {
        if (this.doCycle) {
            this.endTime = new Date(System.currentTimeMillis());
            message.setEndTimestamp(this.endTime);
            log.debug("Message is back: {}", message);
            this.messageService.saveMessage(message);
        }
        if ((endTime.getTime() - this.startDate.getTime()) / 1000 < this.workTime && this.doCycle) {
            this.sendMessage(new Message().setMc1Timestamp(this.endTime).setSessionId(++cycles));
        } else {
            this.stop();
        }
    }

    private void connectionStop() {
        this.clientService.connectionStop();
    }

    private void sendMessage(Message message) {
        this.clientService.sendMessage(message);
    }
}
