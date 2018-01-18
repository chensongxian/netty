package com.csx.codec.msgpack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018/01/18
 */
public class EchoClientHandler extends ChannelHandlerAdapter{

    private int counter;

    private final int sendNumber;


    public EchoClientHandler(int sendNumber) {
        this.sendNumber=sendNumber;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client receive the msgpack message : "+msg+" count : "+(++counter));
        ctx.write(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] userInfos = userInfos();
        for(UserInfo userInfo:userInfos){
            ctx.write(userInfo);
        }
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private UserInfo[] userInfos(){
        UserInfo[] userInfos=new UserInfo[sendNumber];
        UserInfo userInfo=null;
        for(int i=0;i<userInfos.length;i++){
            userInfo=new UserInfo();
            userInfo.setName("ABCDEFG--->"+i);
            userInfo.setAge(i);
            userInfos[i]=userInfo;
        }
        return userInfos;
    }
}
