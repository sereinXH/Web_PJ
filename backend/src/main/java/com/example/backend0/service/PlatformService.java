package com.example.backend0.service;

import com.example.backend0.entity.Platform;
import com.example.backend0.repository.PlatformRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName PlatFormService
 * @Description
 **/
@Service
public class PlatformService {
    @Autowired
    PlatformRepository platformRepository;
    public Platform save(Platform platform){
        if(platform!=null){
            return platformRepository.save(platform);
        }
        return null;
    }
    @Transactional
    public Platform getPlatformById(Integer id){
        return platformRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Platform> findAll(){
        return platformRepository.findAll();
    }
}