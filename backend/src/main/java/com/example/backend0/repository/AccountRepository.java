package com.example.backend0.repository;

import com.example.backend0.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    Optional<Account> findByAccountName(String accountName);
}
