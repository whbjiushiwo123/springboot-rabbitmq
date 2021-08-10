package com.whb.mq;

import com.whb.service.busi.SendSms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessUserSms implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(ProcessUserSms.class);
    @Autowired
    private SendSms sendSms;
    @Override
    public void onMessage(Message message) {
        logger.info("accept sms ready process……");
        sendSms.sendSms(new String(message.getBody()));
    }
}
