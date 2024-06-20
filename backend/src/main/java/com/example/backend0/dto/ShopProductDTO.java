package com.example.backend0.dto;

import lombok.Data;

@Data
public class ShopProductDTO {
    Integer concreteProductID;
    String productName;
    String type;
    String productAddress;
    String productDate;
    String description;
    Integer shopID;
    String platformName;
    Float currentPrice;

    public ShopProductDTO(Integer concreteProductID, String productName, String type, String productAddress, String productDate, String description, Integer shopID, String platformName, Float currentPrice) {
        this.concreteProductID = concreteProductID;
        this.productName = productName;
        this.type = type;
        this.productAddress = productAddress;
        this.productDate = productDate;
        this.description = description;
        this.shopID = shopID;
        this.platformName = platformName;
        this.currentPrice = currentPrice;
    }
}
