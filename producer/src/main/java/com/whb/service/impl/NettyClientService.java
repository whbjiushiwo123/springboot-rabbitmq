package com.whb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.whb.service.INettyClientService;
import com.whb.vo.GoodTransferVo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class NettyClientService implements INettyClientService {
    @Value("${netty_port}")
    private int port;
    @Value("${netty_address}")
    private String address;
    @Override
    public String rpcServer(GoodTransferVo vo) throws InterruptedException {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ClientHandler());
                    }
                });
        ChannelFuture future =  bootstrap.connect(address,port).sync();
        future.channel().writeAndFlush(Unpooled.copiedBuffer(JSONObject.toJSONString(vo).getBytes(StandardCharsets.UTF_8)));
        future.channel().closeFuture().sync();
        return "null";
    }

}
