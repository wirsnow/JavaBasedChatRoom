package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.swingui.ChatFrame;
import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;

import javax.swing.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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


    public static void main(String[] args) {
        JTextArea messageArea = new JTextArea();
        JTextArea editorArea = new JTextArea();
        Map<String, Socket> allOnlineUser = new HashMap<>();
        ChatFrameListener listener = new ChatFrameListener(allOnlineUser, messageArea, editorArea);
        // 启动ui界面
        SwingUtilities.invokeLater(() -> new ChatFrame("Client", messageArea, editorArea, listener));

    }
}
