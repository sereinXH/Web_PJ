package com.example.backend0.dto;

import lombok.Data;

import java.sql.Date;

/**
 * @ClassName PriceHistoryDTO
 * @Description
 **/
@Data
public class PriceHistoryDTO {
    private Integer priceHistoryID;
    private Float price;
    private Date date;

    public PriceHistoryDTO() {
    }

    public PriceHistoryDTO(Integer priceHistoryID, Float price, Date date) {
        this.priceHistoryID = priceHistoryID;
        this.price = price;
        this.date = date;
    }
}
