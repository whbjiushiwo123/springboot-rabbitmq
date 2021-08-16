package com.whb.service.impl;

import com.whb.service.IDepot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Service
public class Depot implements IDepot {
    private static Logger logger = LoggerFactory.getLogger(Depot.class);
    private ConcurrentHashMap<String,Integer>goodData = new ConcurrentHashMap<>();

    @PostConstruct
    public void initData(){
        goodData.put("001",1000);
        goodData.put("002",500);
        goodData.put("003",20);
    }

    @Override
    public int inDepot(String goodsId, int addAmout) {
        logger.info("+++++++++++++++++增加商品："+goodsId+"库存,数量为："+addAmout);
        int newValue = goodData.compute(goodsId, new BiFunction<String, Integer, Integer>() {
            @Override
            public Integer apply(String s, Integer integer) {
                return integer ==null?addAmout:addAmout+integer;
            }
        });
        logger.info("+++++++++++++++++增加商品："+goodsId+"库存,数量为："+newValue);
        return newValue;
    }

    @Override
    public int outDepot(String goodsId, int reduceAmount) {
        logger.info("+++++++++++++++++扣减商品："+goodsId+"库存,数量为："+reduceAmount);
        int newValue = goodData.compute(goodsId, new BiFunction<String, Integer, Integer>() {
            @Override
            public Integer apply(String s, Integer integer) {
                return integer ==null?reduceAmount:integer-reduceAmount;
            }
        });
        logger.info("+++++++++++++++++扣减商品："+goodsId+"库存,数量为："+newValue);
        return newValue;
    }

}
