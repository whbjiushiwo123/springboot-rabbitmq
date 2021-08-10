package com.whb.service.impl;

import com.whb.model.UserEntity;
import com.whb.service.IUserService;
import com.whb.service.busi.SaveUser;
import com.whb.service.busi.SendEmail;
import com.whb.service.busi.SendSms;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 串行接口
 */
@Service
@Qualifier("async")
public class AsyncProcess implements IUserService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SaveUser saveUser;
    public boolean userRegister(UserEntity userEntity){
        try{
            saveUser.saveUser(userEntity);
            rabbitTemplate.send("user-reg-exchange","email",
                    new Message(userEntity.getEmail().getBytes(),new MessageProperties()));
            rabbitTemplate.send("user-reg-exchange","sms",
                    new Message(userEntity.getPhone().getBytes(),new MessageProperties()));
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
