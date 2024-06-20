package com.example.backend0.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @ClassName User
 * @Description 用户实体表
 **/
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator",sequenceName = "user_seq", allocationSize = 1)
    Integer ID; //
    String userName;
    Integer age;
    String sex; // male or female
    String phone;
}
