package com.csx.codec.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018/01/18
 */
public class TestSubscribeReqProto {
    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body)
            throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq
                .newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("csx");
        builder.setProductName("test");
        builder.setPhoneNumber("123456");
        builder.setAddress("sz");
        return builder.build();
    }

    /**
     * @param args
     * @throws InvalidProtocolBufferException
     */
    public static void main(String[] args)
            throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode : " + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("After decode : " + req.toString());
        System.out.println("Assert equal : --> " + req2.equals(req));

    }
}
