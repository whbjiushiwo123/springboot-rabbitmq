package com.whb.mq;

import com.whb.service.busi.SendSms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessUserSms {
    private Logger logger = LoggerFactory.getLogger(ProcessUserSms.class);
    @Autowired
    private SendSms sendSms;
    @RabbitListener(queues = "sms_queue")
    @RabbitHandler
    public void onMessage(String message) {
        logger.info("accept sms ready process……");
        sendSms.sendSms(message);
    }
}
