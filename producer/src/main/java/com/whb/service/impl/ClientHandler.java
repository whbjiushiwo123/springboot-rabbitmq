package com.whb.service.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelHandler.Sharable;
@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>    {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks", CharsetUtil.UTF_8));
        //当被通知Channel是活跃的时候，发送一条消息
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println(    //  记录已接收消息的转储
        "Client received: " + in.toString(CharsetUtil.UTF_8));
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, //在发生异常时，记录错误并关闭Channel
            Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
