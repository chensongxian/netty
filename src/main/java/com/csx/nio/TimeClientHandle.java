package com.csx.nio;

import com.sun.deploy.panel.SecurityLevel;
import com.sun.org.apache.regexp.internal.RE;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018/01/15
 */
public class TimeClientHandle implements Runnable{
   private String host;
   private int port;
   private Selector selector;
   private SocketChannel socketChannel;
   private ByteBuffer readBuffer=ByteBuffer.allocate(1024);

   private volatile boolean stop=false;

    public TimeClientHandle(String host, int port) {

        try {
            this.host = host==null?"127.0.0.1":host;
            this.port = port;
            selector=Selector.open();
            socketChannel=SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void stop(){
        this.stop=true;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!stop){
            try {
                selector.select(1000);
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            SocketChannel sc= (SocketChannel) key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }else{
                    System.exit(-1);
                }
            }
            if(key.isReadable()){
                this.readBuffer.clear();
                int count=sc.read(readBuffer);
                if(count==-1){
                    sc.close();
                    key.cancel();
                    return;
                }
                this.readBuffer.flip();
                byte[] bytes=new byte[this.readBuffer.remaining()];
                this.readBuffer.get(bytes);
                String body=new String(bytes,"utf-8");
                System.out.println("body:"+body);
                stop();
            }
        }
    }



    private void doConnect() throws IOException {
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else{
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] req="query time order".getBytes();
        ByteBuffer byteBuffer=ByteBuffer.allocate(req.length);

        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        if(!byteBuffer.hasRemaining()){
            System.out.println("send order 2 server success!");
        }
    }

    public static void main(String[] args) {
        TimeClientHandle clientHandle=new TimeClientHandle("127.0.0.1",8763);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(clientHandle);

    }
}
