package com.example.backend0.service;

import com.example.backend0.entity.PriceHistory;
import com.example.backend0.repository.PriceHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName PriceHistoryService
 * @Description
 **/
@Service
public class PriceHistoryService {

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Transactional
    public void addPriceHistory(PriceHistory priceHistory) {
        // 实现添加价格历史的逻辑
        priceHistoryRepository.save(priceHistory);
    }

    @Transactional
    public void addPriceHistoryWithException(PriceHistory priceHistory) {
        // 实现添加价格历史的逻辑
        priceHistoryRepository.save(priceHistory);

        // 模拟在事务内抛出异常
        throw new RuntimeException("Simulated exception within transaction");
    }

    public List<PriceHistory> getAllPriceHistories() {
        // 实现获取所有价格历史的逻辑
        return priceHistoryRepository.findAll();
    }
}
