package com.whb.service.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback {
    private Logger logger= LoggerFactory.getLogger(ConfirmCallBack.class);
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            logger.info("-------------------------confirm message-------------------------");
        }else{
            logger.info("-------------------------retry send message-------------------------");
        }
    }
}
