package com.whb.mq;

import com.alibaba.fastjson.JSON;
import com.whb.service.IDepositService;
import com.whb.vo.GoodTransferVo;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class MqDepotService {
    @Autowired
    private IDepositService depositService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "reduce_account",ackMode = "MANUAL" )
    @RabbitHandler
    public void onMessage(Message message){
        System.out.println("recevie:"+new String(message.getBody()));
        GoodTransferVo vo = JSON.parseObject(new String(message.getBody()), GoodTransferVo.class);
        depositService.Inventory(vo);
    }
}
