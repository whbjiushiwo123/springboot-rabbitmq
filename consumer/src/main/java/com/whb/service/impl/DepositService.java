package com.whb.service.impl;


import com.whb.service.IDepositService;
import com.whb.vo.GoodTransferVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositService implements IDepositService {

    @Autowired
    private Depot depot;
    @Override
    public int Inventory(GoodTransferVo goodTransferVo) {
        if(goodTransferVo.isInOrOut()){
            return depot.inDepot(goodTransferVo.getGoodsId(),goodTransferVo.getChangeAmount());
        }else{
            return depot.outDepot(goodTransferVo.getGoodsId(),goodTransferVo.getChangeAmount());
        }
    }
}
