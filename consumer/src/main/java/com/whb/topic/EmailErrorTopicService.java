package com.whb.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class EmailErrorTopicService implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(EmailErrorTopicService.class);
    @Override
    public void onMessage(Message message) {
        logger.info("EmailErrorTopicService Message"+new String(message.getBody()));
    }
}
