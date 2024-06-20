package com.example.backend0.service;

import com.example.backend0.entity.Account;
import com.example.backend0.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName AccountService
 * @Description
 **/
@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Transactional
    public boolean accountNameIsPresent(String accountName){
        return accountRepository.findByAccountName(accountName).isPresent();
    }
    @Transactional
    public boolean rightAccount(String accountName,String password){
        Account account=accountRepository.findByAccountName(accountName).orElse(null);
        if(account!=null&&account.getPassword().equals(password)){
            return true;
        }
        return false;
    }
    @Transactional
    public Account save(Account account){
        if(account!=null){
            return accountRepository.save(account);
        }
        return null;
    }
    @Transactional
    public Account getAccountByAccountName(String accountName){
        return accountRepository.findByAccountName(accountName).orElse(null);
    }
}
