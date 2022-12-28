package indi.wirsnow.client;

import indi.wirsnow.swingui.ChatClientFrame;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author : wirsnow
 * @date : 2022/12/7 11:04
 * @description : 客户端程序启动类
 */
public class ChatClientAPP {
    /*
    1、启动ui界面
    2、尝试连接服务端
    3、正常连接后，启动接收线程
    */
    private static Socket socket;
    private static String sender;
    private static boolean connected = false;
    private static ObjectOutputStream objectOutputStream;

    public static void main(String[] args) {
        sender = "wirsnow";
        new ChatClientFrame(sender);

        connect();
        if (connected) {
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                new Thread(new ChatClientListen(socket)).start();
                new Thread(new ChatClientOutput(sender,objectOutputStream)).start();
                new Thread(new ChatClientHeart(objectOutputStream)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static void connect() {
        try {
            socket = new Socket("127.0.0.1", 56448);
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
            connected = false;
        }
    }
}
