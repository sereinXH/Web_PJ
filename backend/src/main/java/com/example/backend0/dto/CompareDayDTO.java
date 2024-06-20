package com.example.backend0.dto;

import com.example.backend0.entity.PriceHistory;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CompareDayDTO
 * @Description
 **/
@Data
public class CompareDayDTO implements Comparable<CompareDayDTO>{
    String shopName;
    String platformName;
    Float price;

    @Override
    public int compareTo(CompareDayDTO o) {
        return Float.compare(o.price,this.price);
    }
}
