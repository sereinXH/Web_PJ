package com.example.backend0.repository;

import com.example.backend0.dto.MessageDTO;
import com.example.backend0.entity.ConcreteProduct;
import com.example.backend0.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message,Integer> {
    @Transactional

    @Query("SELECT new com.example.backend0.dto.MessageDTO(m.ID,p .productName,s .shopName,pf .platformName,cp .currentPrice,c.minimumPrice,m .date) "+
            "from Message m " +
            "join ConcreteProduct cp on cp.ID=m.concreteProductID " +
            "join Product p on cp.productID=p .ID " +
            "join Collect c on c .concreteProductID=cp .ID and c.userID=:userID "+
            "join  Platform pf on pf .ID=cp .platformID "+
            "join Shop s on s.ID=cp .shopID "+
            "where m.userID=:userID  ")
    List<MessageDTO> getMessagesByUserID(Integer userID);

//    @Query("SELECT new com.example.backend0.dto.MessageDTO(Product.productName,Shop.shopName,Platform.platformName,ConcreteProduct.currentPrice,Collect.minimumPrice,Message.date) "+
//            "from Message,ConcreteProduct,Product,Collect,Shop,Platform "+
//            "where Message.userID=:userID and Message.concreteProductID=ConcreteProduct.ID "+
//            "and ConcreteProduct.productID=Product.ID and Collect.concreteProductID=ConcreteProduct.ID "+
//            "and Shop.ID=ConcreteProduct.shopID and Platform.ID=ConcreteProduct.platformID")
//    List<MessageDTO> getMessagesByUserID(Integer userID);
}
