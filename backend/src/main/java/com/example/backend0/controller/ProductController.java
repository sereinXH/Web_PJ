package com.example.backend0.controller;

import com.example.backend0.dto.*;
import com.example.backend0.entity.ConcreteProduct;
import com.example.backend0.entity.PriceHistory;
import com.example.backend0.entity.Product;
import com.example.backend0.repository.ConcreteProductRepository;
import com.example.backend0.repository.PriceHistoryRepository;
import com.example.backend0.repository.ProductRepository;
import com.example.backend0.result.Result;
import com.example.backend0.result.ResultFactory;
import com.example.backend0.service.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName ProductController
 * @Description
 **/
@RestController
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ConcreteProductRepository concreteProductRepository;
    @Autowired
    PriceHistoryRepository priceHistoryRepository;

    @GetMapping("/product-software")
    public Result getAllProducts3() {
        List<PartialProductDTO> productDTOS = productService.getAllProductsWithPartialInfo();
        // 打乱顺序，随机排序
        Collections.shuffle(productDTOS);
        return ResultFactory.buildSuccessResult(productDTOS);
    }
    @GetMapping("/product-Descartes")
    public Result getAllProducts2() {
        List<PartialProductDTO> productDTOS = concreteProductRepository.findPartialProductInfoByConcreteIDDescartes();
        // 打乱顺序，随机排序
        Collections.shuffle(productDTOS);
        return ResultFactory.buildSuccessResult(productDTOS);
    }
    @GetMapping("/product")
    public Result getAllProducts() {
        List<PartialProductDTO> productDTOS = concreteProductRepository.findPartialProductInfoByConcreteIDNatural();
        // 打乱顺序，随机排序
        Collections.shuffle(productDTOS);
        return ResultFactory.buildSuccessResult(productDTOS);
    }

    @PostMapping("/product/search")
    public Result getProductsWhenSearch(@RequestParam("info") String info) {
        List<PartialProductDTO> productList = productService.getAllProductsWithPartialInfo();
        List<PartialProductDTO> res = new ArrayList<>();
        for (PartialProductDTO product : productList) {
            if (matchSearch(info, product)) {
                res.add(product);
            }
        }
        // 打乱顺序，随机排序
        Collections.shuffle(res);
        return ResultFactory.buildSuccessResult(res);
    }

    @PostMapping("/product/fullInfo")
    public Result getFullProductsInfoByConcreteProductId(@RequestParam("concreteProductID") Integer concreteProductId) {
        if (concreteProductId != null)
            return ResultFactory.buildSuccessResult(productService.findFullProductInfoByConcreteProductID((concreteProductId)));
        else
            return ResultFactory.buildFailedResult("参数为空");
    }

    @PostMapping("/product/priceInfo")
    public Result getPriceInfo(@RequestParam("concreteProductID") Integer concreteProductID, @RequestParam("dataRange") String dataRange) {
        if (concreteProductID == null || dataRange == null) {
            return ResultFactory.buildFailedResult("参数缺失");
        }
        Date sqlDate = new Date(System.currentTimeMillis());
        if (dataRange.equals("year")) {
            return ResultFactory.buildSuccessResult(productService.getPriceHistoryByYear(concreteProductID, sqlDate));
        } else if (dataRange.equals("month")) {
            return ResultFactory.buildSuccessResult(productService.getPriceHistoryByMonth(concreteProductID, sqlDate));
        } else if (dataRange.equals("week")) {
            return ResultFactory.buildSuccessResult(productService.getPriceHistoryByWeek(concreteProductID, sqlDate));
        } else {
            return ResultFactory.buildFailedResult("dataRange错误");
        }
    }

    public boolean matchSearch(String searchInfo, PartialProductDTO product) {
        if (product.getProductName().contains(searchInfo) || product.getType().contains(searchInfo)) {
            return true;
        }
        return false;
    }

    public Result upperProduct(@RequestParam("productName") String productName, @RequestParam("type") String type, @RequestParam("productAddress") String productAddress,
                               @RequestParam("productDate") String productDate, @RequestParam("description") String description) {
        return upperAProduct(productName, type, productAddress, productDate, description);

    }

    public Result upperAProduct(String productName, String type, String productAddress,
                                String productDate, String description) {
        Product product = productRepository.findByAllInfo(productName, type, productAddress, productDate, description);
        if (product == null) {
            System.out.println("new product!");
            product = new Product();
        }
        product.setProductName(productName);
        product.setType(type);
        product.setProductAddress(productAddress);
        product.setProductDate(productDate);
        product.setDescription(description);
        return ResultFactory.buildSuccessResult(productService.save(product));
    }

    public Result upperAConcreteProduct(Integer shopId, Integer platformId, Integer productId, Float currentPrice) {

        ConcreteProduct concreteProduct = concreteProductRepository.findByAllInfo(shopId, platformId, productId, currentPrice);
        if (concreteProduct == null) {
            concreteProduct = new ConcreteProduct();
            System.out.println("new concreteProduct!");
        }
        concreteProduct.setPlatformID(platformId);
        concreteProduct.setProductID(productId);
        concreteProduct.setShopID(shopId);
        concreteProduct.setCurrentPrice(currentPrice);
        ConcreteProduct res = productService.saveConcreteProduct(concreteProduct);
        // 更新历史价格
        PriceHistory priceHistory = new PriceHistory();
        Date sqlDate = new Date(System.currentTimeMillis());
        priceHistory.setPrice(currentPrice);
        priceHistory.setDate(sqlDate);
        priceHistory.setConcreteProductID(res.getID());
        priceHistoryRepository.save(priceHistory);
        return ResultFactory.buildSuccessResult(res);
    }

    public Result upperAConcreteProductTest(Integer shopId, Integer platformId, Integer productId, Float currentPrice, Date date) {

        ConcreteProduct concreteProduct = concreteProductRepository.findByAllInfo(shopId, platformId, productId, currentPrice);
        if (concreteProduct == null) {
            concreteProduct = new ConcreteProduct();
            System.out.println("new concreteProduct!");
        }
        concreteProduct.setPlatformID(platformId);
        concreteProduct.setProductID(productId);
        concreteProduct.setShopID(shopId);
        concreteProduct.setCurrentPrice(currentPrice);
        ConcreteProduct res = productService.saveConcreteProduct(concreteProduct);
        // 更新历史价格
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(currentPrice);
        priceHistory.setDate(date);
        priceHistory.setConcreteProductID(res.getID());
        priceHistoryRepository.save(priceHistory);
        return ResultFactory.buildSuccessResult(res);
    }

    public Float getPriceByDateAndConcreteProductId(Date date, Integer concreteProductId) {
        return priceHistoryRepository.getPriceByDateAndConcreteProductId(date, concreteProductId);
    }


    public Result compare(Integer concreteProductID, Integer days) {
        Integer productID = concreteProductRepository.findProductIdByConcreteProductId(concreteProductID);
        List<Integer> concreteProductIds = concreteProductRepository.findConcreteProductIdsByProductID(productID);
        List<CompareDTO> res = new ArrayList<>();
        List<Date> generatedSqlDates = generateSqlDates(days);
        Float maxDif = 0f;
        Integer maxDifCon = -1;
        Float minDif = 2000f;// 写死
        Integer minDifCon = -1;
        for (Integer id : concreteProductIds) {
            CompareDTO compareDTO = new CompareDTO();
            List<PriceHistory> priceHistorys = new ArrayList<>();

            compareDTO.setPlatformName(concreteProductRepository.getPlatformNameByConcreteProductID(id));
            compareDTO.setShopName(concreteProductRepository.getShopNameByConcreteProductID(id));
            res.add(compareDTO);
            for (Date date : generatedSqlDates) {
                PriceHistory priceHistory = new PriceHistory();
                Float price = priceHistoryRepository.getPriceByDateAndConcreteProductId(date, id);
                priceHistory.setConcreteProductID(id);
                priceHistory.setDate(date);
                priceHistory.setPrice(price);
                priceHistorys.add(priceHistory);
            }
            compareDTO.setPriceHistory(priceHistorys);
            List<PriceHistory> ph = new ArrayList<>(priceHistorys);
            Collections.sort(ph);
            Float dif = ph.get(0).getPrice() - ph.get(priceHistorys.size() - 1).getPrice();
            if (dif <= minDif) {
                minDifCon = id;
                minDif = dif;
            }
            if (dif >= maxDif) {
                maxDif = dif;
                maxDifCon = id;
            }
        }
        FullCompareDTO fullCompareDTO = new FullCompareDTO();
        fullCompareDTO.setCompareDTOS(res);
        fullCompareDTO.setMaxPriceDifference(maxDif);
        fullCompareDTO.setMinPriceDifference(minDif);
        fullCompareDTO.setMaxPriceDifferenceShopName(concreteProductRepository.getShopNameByConcreteProductID(maxDifCon));
        fullCompareDTO.setMaxPriceDifferencePlatformName(concreteProductRepository.getPlatformNameByConcreteProductID(maxDifCon));
        fullCompareDTO.setMinPriceDifferenceShopName(concreteProductRepository.getShopNameByConcreteProductID(minDifCon));
        fullCompareDTO.setMinPriceDifferencePlatformName(concreteProductRepository.getPlatformNameByConcreteProductID(minDifCon));
        return ResultFactory.buildSuccessResult(fullCompareDTO);
    }

    @PostMapping("/product/compare/week")
    public Result compareByWeek(@RequestParam("concreteProductID") Integer concreteProductID) {
        Result res = compare(concreteProductID, 7);
        return res;
    }

    @PostMapping("/product/compare/month")
    public Result compareByMonth(@RequestParam("concreteProductID") Integer concreteProductID) {
        Result res = compare(concreteProductID, 30);
        return res;
    }

    @PostMapping("/product/compare")
    public Result compareByDay(@RequestParam("concreteProductID") Integer concreteProductID, @RequestParam(value = "date", required = false) Date date) {
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        Integer productID = concreteProductRepository.findProductIdByConcreteProductId(concreteProductID);
        List<Integer> concreteProductIds = concreteProductRepository.findConcreteProductIdsByProductID(productID);
        List<CompareDayDTO> res = new ArrayList<>();
        for (Integer id : concreteProductIds) {
            CompareDayDTO compareDayDTO = new CompareDayDTO();
            compareDayDTO.setPlatformName(concreteProductRepository.getPlatformNameByConcreteProductID(id));
            compareDayDTO.setShopName(concreteProductRepository.getShopNameByConcreteProductID(id));
            res.add(compareDayDTO);
            Float price = priceHistoryRepository.getPriceByDateAndConcreteProductId(date, id);
            compareDayDTO.setPrice(price);
        }
        Collections.sort(res);
        return ResultFactory.buildSuccessResult(res);
    }

    @PostMapping("/product/pricechange")
    public Result pChange(@RequestParam("concreteProductID") Integer concreteProductID) {
        List<PriceChangeDTO> res = new ArrayList<>();
        List<Date> generatedSqlDates = generateSqlDates(30);
        for (Date date : generatedSqlDates) {
            PriceChangeDTO priceChangeDTO = new PriceChangeDTO();
            priceChangeDTO.setConcreteProductID(concreteProductID);
            Float price = priceHistoryRepository.getPriceByDateAndConcreteProductId(date, concreteProductID);
            if (price < 0) {
                return ResultFactory.buildFailedResult("商品不存在");
            }
            priceChangeDTO.setPrice(price);
            priceChangeDTO.setDate(date);
            res.add(priceChangeDTO);
        }
        return ResultFactory.buildSuccessResult(res);
    }

    public static List<Date> generateSqlDates(int days) {
        List<Date> sqlDateList = new ArrayList<>();

        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());

        // 生成往前30天的日期，并将其添加到列表
        for (int i = 0; i < days; i++) {
            sqlDateList.add(new Date(calendar.getTimeInMillis()));
            calendar.add(Calendar.DAY_OF_MONTH, -1);  // 减去一天
        }

        // 反转列表，确保最早的日期在前面
        Collections.reverse(sqlDateList);

        return sqlDateList;
    }
}
