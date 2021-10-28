package com.whb.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import io.netty.channel.ChannelHandler.Sharable;


@Sharable
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    @Autowired(required = false)
    @Qualifier("closeFutureHandler")
    public Handler closeFutureHandler;
    @Autowired(required = false)
    @Qualifier("exceptionFutureHandler")
    public Handler exceptionFutureHandler;
    @Autowired
    @Qualifier("businessFutureHandler")
    public Handler businessFutureHandler;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// 返回客户端消息 - 我已经接收到了你的消息
        System.out.println(Thread.currentThread().getName()+"----位置6");
        String retMsg = businessFutureHandler.hander(msg);
        ctx.writeAndFlush(retMsg);
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx){

    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx){

    }
    @Override
    public void channelActive(ChannelHandlerContext ctx){

    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx){

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable throwable){

    }
}
