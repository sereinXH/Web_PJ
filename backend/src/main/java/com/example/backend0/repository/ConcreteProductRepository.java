package com.example.backend0.repository;

import com.example.backend0.dto.FullProductInfoDTO;
import com.example.backend0.dto.PartialProductDTO;
import com.example.backend0.dto.ShopProductDTO;
import com.example.backend0.entity.ConcreteProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ConcreteProductRepository extends JpaRepository<ConcreteProduct, Integer> {
    @Transactional
    @Query("SELECT new com.example.backend0.dto.FullProductInfoDTO(cp.ID,p.productName,s.shopName,s.shopAddress,pf.platformName,p.type,cp.currentPrice,p.productAddress,p.productDate,p.description) " +
            "FROM ConcreteProduct cp " +
            "JOIN Product p ON cp.productID = p.ID " +
            "JOIN Shop s ON cp.shopID = s.ID " +
            "JOIN Platform pf ON cp.platformID = pf.ID " +
            "WHERE cp.ID = :concreteProductID")
    FullProductInfoDTO findFullProductInfoByConcreteID(Integer concreteProductID);

    @Transactional
    @Query("SELECT new com.example.backend0.dto.PartialProductDTO(cp.ID,p.productName,s.shopName,pf.platformName,p.type,cp.currentPrice) " +
            "FROM ConcreteProduct cp,Product p,Shop s,Platform pf " +
            "WHERE cp.productID = p.ID " +
            "and cp.shopID = s.ID " +
            "and cp.platformID = pf.ID "
    )
    List<PartialProductDTO> findPartialProductInfoByConcreteIDDescartes();

    @Transactional
    @Query("SELECT new com.example.backend0.dto.PartialProductDTO(cp.ID,p.productName,s.shopName,pf.platformName,p.type,cp.currentPrice) " +
            "FROM ConcreteProduct cp " +
            "JOIN Product p ON cp.productID = p.ID " +
            "JOIN Shop s ON cp.shopID = s.ID " +
            "JOIN Platform pf ON cp.platformID = pf.ID ")
    List<PartialProductDTO> findPartialProductInfoByConcreteIDNatural();

    @Transactional
    @Query("SELECT new com.example.backend0.dto.ShopProductDTO(cp.ID,p.productName,p.type,p.productAddress,p.productDate,p.description,cp.shopID,pf.platformName,cp.currentPrice) " +
            "from ConcreteProduct cp " +
            "JOIN Product p on cp.productID=p.ID " +
            "JOIN Platform pf on pf.ID=cp.platformID " +
            "where cp.shopID=:shopID")
    List<ShopProductDTO> getConcreteProductsByShopID(Integer shopID);

    @Transactional
    @Query("select cp from ConcreteProduct cp where cp.shopID=:shopID and " +
            "cp.platformID=:platformID and " +
            "cp.productID=:productID and " +
            "cp.currentPrice=:currentPrice ")
    ConcreteProduct findByAllInfo(Integer shopID,
                                  Integer platformID,
                                  Integer productID,
                                  Float currentPrice);

    @Transactional
    @Query("select cp.productID " +
            "from ConcreteProduct cp where cp.ID=:concreteProductID")
    Integer findProductIdByConcreteProductId(Integer concreteProductID);

    @Transactional
    @Query("select cp from ConcreteProduct cp " +
            "where cp.productID=:productId")
    List<ConcreteProduct> findConcreteProductsByProductID(Integer productId);

    @Transactional

    @Query("select cp.ID from ConcreteProduct cp " +
            "where cp.productID=:productId")
    List<Integer> findConcreteProductIdsByProductID(Integer productId);

    @Transactional

    @Query("select s.shopName from ConcreteProduct cp join Shop s on cp.shopID=s.ID " +
            "where cp.ID=:concreteProductId")
    String getShopNameByConcreteProductID(Integer concreteProductId);

    @Transactional
    @Query("select pf.platformName from ConcreteProduct cp join Platform pf on cp.platformID=pf.ID " +
            "where cp.ID=:concreteProductId")
    String getPlatformNameByConcreteProductID(Integer concreteProductId);
}
