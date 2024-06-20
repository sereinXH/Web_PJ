package com.example.backend0;

import com.example.backend0.entity.PriceHistory;
import com.example.backend0.repository.PriceHistoryRepository;
import com.example.backend0.service.PriceHistoryService;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @ClassName TransactionTest
 * @Description
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Backend0Application.class)
public class TransactionTest {

    @Autowired
    PriceHistoryRepository priceHistoryRepository;
    private PriceHistoryService priceHistoryService;

    @Autowired
    public void YourTestClassName(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @Test
    public void testTransactionRollback() {
        Integer originalSize=priceHistoryService.getAllPriceHistories().size();
        // 模拟在事务内抛出异常
        assertThrows(RuntimeException.class, () -> {
            // 在同一事务内添加一条价格历史记录并抛出异常
            PriceHistory anotherHistory = new PriceHistory();
            anotherHistory.setDate(Date.valueOf("2023-01-02"));
            anotherHistory.setPrice(15.0f);
            priceHistoryService.addPriceHistoryWithException(anotherHistory);
        });
        // 验证事务回滚后的状态
        assertEquals(originalSize, priceHistoryService.getAllPriceHistories().size());
    }
}
