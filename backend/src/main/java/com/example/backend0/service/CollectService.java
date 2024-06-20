package com.example.backend0.service;

import com.example.backend0.dto.FavoriteDTO;
import com.example.backend0.entity.Collect;
import com.example.backend0.repository.CollectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CollectService
 * @Description
 **/
@Service
public class CollectService {
    @Autowired
    CollectRepository collectRepository;
    @Transactional
    public Collect save(Collect collect){
        return collectRepository.save(collect);
    }
    @Transactional
    public List<FavoriteDTO> getFavoritesByUserID(Integer userID){
        return collectRepository.getFavoritesByUserID(userID);
    }
}
