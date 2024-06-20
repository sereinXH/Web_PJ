package com.example.backend0.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @ClassName Product
 * @Description 商品模型表
 **/

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name = "product_generator",sequenceName = "product_seq", allocationSize = 1)
    Integer ID;
    String productName;
    String type;
    String productAddress;
    String productDate;
    String description;
}
