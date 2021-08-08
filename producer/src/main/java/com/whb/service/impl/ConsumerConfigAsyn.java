package com.whb.service.impl;

import com.rabbitmq.client.*;
import com.whb.service.ISendMessageService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ConsumerConfigAsyn extends RabbitMQConnect implements InitializingBean {
    @Autowired
    private ISendMessageService sendMessageService;
    @Override
    public void afterPropertiesSet() throws Exception {
        AtomicInteger integer = new AtomicInteger();
        do  {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("客户端"+integer.get()+"启动");
                    try {
                        sender();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }while (integer.getAndIncrement() < 3);

    }
    public void sender()throws Exception{
        doConnection();
//        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.FANOUT);
        channel.exchangeDeclare(EXCHANGE_DIRECT_NAME,BuiltinExchangeType.DIRECT);

        channel.queueDeclare(QUEUE_DIRECT_NAME,false,false,false,null);
        //此处绑定队列
        channel.queueBind(QUEUE_DIRECT_NAME,EXCHANGE_DIRECT_NAME,ROUTE_KEY);

        System.out.println(" [*] Waiting for messages. ");
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("Received ["+envelope.getRoutingKey()+"]"+message);
            }
        };
        channel.basicConsume(QUEUE_DIRECT_NAME,true,consumer );

    }

}
