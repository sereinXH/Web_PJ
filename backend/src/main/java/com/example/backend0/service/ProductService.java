package com.example.backend0.service;

import com.example.backend0.dto.CompareDTO;
import com.example.backend0.dto.FullProductInfoDTO;
import com.example.backend0.dto.PartialProductDTO;
import com.example.backend0.dto.PriceHistoryDTO;
import com.example.backend0.entity.*;
import com.example.backend0.repository.ConcreteProductRepository;
import com.example.backend0.repository.PriceHistoryRepository;
import com.example.backend0.repository.ProductRepository;
import com.example.backend0.util.DateUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.example.backend0.controller.ProductController.generateSqlDates;

/**
 * @ClassName ProductService
 * @Description
 **/
@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ConcreteProductRepository concreteProductRepository;
    @Autowired
    PlatformService platformService;
    @Autowired
    ShopService shopService;
    @Autowired
    PriceHistoryRepository priceHistoryRepository;

    @Transactional
    public List<PartialProductDTO> getAllProductsWithPartialInfo() {
        List<ConcreteProduct> concreteProducts = concreteProductRepository.findAll();
        List<PartialProductDTO> res = new ArrayList<>();
        for (ConcreteProduct product : concreteProducts) {
            PartialProductDTO partialProductDTO = getPartialProductByConcreteProductId(product.getID());
            if (partialProductDTO == null) {
                return null;
            }
            res.add(partialProductDTO);
        }
        return res;
    }

    @Transactional
    public List<Product> getAllProductsWithFullInfo() {
        return productRepository.findAll();
    }

    @Transactional
    public Product save(Product product) {
        Product res = null;
        if (product != null) {
            res = productRepository.save(product);
        }
        return res;
    }

    @Transactional
    public ConcreteProduct saveConcreteProduct(ConcreteProduct concreteProduct) {
        ConcreteProduct res = null;
        if (concreteProduct != null) {
            res = concreteProductRepository.save(concreteProduct);
        }
        return res;

    }

    @Transactional
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional

    public PartialProductDTO getPartialProductByConcreteProductId(Integer id) {
        ConcreteProduct concreteProduct = concreteProductRepository.findById(id).orElse(null);
        if (concreteProduct == null) {
            return null;
        }
        Platform platform = platformService.getPlatformById(concreteProduct.getPlatformID());
        if (platform == null) {
            return null;
        }
        Product product = productRepository.findById(concreteProduct.getProductID()).orElse(null);
        if (product == null) {
            return null;
        }
        Shop shop = shopService.getShopById(concreteProduct.getShopID());
        if (shop == null) {
            return null;
        }
        PartialProductDTO partialProductDTO = new PartialProductDTO();
        partialProductDTO.setId(id);
        partialProductDTO.setProductName(product.getProductName());
        partialProductDTO.setShopName(shop.getShopName());
        partialProductDTO.setPlatformName(platform.getPlatformName());
        partialProductDTO.setType(product.getType());
        partialProductDTO.setCurrentPrice(concreteProduct.getCurrentPrice());
        return partialProductDTO;
    }

    @Transactional

    public FullProductInfoDTO findFullProductInfoByConcreteProductID(Integer concreteProductId) {

        return concreteProductRepository.findFullProductInfoByConcreteID(concreteProductId);
    }

    @Transactional
    public List<PriceHistoryDTO> getPriceHistoryByYear(Integer concreteProductID, Date date) {

        return getPriceHistoryByDays(concreteProductID, 365);
    }

    @Transactional
    public List<PriceHistoryDTO> getPriceHistoryByMonth(Integer concreteProductID, Date date) {
        return getPriceHistoryByDays(concreteProductID, 30);
    }

    @Transactional
    public List<PriceHistoryDTO> getPriceHistoryByWeek(Integer concreteProductID, Date date) {
        return getPriceHistoryByDays(concreteProductID, 7);
    }
    @Transactional
    public List<PriceHistoryDTO> getPriceHistoryByDays(Integer concreteProductID, Integer days) {
        List<Date> generatedSqlDates = generateSqlDates(days);
        List<PriceHistoryDTO> priceHistorys = new ArrayList<>();
        for (Date date : generatedSqlDates) {
            PriceHistoryDTO priceHistoryDTO = new PriceHistoryDTO();
            Float price = priceHistoryRepository.getPriceByDateAndConcreteProductId(date, concreteProductID);
            priceHistoryDTO.setPriceHistoryID(0);
            priceHistoryDTO.setDate(date);
            priceHistoryDTO.setPrice(price);
            priceHistorys.add(priceHistoryDTO);
        }
        return priceHistorys;
    }
}
