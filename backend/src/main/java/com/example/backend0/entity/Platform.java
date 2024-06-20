package com.example.backend0.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @ClassName PlatForm
 * @Description 平台信息表
 **/
@Data
@Entity
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_generator")
    @SequenceGenerator(name = "platform_generator",sequenceName = "platform_seq", allocationSize = 1)
    Integer ID;
    String platformName;
}
