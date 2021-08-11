package com.whb.service.impl;

import com.whb.model.UserEntity;
import com.whb.service.IUserService;
import com.whb.service.busi.SaveUser;
import com.whb.service.busi.SendEmail;
import com.whb.service.busi.SendSms;
import com.whb.tools.DoneTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 串行接口
 */
@Service
@Qualifier("serial")
public class SerialProcess implements IUserService {
    @Autowired
    private SaveUser saveUser;
    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private SendSms sendSms;

    @DoneTime(param = "userRegister")
    public boolean userRegister(UserEntity userEntity){
        try{
            saveUser.saveUser(userEntity);
            sendEmail.sendMail(userEntity.getEmail());
            sendSms.sendSms(userEntity.getPhone());
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
