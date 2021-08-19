package com.whb.service.impl;

import com.google.gson.Gson;
import com.whb.service.IOrderService;
import com.whb.vo.GoodTransferVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 消息持久化
 */
@Service
@Qualifier("mqAck")
public class MqDepotModel implements IOrderService {
    private Logger logger = LoggerFactory.getLogger(MqDepotModel.class);
    private static final String DEPOT_KEY="depot.account";
    private static final String DEPOT_EXCHANGE = "reduce-account-exchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void order(GoodTransferVo vo) {
        Gson gson = new Gson();
        MessageProperties properties = new MessageProperties();
        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//持久化
        rabbitTemplate.send(DEPOT_EXCHANGE,DEPOT_KEY,
                new Message(gson.toJson(vo).getBytes(),properties));
        logger.info("<<<<<<<<<<<<<<<<<<<<<下单成功");
    }
}
