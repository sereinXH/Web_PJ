package com.example.backend0.dto;

import lombok.Data;

@Data
public
class PartialProductDTO {
    Integer id;
    String productName;
    String shopName;
    String platformName;
    String type;
    Float currentPrice;

    public PartialProductDTO() {
    }

    public PartialProductDTO(Integer id, String productName, String shopName, String platformName, String type, Float currentPrice) {
        this.id = id;
        this.productName = productName;
        this.shopName = shopName;
        this.platformName = platformName;
        this.type = type;
        this.currentPrice = currentPrice;
    }
}
