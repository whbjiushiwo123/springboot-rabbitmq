package com.whb.service.impl;

import com.google.gson.Gson;
import com.whb.service.IOrderService;
import com.whb.vo.GoodTransferVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("mq")
public class MQOrderService implements IOrderService {
    private static final String DEPOT_KEY="depot.amount";
    private static final String DEPOT_EXCHANGE = "depot-amount-exchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void order(GoodTransferVo vo) {
        Gson gson = new Gson();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        rabbitTemplate.send(DEPOT_EXCHANGE,DEPOT_KEY, new Message(gson.toJson(vo).getBytes(),messageProperties));
    }
}
