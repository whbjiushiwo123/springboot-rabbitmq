package com.whb.netty;


import org.springframework.stereotype.Component;

@Component
public class BusinessFutureHandler implements Handler{
    private Handler nextHandler;

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public String hander(String msg) {
        System.out.println("接收到信息：" + msg);
        if(nextHandler != null){
            nextHandler.hander(msg);
        }
        return msg;
    }
}
