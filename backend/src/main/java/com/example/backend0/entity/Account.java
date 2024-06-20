package com.example.backend0.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @ClassName Account
 * @Description 账号实体表
 **/
@Data
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @SequenceGenerator(name = "account_generator",sequenceName = "account_seq", allocationSize = 1)
    Integer ID;
    String accountName;
    String password;
    Integer type; // 0 管理员，1用户，2商户
    Integer infoID;

}
