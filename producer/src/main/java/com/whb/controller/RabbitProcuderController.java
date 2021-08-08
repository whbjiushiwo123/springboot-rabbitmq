package com.whb.controller;

import com.alibaba.fastjson.JSON;
import com.whb.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/rabbitmq")
public class RabbitProcuderController {
    private Logger logger = LoggerFactory.getLogger(RabbitProcuderController.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ResponseBody
    @RequestMapping("/fanoutSend")
    public String fanoutSend(@RequestBody List<UserEntity> userEntities){
        String opt = "";
        try{
            for(int i=0;i<userEntities.size();i++){
                String str = "Fanout , the message_"+i+ JSON.toJSONString(userEntities);
                logger.info("****************** Send Message:["+str+"]");
                rabbitTemplate.send("fanout-exchange","",
                        new Message(str.getBytes(),new MessageProperties()));
            }
        }catch (Exception e){
            opt = e.getCause().toString();
        }
        return opt;
    }
    @ResponseBody
    @RequestMapping("/topicSend")
    public String topicSend(@RequestBody List<UserEntity> userEntities){
        String opt = "";
        try{
            String [] serverities = {"error","info","warning"};
            String [] modules = {"email","order","user"};
            for(String server:serverities){
                for(String module:modules){
                    String routeKey = server+"."+module;
                    logger.info("******************Send Message,the routeKey:["+routeKey+"]["+JSON.toJSONString(userEntities)+"]");
                    rabbitTemplate.send("topic-exchange",routeKey,
                            new Message(JSON.toJSONString(userEntities).getBytes(),new MessageProperties()));

                }
            }
            opt="OK";
        }catch (Exception e){
            opt = e.getCause().toString();
        }
        return opt;
    }
}
