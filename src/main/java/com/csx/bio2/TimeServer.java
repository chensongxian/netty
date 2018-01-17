package com.csx.bio2;

import com.csx.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018-01-17
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(
                    50, 10000);// 创建IO任务线程池
            while (true) {
                socket = server.accept();
                singleExecutor.exec(new TimeServerHandler(socket));
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (server != null) {
                try {
                    System.out.println("The time server close");
                    server.close();
                    server = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
