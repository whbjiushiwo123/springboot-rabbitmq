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
    private volatile static Object lock1 = new Object();
    private volatile static Object lock2 = new Object();
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
    class T1 implements Runnable{

        @Override
        public void run() {
            synchronized (lock1){
                try {
                    System.out.println("T1 开始加锁 1！"+lock1);
                    Thread.sleep(2000L);
                    synchronized (lock2){
                        System.out.println("T1 加锁 2 成功！"+lock2);
                    }
                    lock1 = "OK";
                    System.out.println("T1 结束！");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class T2 implements Runnable{

        @Override
        public void run() {
            synchronized (lock2){
                try {
                    System.out.println("T2 开始加锁 1！"+lock2);
                    Thread.sleep(2000L);
                    synchronized (lock1){
                        System.out.println("T2 加锁 2 成功！"+lock1);
                    }
                    lock2 = "OK";
                    System.out.println("T2 结束！");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
