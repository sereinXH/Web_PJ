package com.example.backend0.dto;

import com.example.backend0.entity.PriceHistory;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

/**
 * @ClassName CompareDTO
 * @Description
 **/
@Data
public class CompareDTO {
    String shopName;
    String platformName;
    List<PriceHistory> priceHistory;


}
