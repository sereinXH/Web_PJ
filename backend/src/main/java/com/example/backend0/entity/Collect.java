package com.example.backend0.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

/**
 * @ClassName Collect
 * @Description 用户收藏表
 **/
@Data
@Entity
public class Collect {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "collect_generator")
    @SequenceGenerator(name = "collect_generator",sequenceName = "collect_seq", allocationSize = 1)
    Integer ID;
    Integer userID;
    Integer concreteProductID;
    Float minimumPrice;
    Date date;
}
