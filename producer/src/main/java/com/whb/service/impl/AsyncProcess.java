package com.whb.service.impl;

import com.whb.model.UserEntity;
import com.whb.service.IUserService;
import com.whb.service.busi.SaveUser;
import com.whb.tools.DoneTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(AsyncProcess.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SaveUser saveUser;
    @DoneTime(param = "userRegister")
    public boolean userRegister(UserEntity userEntity){
        logger.info("**************   ASYNC start");
        try{
            saveUser.saveUser(userEntity);
            rabbitTemplate.send("user-reg-exchange","mail",
                    new Message(userEntity.getEmail().getBytes(),new MessageProperties()));
            rabbitTemplate.send("user-reg-exchange","sms",
                    new Message(userEntity.getPhone().getBytes(),new MessageProperties()));
            logger.info("**************   ASYNC end");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
