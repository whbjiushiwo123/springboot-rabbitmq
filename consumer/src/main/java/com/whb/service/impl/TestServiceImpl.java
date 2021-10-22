package com.whb.service.impl;

import com.whb.service.TestService;
import org.springframework.stereotype.Service;

@Service(value = "testService")
public class TestServiceImpl implements TestService {
    public String getName() {
        return "luoguohui";
    }
}
