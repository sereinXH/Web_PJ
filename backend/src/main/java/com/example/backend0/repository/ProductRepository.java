package com.example.backend0.repository;

import com.example.backend0.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Transactional
    @Query("SELECT p from Product p where p.productName=:productName and p.type=:type and p.productAddress=:productAddress "+
    "and p.productDate=:productDate and p.description=:description")
    Product findByAllInfo(String productName,
    String type,
    String productAddress,
    String productDate,
    String description);
}
