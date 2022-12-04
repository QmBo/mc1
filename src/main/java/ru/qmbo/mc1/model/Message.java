package ru.qmbo.mc1.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * Message
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 04.12.2022
 */
@Entity
@Table(name = "messages")
@Accessors(chain = true)
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer sessionId;
    @Column(name = "mc1_timestamp")
    private Date mc1Timestamp;
    @Column(name = "mc2_timestamp")
    private Date mc2Timestamp;
    @Column(name = "mc3_timestamp")
    private Date mc3Timestamp;
    @Column(name = "end_timestamp")
    private Date endTimestamp;
}
