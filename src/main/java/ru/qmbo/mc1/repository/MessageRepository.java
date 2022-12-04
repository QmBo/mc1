package ru.qmbo.mc1.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.qmbo.mc1.model.Message;

/**
 * MessageRepository
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
}
