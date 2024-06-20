package com.example.backend0.entity;

import com.example.backend0.dto.CollectDTO;
import com.example.backend0.dto.PriceHistoryDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

/**
 * @ClassName PriceHistory
 * @Description 价格变动表
 **/
@Data
@Entity
public class PriceHistory implements Comparable<PriceHistory>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_history_generator")
    @SequenceGenerator(name = "price_history_generator",sequenceName = "price_history_seq", allocationSize = 1)
    Integer ID;
    Integer concreteProductID;
    Float price;
    Date date;
    @Override
    public int compareTo(PriceHistory other) {
        return Float.compare(other.price, this.price); // 降序排序
    }
}
