package com.example.backend0.util;

import com.example.backend0.dto.CollectDTO;
import com.example.backend0.entity.Collect;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Reverse
 * @Description
 **/
public class Reverse {
    public static List<CollectDTO> reverse(List<CollectDTO> input){
        Integer size=input.size();
        List<CollectDTO> res=new ArrayList<>();
        res.clear();
        for(int i=0;i<size;i++){
            res.add(input.get(size-1-i));
        }
        return res;
    }

}
