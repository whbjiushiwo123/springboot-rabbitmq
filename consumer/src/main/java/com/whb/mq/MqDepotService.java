package com.whb.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqDepotService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "reduce_account")
    @RabbitHandler
    public void onMessage(String message){
        System.out.println("recevie:"+message);
    }
}
