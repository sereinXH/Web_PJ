package com.example.backend0.dto;

import com.example.backend0.entity.ConcreteProduct;
import lombok.Data;

/**
 * @ClassName CollectDTO
 * @Description
 **/
@Data
public class CollectDTO implements Comparable<CollectDTO> {
    Integer concreteProductID;
    String productName;
    String shopName;
    String platformName;
    String type;
    Float currentPrice;
    Integer collectNum;

    public CollectDTO(Integer ID, String productName, String shopName, String platformName, String type, Float currentPrice) {
        this.concreteProductID = ID;
        this.productName = productName;
        this.shopName = shopName;
        this.platformName = platformName;
        this.type = type;
        this.currentPrice = currentPrice;
        this.collectNum=0;
    }
    public CollectDTO(){}

    // 实现 compareTo 方法
    @Override
    public int compareTo(CollectDTO other) {
        return Integer.compare(other.collectNum, this.collectNum); // 降序排序
    }
}
