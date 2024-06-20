package com.example.backend0.dto;

import lombok.Data;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @ClassName PriceHistorySingleInfo
 * @Description
 **/
@Data
public class PriceHistorySingleInfo {
    Float price;
    Date date;
    Integer priceHistoryID;
}
