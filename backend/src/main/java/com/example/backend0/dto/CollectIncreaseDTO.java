package com.example.backend0.dto;

import lombok.Data;

import java.sql.Date;

/**
 * @ClassName CollectIncreaseDTO
 * @Description
 **/
@Data
public class CollectIncreaseDTO {
    Integer concreteProductID;
    Date date;
    Integer increasement;
}
