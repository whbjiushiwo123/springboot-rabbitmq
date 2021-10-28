package com.whb.service;

import com.whb.vo.GoodTransferVo;

public interface INettyClientService {
    String rpcServer(GoodTransferVo vo) throws InterruptedException;
}
