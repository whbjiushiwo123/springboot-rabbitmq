package com.whb.service.impl;

import com.whb.rpc.RpcProxy;
import com.whb.service.IDepositService;
import com.whb.service.IOrderService;
import com.whb.vo.GoodTransferVo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;


@Service
@Qualifier("rpc")
public class RpcOrderService implements IOrderService {
    private String ip = "127.0.0.1";
    private int port = 8880;
    @Override
    public void order(GoodTransferVo vo) {
        IDepositService service = RpcProxy.getRmoteProxyObj(IDepositService.class,
                new InetSocketAddress(ip,port));
        int newValue = service.Inventory(vo);
    }
}
