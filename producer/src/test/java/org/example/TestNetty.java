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
import org.example.ClientHandler;

/**
 * Netty客户端的程序
 * @author huangjianfei
 */
public class TestNetty {
    /*IP地址*/
    static final String HOST = System.getProperty("host", "127.0.0.1");
    /*端口号*/
    static final int PORT = 4444;
    public static void main(String[] args) throws Exception {
        EventLoopGroup workgroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();//客户端
        b.group(workgroup)
                .channel(NioSocketChannel.class)//客户端 -->NioSocketChannel
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {//handler
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });
        //创建异步连接 可添加多个端口
        ChannelFuture cf1 = b.connect(HOST, PORT).sync();

        //buf
        //client向server端发送数据  Buffer形式
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty".getBytes()));
        cf1.channel().closeFuture().sync();

        workgroup.shutdownGracefully();
    }
}