package com.whb.controller;

import com.alibaba.fastjson.JSONObject;
import com.whb.service.IOrderService;
import com.whb.vo.GoodTransferVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    @Qualifier("mq")
    private IOrderService orderService;

    @ResponseBody
    @RequestMapping("/orderConfirm")
    public String order(@RequestBody GoodTransferVo vo){
        logger.info("订单："+ JSONObject.toJSON(vo));
        orderService.order(vo);
        return "OK";
    }
}
