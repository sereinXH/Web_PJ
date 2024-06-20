package com.example.backend0.repository;

import com.example.backend0.entity.Platform;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform,Integer> {
    @Query("select p.ID from Platform p where p.platformName=:platformName")
    Integer getIdByPlatformName(String platformName);
}
