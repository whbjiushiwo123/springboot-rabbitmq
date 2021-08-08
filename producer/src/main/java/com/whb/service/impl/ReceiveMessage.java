package com.whb.service.impl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.whb.model.Message;
import com.whb.service.IReciveMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class ReceiveMessage extends RabbitMQConnect implements IReciveMessage,InitializingBean {


    public void receive() {
        try {
            channel = doConnection();
            channel.queueDeclare(EXCHANGE_NAME,false,false,false,null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (cosumerTag,delivery)->{
              String message = new String(delivery.getBody(),"UTF-8");
              System.out.println( " [x] 收到 '" + message + "'" );
            };
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag->{});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }



    public void taskWorker( String name){
        try {
            channel = doConnection();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            Message message = new Message();
            DeliverCallback deliverCallback = (cosumerTag,delivery)->{
                String msg = new String(delivery.getBody(),"UTF-8");
                System.out.println( name+" [x] 收到 '" + message + "'" );
                try {
                    doWork(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message.setMessage(msg);
            };
            boolean autoAck = true;
            channel.basicConsume(QUEUE_NAME,autoAck,deliverCallback,consumerTag->{});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void doWork(String message) throws Exception{
        for(char c:message.toCharArray()){
            if(c =='.') Thread.sleep(10000);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
