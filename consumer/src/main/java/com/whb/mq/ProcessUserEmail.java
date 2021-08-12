package com.whb.mq;

import com.whb.service.busi.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessUserEmail {
    private Logger logger = LoggerFactory.getLogger(ProcessUserEmail.class);
    @Autowired
    private SendEmail sendEmail;
    @RabbitListener(queues = "mail_queue")
    @RabbitHandler
    public void onMessage(String message) {
        logger.info("accept message ready process……");
        sendEmail.sendMail(message);
    }
}
