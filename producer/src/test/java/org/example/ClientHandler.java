package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
public class ClientHandler extends SimpleChannelInboundHandler<String>{
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {

    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        try{
            //do something
            //接收服务端发来的数据 ByteBuf
            ByteBuf  buf = (ByteBuf)msg;
            //创建一个和buf一样长度的字节空数组
            byte[] data = new byte[buf.readableBytes()];
            //将buf中的数据读取到data数组中
            buf.readBytes(data);
            //将data数组惊醒包装 以String格式输出
            String response = new String(data,"utf-8");
            System.out.println("client :"+response);
            ctx.close();
        }finally{
            // Discard the received data silently.
            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerObject(ChannelHandlerContext ctx, Object msg) {

        Student student = (Student)msg;
        System.err.println("server 获取信息："+student.getId()+student.getName());
    }


    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.err.println("客户端读取数据完毕");
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("client 读取数据出现异常");
        ctx.close();
    }
}