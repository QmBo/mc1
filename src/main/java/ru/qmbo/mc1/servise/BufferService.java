package ru.qmbo.mc1.servise;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;
import ru.qmbo.mc1.model.Message;

/**
 * BufferService
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Service
@Data
@Accessors(chain = true)
public class BufferService {
    private Message message;
}
