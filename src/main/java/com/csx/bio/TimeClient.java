package com.csx.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * BIO传统同步阻塞IO，客户端
 * @Author: csx
 * @Date: 2018-01-17
 */
public class TimeClient {
    public static void main(String[] args) {
        int port=8080;
        String host="127.0.0.1";

        Socket socket=null;
        BufferedReader in=null;
        PrintWriter out=null;

        try {
            socket=new Socket(host,port);
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream(),true);

            out.println("QUERY TIME ORDER");
            System.out.println("Send order 2 server succeed.");
            String resp = in.readLine();
            System.out.println("Now is : " + resp);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(out!=null){
                out.close();
                out=null;
            }

            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket=null;
            }
        }
    }
}
