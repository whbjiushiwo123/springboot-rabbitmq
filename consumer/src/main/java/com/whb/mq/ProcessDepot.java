package com.whb.mq;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.whb.service.IDepositService;
import com.whb.vo.GoodTransferVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收到消息应答
 */
@Service
public class ProcessDepot implements ChannelAwareMessageListener {
    private Logger logger = LoggerFactory.getLogger(ProcessDepot.class);
    @Autowired
    private IDepositService depositService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "reduce_account",ackMode = "MANUAL" )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        System.out.println("recevie:"+msg);
        try{
            Gson gson = new Gson();
            GoodTransferVo vo =gson.fromJson(msg,GoodTransferVo.class);
            depositService.Inventory(vo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            logger.info(">>>>>>>>>>>库存处理成功，应答MQ");
        }catch (Exception e){
            logger.error("消费者确认返回异常："+e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            logger.info(">>>>>>>>>>>库存处理失败，要求重发");

        }
    }
}
