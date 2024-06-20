package com.example.backend0.dto;

import lombok.Data;

/**
 * @ClassName FullProductInfoDTO
 * @Description
 **/
@Data
public class FullProductInfoDTO {

    private Integer concreteProductID;
    private String productName;
    private String shopName;
    private String shopAddress;
    private String platformName;
    private String type;
    private Float currentPrice;
    private String productAddress;
    private String productDate;
    private String description;



    public FullProductInfoDTO() {
    }

    public FullProductInfoDTO(Integer concreteProductID, String productName, String shopName, String shopAddress, String platformName, String type, Float currentPrice, String productAddress, String productDate, String description) {
        this.concreteProductID = concreteProductID;
        this.productName = productName;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.platformName = platformName;
        this.type = type;
        this.currentPrice = currentPrice;
        this.productAddress = productAddress;
        this.productDate = productDate;
        this.description = description;
    }
}
