package com.whb.service.impl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConnect {
    protected final static String EXCHANGE_DIRECT_NAME="producer_confirm_direct";
    protected final static String QUEUE_DIRECT_NAME = "producer_confirm_direct";

    protected final static String EXCHANGE_NAME="producer_confirm";
    protected final static String ROUTE_KEY="error";
    protected final static String QUEUE_NAME = "producer_confirm";
    protected Channel channel = null;
    protected Connection connection = null;

    protected void closeChannel(){
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
    protected void closeConnect(){
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected Channel doConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        return channel;
    }
}
