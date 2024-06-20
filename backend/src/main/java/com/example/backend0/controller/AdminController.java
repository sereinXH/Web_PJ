package com.example.backend0.controller;

import com.example.backend0.dto.FullProductInfoDTO;
import com.example.backend0.dto.PriceHistorySingleInfo;
import com.example.backend0.entity.*;
import com.example.backend0.repository.*;
import com.example.backend0.result.Result;
import com.example.backend0.result.ResultFactory;
import com.example.backend0.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @ClassName AdminController
 * @Description
 **/
@RestController
public class AdminController {
    @Autowired
    PlatformService platformService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    PlatformRepository platformRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ConcreteProductRepository concreteProductRepository;
    @Autowired
    ShopController shopController;
    @Autowired
    PriceHistoryRepository priceHistoryRepository;

    @PostMapping("/admin/platform/add")
    public Result upperPlatform(@RequestParam("platformName") String platformName) {
        Platform platform = new Platform();
        platform.setPlatformName(platformName);
        return ResultFactory.buildSuccessResult(platformService.save(platform));
    }

    @PostMapping("/admin/user/concreteInfo")
    public Result user_concreteInfo(@RequestParam("userID") Integer userID) {
        return ResultFactory.buildSuccessResult(userRepository.findById(userID));
    }

    @PostMapping("/admin/shop/concreteInfo")
    public Result shop_concreteInfo(@RequestParam("shopID") Integer shopID) {
        return ResultFactory.buildSuccessResult(shopRepository.findById(shopID));
    }

    @PostMapping("/admin/user/info")
    public Result user_Info() {
        return ResultFactory.buildSuccessResult(userRepository.findAll());
    }

    @PostMapping("/admin/shop/info")
    public Result shop_Info() {
        return ResultFactory.buildSuccessResult(shopRepository.findAll());
    }

    @PostMapping("/admin/user/delete")
    public Result user_delete(@RequestParam("userID") Integer userID) {
        userRepository.deleteById(userID);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/admin/shop/delete")
    public Result shop_delete(@RequestParam("shopID") Integer shopID) {
        shopRepository.deleteById(shopID);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/admin/user/change")
    public Result user_change(@RequestParam("userID") Integer userID, @RequestParam(value = "userName") String userName, @RequestParam(value = "age") Integer age, @RequestParam(value = "sex") String sex, @RequestParam(value = "phone") String phone) {
        User user = userRepository.findById(userID).orElse(null);
        if (user == null) {
            return ResultFactory.buildFailedResult("用户不存在");
        }
        user.setUserName(userName);
        user.setAge(age);
        user.setPhone(phone);
        user.setSex(sex);
        return ResultFactory.buildSuccessResult(userRepository.save(user));
    }

    @PostMapping("/admin/shop/change")
    public Result shop_change(@RequestParam("shopID") Integer shopID, @RequestParam(value = "shopName") String shopName, @RequestParam(value = "shopAddress") String shopAddress) {
        Shop shop = shopRepository.findById(shopID).orElse(null);
        if (shop == null) {
            return ResultFactory.buildFailedResult("用户不存在");
        }
        shop.setShopName(shopName);
        shop.setShopAddress(shopAddress);
        return ResultFactory.buildSuccessResult(shopRepository.save(shop));
    }

    @PostMapping("/admin/platform/name")
    public Result platform_name(@RequestParam("platformID") Integer platformID) {
        Platform platform = platformService.getPlatformById(platformID);
        if (platform == null) {
            return ResultFactory.buildFailedResult("平台不存在");
        }
        return ResultFactory.buildSuccessResult(platform.getPlatformName());
    }

    @PostMapping("/admin/platform/info")
    public Result platform_Info() {
        return ResultFactory.buildSuccessResult(platformService.findAll());
    }

    @PostMapping("/admin/platform/change")
    public Result platform_change(@RequestParam("platformID") Integer platformID, @RequestParam(value = "platformName") String platformName) {
        Platform platform = platformService.getPlatformById(platformID);
        if (platform == null) {
            return ResultFactory.buildFailedResult("用户不存在");
        }
        platform.setPlatformName(platformName);
        return ResultFactory.buildSuccessResult(platformService.save(platform));
    }

    @PostMapping("/admin/platform/delete")
    public Result platform_delete(@RequestParam("platformID") Integer platformID) {
        platformRepository.deleteById(platformID);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/admin/product/info")
    public Result product_Info() {
        List<ConcreteProduct> concreteProducts = concreteProductRepository.findAll();
        List<FullProductInfoDTO> fullProductInfoDTOS = new ArrayList<>();
        for (ConcreteProduct concreteProduct : concreteProducts) {
            FullProductInfoDTO fullProductInfoDTO = concreteProductRepository.findFullProductInfoByConcreteID(concreteProduct.getID());
            fullProductInfoDTOS.add(fullProductInfoDTO);
        }
        return ResultFactory.buildSuccessResult(fullProductInfoDTOS);
    }

    @PostMapping("/admin/product/delete")
    public Result product_delete(@RequestParam("concreteProductID") Integer productID) {
        concreteProductRepository.deleteById(productID);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/admin/product/change")
    public Result changeProduct(@RequestParam("productName") String productName, @RequestParam("type") String type, @RequestParam("productAddress") String productAddress,
                                @RequestParam("productDate") String productDate, @RequestParam("description") String description,
                                @RequestParam("platformName") String platformName, @RequestParam("currentPrice") Float currentPrice, @RequestParam("concreteProductID") Integer concreteProductID) {
        Integer shopId = -1;//default
        return shopController.changeProduct(productName, type, productAddress, productDate, description, shopId, platformName, currentPrice, concreteProductID);
    }

    @PostMapping("/priceHistory/change")
    public Result changePriceHistory(@RequestParam("priceHistoryId") Integer priceHistoryId, @RequestParam("price") Float price, @RequestParam("concreteProductID") Integer concreteProductID, @RequestParam("date") Date date) {
        PriceHistory priceHistory = priceHistoryRepository.findById(priceHistoryId).orElse(null);
        Date current = new Date(System.currentTimeMillis());
        if (priceHistory == null || date.toString().equals(current.toString()) || (!date.toString().equals(priceHistory.getDate().toString()) && priceHistoryRepository.getPriceHistoryByDateAndConcreteProductId(date, concreteProductID) != null)) {
            return ResultFactory.buildFailedResult("修改失败");
        }
        priceHistory.setPrice(price);
        priceHistory.setConcreteProductID(concreteProductID);
        priceHistory.setDate(date);
        return ResultFactory.buildSuccessResult(priceHistoryRepository.save(priceHistory));
    }

    @PostMapping("/admin/priceHistory/getByConcreteProductId")
    public Result priceHistoryGet(@RequestParam("concreteProductID") Integer concreteProductID) {
        List<PriceHistory> priceHistories = priceHistoryRepository.getPriceHistorysByConcreteProductId(concreteProductID);
        priceHistories.sort(Comparator.comparing(PriceHistory::getDate));// 按日期升序排序
        return ResultFactory.buildSuccessResult(priceHistories);
    }

    @PostMapping("/admin/priceHistory/delete")
    public Result priceHistoryDelete(@RequestParam("priceHistoryID") Integer priceHistoryID) {
        priceHistoryRepository.deleteById(priceHistoryID);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/priceHistory/getBypriceHistoryID")
    public Result getPAndD(@RequestParam("priceHistoryID") Integer priceHistoryID) {
        PriceHistory priceHistory = priceHistoryRepository.findById(priceHistoryID).orElse(null);
        if (priceHistory == null) {
            return ResultFactory.buildFailedResult("不存在");
        }
        PriceHistorySingleInfo priceHistorySingleInfo = new PriceHistorySingleInfo();
        priceHistorySingleInfo.setPriceHistoryID(priceHistoryID);
        priceHistorySingleInfo.setPrice(priceHistory.getPrice());
        priceHistorySingleInfo.setDate(priceHistory.getDate());
        return ResultFactory.buildSuccessResult(priceHistorySingleInfo);
    }
}
