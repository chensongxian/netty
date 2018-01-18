package io.netty.handler.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018/01/18
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//        System.out.println("解码");
//        int length=byteBuf.readableBytes();
//        byte[] array=new byte[length];
//
//        byteBuf.getBytes(byteBuf.readerIndex(),array,0,length);
//        MessagePack messagePack=new MessagePack();
//        list.add(messagePack.read(array));

        final byte[] array;
        final int length=byteBuf.readableBytes();
        array=new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array,0,length);
        MessagePack msgpack=new MessagePack();
        list.add(msgpack.read(array));
    }
}
