package com.example.backend0.controller;

import com.example.backend0.dto.CollectDTO;
import com.example.backend0.dto.CollectIncreaseDTO;
import com.example.backend0.entity.Collect;
import com.example.backend0.entity.ConcreteProduct;
import com.example.backend0.repository.CollectRepository;
import com.example.backend0.repository.ConcreteProductRepository;
import com.example.backend0.result.Result;
import com.example.backend0.result.ResultFactory;
import com.example.backend0.util.DateUtil;
import com.example.backend0.util.Reverse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName CollectController
 * @Description
 **/
@RestController
public class CollectController {
    @Autowired
    CollectRepository collectRepository;
    @Autowired
    ConcreteProductRepository concreteProductRepository;

    @PostMapping("/collect/order")
    public Result getCollects(@RequestParam("dateRange") String dateRange,@RequestParam("date")Date date) {
        if (dateRange == null) {
            return ResultFactory.buildFailedResult("参数缺失");
        }
        Date sqlDate = Date.valueOf("2023-12-31");
        //Date sqlDate = new Date(System.currentTimeMillis());
        if (dateRange.equals("year")) {
            return ResultFactory.buildSuccessResult(getProductsByDateRange(Reverse.reverse(collectRepository.findCollectByYear(sqlDate.toLocalDate().getYear()))));
        } else if (dateRange.equals("month")) {
            return ResultFactory.buildSuccessResult(getProductsByDateRange(Reverse.reverse((collectRepository.findCollectByMonth(sqlDate.toLocalDate().getYear(), sqlDate.toLocalDate().getMonthValue())))));
        } else if (dateRange.equals("week")) {
            return ResultFactory.buildSuccessResult(getProductsByDateRange(Reverse.reverse((collectRepository.findCollectByWeek2(DateUtil.getSevenDaysAgo(sqlDate),sqlDate)))));
        } else {
            return ResultFactory.buildFailedResult("dateRange错误");
        }
    }

    public List<CollectDTO> getProductsByDateRange(List<CollectDTO> collects) {
        List<Integer> productIDs = new ArrayList<>();
        List<CollectDTO> productList = new ArrayList<>();
        for (CollectDTO collect : collects) {
            if (!in(collect.getConcreteProductID(), productIDs)) {
                productIDs.add(collect.getConcreteProductID());
                collect.setCollectNum(1);
                productList.add(collect);
            } else {
                CollectDTO cp=getById(productList,collect.getConcreteProductID());
                cp.setCollectNum(cp.getCollectNum()+1);
            }
        }
        Collections.sort(productList);
        return productList;
    }

    public boolean in(Integer integer, List<Integer> integers) {
        for (Integer item : integers) {
            if (Objects.equals(item, integer)) {
                return true;
            }
        }
        return false;
    }
    public CollectDTO getById(List<CollectDTO> productList,Integer id){
        for(CollectDTO item:productList){
            if(item.getConcreteProductID().equals(id)){
                return item;
            }
        }
        return new CollectDTO();
    }
    @PostMapping("/collect/increse")
    public Result increase(@RequestParam("concreteProductID") Integer concreteProductID){
        List<CollectIncreaseDTO> res=new ArrayList<>();
        List<Date> generatedSqlDates = ProductController.generateSqlDates(30);
        for (Date date : generatedSqlDates) {
            CollectIncreaseDTO collectIncreaseDTO=new CollectIncreaseDTO();
            collectIncreaseDTO.setConcreteProductID(concreteProductID);
            collectIncreaseDTO.setDate(date);
            collectIncreaseDTO.setIncreasement(collectRepository.findCollectNumByDateAndConcreteProductID(date,concreteProductID));
            res.add(collectIncreaseDTO);
        }
        return ResultFactory.buildSuccessResult(res);
    }

}
