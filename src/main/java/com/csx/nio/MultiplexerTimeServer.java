package com.csx.nio;

import com.sun.org.apache.bcel.internal.generic.Select;
import sun.org.mozilla.javascript.internal.WrappedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018/01/15
 */
public class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel servChannel;
    private volatile boolean stop = false;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);


    /**
     * 初始化多路复用器，绑定监听端口
     *
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            servChannel = ServerSocketChannel.open();
            servChannel.configureBlocking(false);
            servChannel.socket().bind(new InetSocketAddress(port), 1024);
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port:" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handInput(key);
                    }catch (Exception e){
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }
        //路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //处理新接入的请求消息
            if (key.isAcceptable()) {
                this.accept(key);
            }
            if (key.isReadable()) {
                this.read(key);
            }
        }
    }

    private void read(SelectionKey key) {
        try {
            //1 清空缓冲区旧的数据
            this.readBuffer.clear();
            //2 获取之前注册的socket通道对象
            SocketChannel sc = (SocketChannel) key.channel();
            //3 读取数据
            int count = sc.read(this.readBuffer);
            //4 如果没有数据
            if (count == -1) {
                key.cancel();
                sc.close();
                return;
            }
            //5 有数据则进行读取 读取之前需要进行复位方法(把position 和limit进行复位)
            this.readBuffer.flip();
            //6 根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
            byte[] bytes = new byte[this.readBuffer.remaining()];
            //7 接收缓冲区数据
            this.readBuffer.get(bytes);
            //8 打印结果
            String body = new String(bytes,"UTF-8").trim();
            System.out.println("The time server receive order : "
                    + body);

            String currentTime = "QUERY TIME ORDER"
                    .equalsIgnoreCase(body) ? new java.util.Date(
                    System.currentTimeMillis()).toString()
                    : "BAD ORDER";
            this.write(sc,currentTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(SocketChannel sc,String response) throws IOException {
        if(response!=null&&response.trim().length()>0){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer=ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            sc.write(writeBuffer);
        }
    }

    private void accept(SelectionKey key) throws IOException {
        //1 获取服务通道
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        //2 执行阻塞方法
        SocketChannel sc = ssc.accept();
        //3 设置阻塞模式
        sc.configureBlocking(false);
        //4 注册到多路复用器上，并设置读取标识
        sc.register(this.selector, SelectionKey.OP_READ);
    }

}
