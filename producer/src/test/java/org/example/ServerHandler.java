package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        simpleRead(ctx,msg);
    }

    private void simpleRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf bb = (ByteBuf) msg;
        byte[] reqByte = new byte[bb.readableBytes()];
        bb.readBytes(reqByte);
        String reqStr = new String(reqByte);
        System.err.println("server 接收到客户端的请求： " + reqStr);
        String respStr = new StringBuilder("来自服务器的响应").append(reqStr).append("$_").toString();
        ctx.writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes()));
    }
    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.err.println("服务端读取数据完毕");
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("server 读取数据出现异常");
        ctx.close();
    }

    /**
     * 将请求信息直接转成对象
     * @param ctx
     * @param msg
     */
    private void handlerObject(ChannelHandlerContext ctx, Object msg) {
        // 需要序列化 直接把msg转成对象信息，一般不会用，可以用json字符串在不同语言中传递信息
        Student student = (Student)msg;
        System.err.println("server 获取信息："+student.getId()+student.getName());
        student.setName("李四");
        ctx.write(student);
    }

}
