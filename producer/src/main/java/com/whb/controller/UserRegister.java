package com.whb.controller;

import com.whb.model.ResponseEntity;
import com.whb.model.UserEntity;
import com.whb.service.IUserService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserRegister {
    private static Logger logger = LoggerFactory.getLogger(UserRegister.class);
    @Autowired
    @Qualifier("paralller")
    private IUserService userService;
    @ResponseBody
    @RequestMapping("/userRegister")
    public ResponseEntity userRegister(@RequestBody UserEntity userEntity){
        ResponseEntity entity = new ResponseEntity();
        entity.setCode("1000");
        entity.setMsg("OK");
        entity.setResult(userService.userRegister(userEntity));
        return entity;
    }
}
