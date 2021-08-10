package com.whb.service.busi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {
    private Logger logger = LoggerFactory.getLogger(SendEmail.class);
    public void sendMail(String email){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("---------------Already Send Email to:"+email);
    }
}
