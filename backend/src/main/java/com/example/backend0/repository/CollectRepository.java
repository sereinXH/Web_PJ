package com.example.backend0.repository;

import com.example.backend0.dto.FavoriteDTO;
import com.example.backend0.dto.FullProductInfoDTO;
import com.example.backend0.dto.CollectDTO;
import com.example.backend0.entity.Collect;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
@Repository
public interface CollectRepository extends JpaRepository<Collect, Integer> {
    @Transactional
    @Query("SELECT new com.example.backend0.dto.FavoriteDTO(cp.ID,p .productName,s.shopName,pf .platformName,cp.currentPrice,c .minimumPrice) " +
            "FROM Collect c " +
            "JOIN ConcreteProduct  cp on cp .ID=c.concreteProductID " +
            "JOIN Shop s on s.ID=cp .shopID " +
            "JOIN Platform pf on pf .ID=cp .platformID " +
            "join Product p on p.ID=cp .productID " +
            "where c.userID=:userID ")
    List<FavoriteDTO> getFavoritesByUserID(Integer userID);

    @Transactional
    @Query("SELECT new com.example.backend0.dto.CollectDTO(ph.concreteProductID, p.productName, s.shopName,pf.platformName,p.type,cp.currentPrice) " +
            "FROM Collect ph " +
            "JOIN ConcreteProduct cp on cp.ID=ph.concreteProductID " +
            "JOIN Product p ON cp.productID = p.ID " +
            "JOIN Shop s ON cp.shopID = s.ID " +
            "JOIN Platform pf ON cp.platformID = pf.ID " +
            "WHERE YEAR(ph.date) = :year")
    List<CollectDTO> findCollectByYear(@Param("year") Integer year);

    @Transactional
    @Query("SELECT new com.example.backend0.dto.CollectDTO(ph.concreteProductID, p.productName, s.shopName,pf.platformName,p.type,cp.currentPrice) " +
            "FROM Collect ph " +
            "JOIN ConcreteProduct cp on cp.ID=ph.concreteProductID " +
            "JOIN Product p ON cp.productID = p.ID " +
            "JOIN Shop s ON cp.shopID = s.ID " +
            "JOIN Platform pf ON cp.platformID = pf.ID " +
            "WHERE YEAR(ph.date) = :year " +
            "AND MONTH(ph.date) = :month")
    List<CollectDTO> findCollectByMonth(@Param("year") Integer year,
                                        @Param("month") Integer month);

    @Transactional
    @Query("SELECT new com.example.backend0.dto.CollectDTO(ph.concreteProductID, p.productName, s.shopName,pf.platformName,p.type,cp.currentPrice) " +
            "FROM Collect ph " +
            "JOIN ConcreteProduct cp on cp.ID=ph.concreteProductID " +
            "JOIN Product p ON cp.productID = p.ID " +
            "JOIN Shop s ON cp.shopID = s.ID " +
            "JOIN Platform pf ON cp.platformID = pf.ID " +
            "WHERE  ph.date between :date and :date - 7")
    List<CollectDTO> findCollectByWeek(@Param("date") Date date);

    @Transactional
    @Query("SELECT new com.example.backend0.dto.CollectDTO(ph.concreteProductID, p.productName, s.shopName,pf.platformName,p.type,cp.currentPrice) " +
            "FROM Collect ph " +
            "JOIN ConcreteProduct cp on cp.ID=ph.concreteProductID " +
            "JOIN Product p ON cp.productID = p.ID " +
            "JOIN Shop s ON cp.shopID = s.ID " +
            "JOIN Platform pf ON cp.platformID = pf.ID " +
            "WHERE  ph.date between :startDate and :endDate")
    List<CollectDTO> findCollectByWeek2(Date startDate, Date endDate);

    @Transactional
    @Query("SELECT COUNT(c) " +
            "FROM Collect c " +
            "WHERE c.concreteProductID = :concreteProductID " +
            "AND DATE(c.date) = :date")
    Integer findCollectNumByDateAndConcreteProductID(@Param("date") Date date, @Param("concreteProductID") Integer concreteProductID);

    @Transactional
    @Query("select  c from Collect c where c.concreteProductID=:concreteProductID and c.userID=:userID")
    Collect getCollectByUserIDAndConcreteProductID(Integer userID, Integer concreteProductID);

}

