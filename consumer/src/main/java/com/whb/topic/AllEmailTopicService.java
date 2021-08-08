package com.whb.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class AllEmailTopicService implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(AllEmailTopicService.class);
    @Override
    public void onMessage(Message message) {
        logger.info("AllEmailTopicService Message"+new String(message.getBody()));
    }

}
