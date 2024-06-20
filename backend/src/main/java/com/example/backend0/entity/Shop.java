package com.example.backend0.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @ClassName Shop
 * @Description 商家实体表
 **/
@Data
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop_generator")
    @SequenceGenerator(name = "shop_generator",sequenceName = "shop_seq", allocationSize = 1)
    Integer ID;
    String shopName;
    String shopAddress;

}
