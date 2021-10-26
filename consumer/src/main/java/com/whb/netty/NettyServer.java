package com.whb.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class NettyServer implements Runnable{
    @Value("${netty_port}")
    private int port;
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Thread nServer;
    @Autowired
    private NettyServerInitializer nettyServerInitializer;

    @PostConstruct
    public void init(){
        nServer = new Thread(this);
        nServer.start();
    }
    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();
        System.out.println(Thread.currentThread().getName() + "----位置4");
        ServerBootstrap bootstrap = new ServerBootstrap();
        try{
            bootstrap.group(bossGroup,workGroup);
            bootstrap.option(ChannelOption.SO_BACKLOG,1024);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(nettyServerInitializer);

            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
            channel = f.channel();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    public void destory(){
        System.out.println("destroy server resources");
        if(null == channel){
            System.out.println("server channel is null");
        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        bossGroup = null;
        workGroup = null;
        channel = null;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
