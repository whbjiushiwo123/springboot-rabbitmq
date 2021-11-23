package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Constant;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class ClientNetty {
    // 要请求的服务器的ip地址
    private String ip;
    // 服务器的端口
    private int port;

    public ClientNetty(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    private void action(){
        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();
        try{
            bs.group(workGroup)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new ClientHandler());
                        }
                    });

            String reqStr = "我是客户端请求1$_"+new Date();
           ChannelFuture cf = bs.connect(ip,port).sync();
           while(true){
               Thread.sleep(2000);
               cf.channel().writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes("UTF-8")));
           }
//           cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        new ClientNetty("127.0.0.1", 8811).action();
    }
}
