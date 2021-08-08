package com.whb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class H1_Service implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(H1_Service.class);
    @Override
    public void onMessage(Message message) {
        logger.info("H1_Service Message"+new String(message.getBody()));
    }
}
