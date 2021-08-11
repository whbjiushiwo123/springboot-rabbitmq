package com.whb.service.impl;

import com.whb.model.UserEntity;
import com.whb.service.IUserService;
import com.whb.service.busi.SaveUser;
import com.whb.service.busi.SendEmail;
import com.whb.service.busi.SendSms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Service
@Qualifier("paralller")
public class ParalllerProcess implements IUserService {
    private Logger logger = LoggerFactory.getLogger(ParalllerProcess.class);
    @Autowired
    private SaveUser saveUser;
    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private SendSms sendSms;
    private static class SendEmailThread implements Callable<Boolean>{
        private SendEmail sendEmail;
        private String email;
        public SendEmailThread(SendEmail sendEmail,String email){
            this.sendEmail = sendEmail;
            this.email = email;
        }
        @Override
        public Boolean call() throws Exception {
            sendEmail.sendMail(email);
            return null;
        }
    }
    private static class SendSmsThread implements Callable<Boolean>{
        private SendSms sendSms;
        private String phone;
        public SendSmsThread(SendSms sendSms,String phone){
            this.sendSms = sendSms;
            this.phone = phone;
        }
        @Override
        public Boolean call() throws Exception {
            sendSms.sendSms(phone);
            return null;
        }
    }
    public boolean userRegister(UserEntity userEntity){
        FutureTask<Boolean> sendMailFuture =
                new FutureTask<>(new SendEmailThread(sendEmail, userEntity.getEmail()));
        FutureTask<Boolean> sendSmsFuture =
                new FutureTask<>(new SendSmsThread(sendSms, userEntity.getPhone()));
        try{
            saveUser.saveUser(userEntity);
            new Thread(sendMailFuture).start();
            new Thread(sendSmsFuture).start();
            sendMailFuture.get();
            sendSmsFuture.get();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
