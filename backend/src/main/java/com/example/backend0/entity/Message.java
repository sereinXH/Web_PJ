package com.example.backend0.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

/**
 * @ClassName Message
 * @Description 用户消息表
 **/
@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_generator")
    @SequenceGenerator(name = "message_generator",sequenceName = "message_seq", allocationSize = 1)
    Integer ID;
    Integer concreteProductID;
    Date date;
    Integer flag;// 0代表未查看，1代表已经确认，
    Integer userID;
}
