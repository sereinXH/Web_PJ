package com.example.backend0.repository;

import com.example.backend0.dto.PriceHistoryDTO;
import com.example.backend0.entity.PriceHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory,Integer> {
    @Transactional
    @Query("SELECT new com.example.backend0.dto.PriceHistoryDTO(ph.ID, ph.price, ph.date) " +
            "FROM PriceHistory ph " +
            "WHERE ph .concreteProductID = :concreteProductID " +
            "AND YEAR(ph.date) = :year")
    List<PriceHistoryDTO> findPriceHistoryByYear(@Param("concreteProductID") Integer concreteProductID,
                                                 @Param("year") Integer year);

//    @Query("SELECT new com.example.backend0.dto.PriceHistoryDTO(ph.ID, ph.price, ph.date) " +
//            "FROM PriceHistory ph " +
//            "WHERE ph.concreteProductID = :concreteProductID " +
//            "AND YEAR(ph.date) = :year " +
//            "AND MONTH(ph.date) = :month")
//    List<PriceHistoryDTO> findPriceHistoryByMonth(@Param("concreteProductID") Integer concreteProductID,
//                                                  @Param("year") Integer year,
//                                                  @Param("month") Integer month);
@Transactional
    @Query("SELECT new com.example.backend0.dto.PriceHistoryDTO(ph.ID, ph.price, ph.date) " +
            "FROM PriceHistory ph " +
            "WHERE ph.concreteProductID = :concreteProductID " +
            "AND YEAR(ph.date) = :year " +
            "AND MONTH(ph.date) = :month")
    List<PriceHistoryDTO> findPriceHistoryByMonth(@Param("concreteProductID") Integer concreteProductID,
                                                  @Param("year") Integer year,
                                                  @Param("month") Integer month);
    @Transactional

    @Query("SELECT new com.example.backend0.dto.PriceHistoryDTO(ph.ID, ph.price, ph.date) " +
            "FROM PriceHistory ph " +
            "WHERE ph.concreteProductID = :concreteProductID " +
            "AND ph.date between :date and :date - 7")
    List<PriceHistoryDTO> findPriceHistoryByWeek(@Param("concreteProductID") Integer concreteProductID,
                                                 @Param("date") Date date);
    @Transactional
    @Query("SELECT new com.example.backend0.dto.PriceHistoryDTO(ph.ID, ph.price, ph.date) " +
            "FROM PriceHistory ph " +
            "WHERE ph.concreteProductID = :concreteProductID " +
            "AND ph.date between :startDate and :endDate")
    List<PriceHistoryDTO> findPriceHistoryByWeek2(Integer concreteProductID, Date startDate,Date endDate);
    @Transactional
    @Query("SELECT ph.price " +
            "FROM PriceHistory ph " +
            "WHERE ph.concreteProductID = :concreteProductId " +
            "AND ph.date <= :date " +
            "ORDER BY ph.date DESC " +  // 通过日期降序排列
            "LIMIT 1")  // 限制结果为一条记录
    Float getPriceByDateAndConcreteProductId(Date date,Integer concreteProductId);
    @Transactional
    @Query("SELECT ph " +
            "FROM PriceHistory ph " +
            "WHERE ph.concreteProductID = :concreteProductId " +
            "AND ph.date = :date " )
    PriceHistory getPriceHistoryByDateAndConcreteProductId(Date date,Integer concreteProductId);
    @Transactional
    @Query("SELECT ph " +
            "FROM PriceHistory ph " +
            "WHERE ph.concreteProductID = :concreteProductId ")
    List<PriceHistory> getPriceHistorysByConcreteProductId(Integer concreteProductId);
}
