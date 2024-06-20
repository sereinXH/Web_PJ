package com.example.backend0.dto;

import lombok.Data;

/**
 * @ClassName FavotiteDTO
 * @Description
 **/
@Data
public class FavoriteDTO {
    Integer concreteProductID;
    String productName;
    String shopName;
    String platformName;
    Float currentPrice;
    Float minimumPrice;

    public FavoriteDTO(Integer concreteProductID, String productName, String shopName, String platformName, Float currentPrice, Float minimumPrice) {
        this.concreteProductID = concreteProductID;
        this.productName = productName;
        this.shopName = shopName;
        this.platformName = platformName;
        this.currentPrice = currentPrice;
        this.minimumPrice = minimumPrice;
    }
}
