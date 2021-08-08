package com.whb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.rabbitmq.client.*;
import com.whb.service.ISendMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
public class SendMessageService extends RabbitMQConnect implements ISendMessageService{
    public void confirm(String message) throws IOException, TimeoutException, InterruptedException {
        doConnection();
        //将信道设置为发送方确认
        channel.confirmSelect();

        //重连的时候可以使用监听
//        connection.addShutdownListener();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Ack deliveryTag："+deliveryTag
                        +"，multiple:"+multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Nack deliveryTag："+deliveryTag
                        +"，multiple:"+multiple);
            }
        });
        /**
         * 1、参数为true，投递消息如果找不到一个合适的队列，消息返回给生产者
         * 2、false，直接丢弃
         */
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("replyCode:"+replyCode);
                System.out.println("replyText:"+replyText);
                System.out.println("exchange:"+exchange);
                System.out.println("routingKey:"+routingKey);
                System.out.println("msg:"+msg);
            }
        });
        String []serverities = {"error","info","earning"};
        for(int i=0;i<serverities.length;i++){
            String serverity = serverities[i%3];
             channel.basicPublish(QUEUE_DIRECT_NAME,serverity,
                    true,null,message.getBytes());
            System.out.println("---------------------------");
            System.out.println("Send Message ：【"+serverity+"】:'"+message+"'");
            Thread.sleep(200);
        }
        closeChannel();
        closeConnect();
    }

    @Override
    public void sendMessage(String message) {
        try {
            confirm(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMultiMessage(List<String> messages) {

    }
}
