package com.example.backend0.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName MessageDTO
 * @Description
 **/
@Data
public class MessageDTO {
    Integer messageID;
    String productName;
    String shopName;
    String platformName;
    Float currentPrice;
    Float minimumPrice;
    Date time;

    public MessageDTO(Integer messageID, String productName, String shopName, String platformName, Float currentPrice, Float minimumPrice, Date time) {
        this.messageID = messageID;
        this.productName = productName;
        this.shopName = shopName;
        this.platformName = platformName;
        this.currentPrice = currentPrice;
        this.minimumPrice = minimumPrice;
        this.time = time;
    }
}
