package indi.wirsnow.chatroom.client;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

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

    // 线程池
    private static final ExecutorService threadPool = new ThreadPoolExecutor(
            2,
            5,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());
    private static Socket socket;
    private static String sender;
    private static boolean connected = false;

    public ChatClientAPP() {

    }


    public static void main(String[] args) throws IOException {
        sender = "wirsnow";
        connect();
        JTextArea messageArea;    // 聊天记录显示框
        JTextArea editorArea;     // 文字输入框

        if (connected) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            try {
                threadPool.submit(new ChatClientListen(ois));
            } catch (Exception e) {
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
