package com.csx.codec.msgpack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.msgpack.MsgpackDecoder;
import io.netty.handler.codec.msgpack.MsgpackEncoder;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018/01/18
 */
public class EchoClient {

    private final int sendNumber=0;

    private void connect(String host,int port){
        EventLoopGroup group=new NioEventLoopGroup();


        try {
            Bootstrap b=new Bootstrap();

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //报文长度解码器
                            socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535, 0, 2,0,2));
                            socketChannel.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
                            socketChannel.pipeline().addLast("frameEecoder",new LengthFieldPrepender(2));
                            socketChannel.pipeline().addLast("msgpack encoder",new MsgpackEncoder());
                            socketChannel.pipeline().addLast(new EchoClientHandler(100));
                        }
                    });

            ChannelFuture future = b.connect(new InetSocketAddress(host, port)).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port=8080;
        new EchoClient().connect("127.0.0.1",port);
    }
}
