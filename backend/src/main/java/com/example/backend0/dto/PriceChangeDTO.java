package com.example.backend0.dto;

import lombok.Data;

import java.sql.Date;

/**
 * @ClassName PriceChangeDTO
 * @Description
 **/
@Data
public class PriceChangeDTO {
    Date date;
    Float price;
    Integer concreteProductID;
}
