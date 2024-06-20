package com.example.backend0.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName FullCompareDTO
 * @Description
 **/
@Data
public class FullCompareDTO {
    List<CompareDTO> compareDTOS;
    Float minPriceDifference;
    Float maxPriceDifference;
    String minPriceDifferenceShopName;
    String minPriceDifferencePlatformName;
    String maxPriceDifferenceShopName;
    String maxPriceDifferencePlatformName;
}
