package com.whb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class H3_Service implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(H3_Service.class);
    @Override
    public void onMessage(Message message) {
        logger.info("H3_Service Message"+new String(message.getBody()));
    }
}
