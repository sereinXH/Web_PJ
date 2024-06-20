package com.example.backend0.controller;

import com.example.backend0.dto.ShopProductDTO;
import com.example.backend0.entity.ConcreteProduct;
import com.example.backend0.entity.PriceHistory;
import com.example.backend0.entity.Product;
import com.example.backend0.repository.ConcreteProductRepository;
import com.example.backend0.repository.PlatformRepository;
import com.example.backend0.repository.PriceHistoryRepository;
import com.example.backend0.result.Result;
import com.example.backend0.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

/**
 * @ClassName ShopController
 * @Description
 **/
@RestController
public class ShopController {
    @Autowired
    ProductController productController;
    @Autowired
    ConcreteProductRepository concreteProductRepository;
    @Autowired
    PriceHistoryRepository priceHistoryRepository;
    @Autowired
    PlatformRepository platformRepository;

    @PostMapping("/shop/addProduct")
    public Result addProduct(@RequestParam("productName") String productName, @RequestParam("type") String type, @RequestParam("productAddress") String productAddress,
                             @RequestParam("productDate") String productDate, @RequestParam("description") String description,
                             @RequestParam("shopID") Integer shopId, @RequestParam("platformID") Integer platformId, @RequestParam("currentPrice") Float currentPrice) {
        Result result = productController.upperAProduct(productName, type, productAddress, productDate, description);
        Integer productID = ((Product) (result.getContents())).getID();
        return ResultFactory.buildSuccessResult(productController.upperAConcreteProduct(shopId, platformId, productID, currentPrice));
    }

    @PostMapping("/shop/Product")
    public Result getProductByShopId(@RequestParam("shopID") Integer shopID) {
        return ResultFactory.buildSuccessResult(concreteProductRepository.getConcreteProductsByShopID(shopID));
    }

    @PostMapping("/shop/changeProduct")
    public Result changeProduct(@RequestParam("productName") String productName, @RequestParam("type") String type, @RequestParam("productAddress") String productAddress,
                                @RequestParam("productDate") String productDate, @RequestParam("description") String description,
                                @RequestParam("shopID") Integer shopId, @RequestParam("platformName") String platformName, @RequestParam("currentPrice") Float currentPrice, @RequestParam("concreteProductID") Integer concreteProductID) {
        ConcreteProduct concreteProduct = concreteProductRepository.findById(concreteProductID).orElse(null);
        if (concreteProduct == null) {
            return ResultFactory.buildFailedResult("不存在改商品");
        }
        Float oPrice = concreteProduct.getCurrentPrice();
        Result result = productController.upperAProduct(productName, type, productAddress, productDate, description);
        Integer productID = ((Product) (result.getContents())).getID();
        concreteProduct.setProductID(productID);
//        concreteProduct.setCurrentPrice(currentPrice);
        concreteProduct.setPlatformID(platformRepository.getIdByPlatformName(platformName));
        ConcreteProduct res = concreteProductRepository.save(concreteProduct);

//        // 更新历史价格
//        if(!currentPrice.equals(oPrice)){
//        PriceHistory priceHistory=new PriceHistory();
//        Date sqlDate = new Date(System.currentTimeMillis());
//        priceHistory.setPrice(currentPrice);
//        priceHistory.setDate(sqlDate);
//        priceHistory.setConcreteProductID(res.getID());
//        priceHistoryRepository.save(priceHistory);
//        }

        return ResultFactory.buildSuccessResult(res);
    }

    @PostMapping("/product/changePrice")
    public Result changeProductOnlyPrice2(@RequestParam("currentPrice") Float currentPrice, @RequestParam("concreteProductID") Integer concreteProductID, @RequestParam(value = "Date", required = false) Date date) {
        ConcreteProduct concreteProduct = concreteProductRepository.findById(concreteProductID).orElse(null);
        Float oprice = concreteProduct.getCurrentPrice();
        if (concreteProduct == null) {
            return ResultFactory.buildFailedResult("不存在改商品");
        }
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        if (currentPrice.equals(oprice) || currentPrice <= 0) {
            return ResultFactory.buildFailedResult("错误的价格修改");
        }
        if (priceHistoryRepository.getPriceHistoryByDateAndConcreteProductId(date, concreteProductID) != null) {
            return ResultFactory.buildFailedResult("已经修改");
        }

        Date current = new Date(System.currentTimeMillis());
        if (date.toString().equals(current.toString())) {
            Float oPrice = concreteProduct.getCurrentPrice();
            concreteProduct.setCurrentPrice(currentPrice);
            ConcreteProduct res = concreteProductRepository.save(concreteProduct);
        }
        // 更新历史价格
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(currentPrice);
        priceHistory.setDate(date);
        priceHistory.setConcreteProductID(concreteProductID);
        priceHistoryRepository.save(priceHistory);
        return ResultFactory.buildSuccessResult();
    }

    public Result changeProductOnlyPrice(@RequestParam("currentPrice") Float currentPrice, @RequestParam("concreteProductID") Integer concreteProductID, @RequestParam(value = "Date", required = false) Date date) {
        ConcreteProduct concreteProduct = concreteProductRepository.findById(concreteProductID).orElse(null);
        Float oprice = concreteProduct.getCurrentPrice();
        if (concreteProduct == null) {
            return ResultFactory.buildFailedResult("不存在改商品");
        }
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        if (currentPrice.equals(oprice) || currentPrice <= 0) {
            return ResultFactory.buildFailedResult("错误的价格修改");
        }
        if (priceHistoryRepository.getPriceHistoryByDateAndConcreteProductId(date, concreteProductID) != null) {
            return ResultFactory.buildFailedResult("已经修改");
        }


        Float oPrice = concreteProduct.getCurrentPrice();
        concreteProduct.setCurrentPrice(currentPrice);
        ConcreteProduct res = concreteProductRepository.save(concreteProduct);

        // 更新历史价格
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(currentPrice);
        priceHistory.setDate(date);
        priceHistory.setConcreteProductID(concreteProductID);
        priceHistoryRepository.save(priceHistory);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/shop/product/delete")
    public Result product_delete(@RequestParam("concreteProductID") Integer productID) {
        concreteProductRepository.deleteById(productID);
        return ResultFactory.buildSuccessResult();
    }

}
