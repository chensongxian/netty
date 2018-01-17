package com.csx.bio;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * BIO传统同步阻塞IO，服务器端
 * @Author: csx
 * @Date: 2018-01-17
 */
public class TimerServer {
    public static void main(String[] args) {
        int port=8080;

        ServerSocket server=null;

        try {
            server=new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket=null;

            while (true){
                socket=server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }


        } catch (IOException e) {
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
