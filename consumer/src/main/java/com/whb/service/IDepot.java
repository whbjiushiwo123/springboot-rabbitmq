package com.whb.service;

public interface IDepot {
    int inDepot(String goodsId, int changeAmount);

    int outDepot(String goodsId, int changeAmount);
}
