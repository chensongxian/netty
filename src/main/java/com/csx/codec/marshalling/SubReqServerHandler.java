package com.csx.codec.marshalling;

import com.csx.codec.pojo.SubscribeReq;
import com.csx.codec.pojo.SubscribeResp;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018-01-21
 */
public class SubReqServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq req = (SubscribeReq) msg;
        System.out.println(req);
        if ("csx".equalsIgnoreCase(req.getUserName())) {
            System.out.println("Service accept client subscrib req : ["
                    + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }



    private SubscribeResp resp(int subReqID) {
        SubscribeResp resp = new SubscribeResp();
        resp.setSubReqID(subReqID);
        resp.setRespCode(0);
        resp.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }
}
