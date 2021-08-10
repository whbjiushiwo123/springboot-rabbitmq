package com.whb.service.busi;

import com.whb.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SaveUser {
    private Logger logger = LoggerFactory.getLogger(SaveUser.class);
    Map<String, UserEntity> map = new ConcurrentHashMap<>();
    public void saveUser(UserEntity userEntity){
        logger.info("save user start -------------");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.putIfAbsent(userEntity.getId(),userEntity);
    }

    public UserEntity getUser(String userId){
        return map.get(userId);
    }
}
