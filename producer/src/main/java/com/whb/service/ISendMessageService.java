package com.whb.service;


import java.util.List;
import java.util.Set;

public interface ISendMessageService {
    public void sendMessage(String message);

    void sendMultiMessage(List<String> messages);
}
