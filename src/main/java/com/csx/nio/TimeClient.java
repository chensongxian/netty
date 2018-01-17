package com.csx.nio;


/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018-01-17
 */
public class TimeClient {
    public static void main(String[] args) {
        int port=8080;
        new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001")
                .start();
    }
}
