package com.whb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class H2_Service implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(H2_Service.class);
    @Override
    public void onMessage(Message message) {
        logger.info("H2_Service Message"+new String(message.getBody()));
    }
}
