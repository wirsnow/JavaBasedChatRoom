package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.appendAndFlush;
import static indi.wirsnow.chatroom.util.ChatUniversalUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/1 14:03
 * @description : 服务端多线程启动器
 */
public class ServerThreadStart {
    private final ExecutorService threadPool = new ThreadPoolExecutor(
            5,
            21,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("线程池已满，拒绝连接"));
    private final ChatUniversalData chatUniversalData;

    public ServerThreadStart(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
        connect();
    }

    private void connect() {
        JTextArea messageArea = chatUniversalData.getMessageArea();
        int port = Integer.parseInt(chatUniversalData.getPortField().getText());
        threadPool.execute(() -> {
            // 创建服务器端ServerSocket，指定绑定的端口，并监听此端口
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                chatUniversalData.setConnected(true);
                flushUserList(chatUniversalData);       // 刷新用户列表
                appendAndFlush(messageArea, "服务器启动成功，等待客户端连接...\n");
                threadPool.execute(() -> disconnect(serverSocket));
                // 调用accept()方法开始监听，等待客户端的连接
                while (chatUniversalData.getConnected()) {
                    try {
                        Socket socket = serverSocket.accept();
                        chatUniversalData.setSocket(socket);
                        threadPool.execute(new ServerMessageInput(socket, chatUniversalData));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                chatUniversalData.setConnected(false);
                JOptionPane.showMessageDialog(null, "端口号已被占用" + e, "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private void disconnect(ServerSocket serverSocket){
        // 关闭所有连接
        while (chatUniversalData.getConnected()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            for (Map.Entry<String, Socket> entry : chatUniversalData.getAllOnlineUser().entrySet()) {
                entry.getValue().close();
            }
            serverSocket.close();
            JOptionPane.showMessageDialog(null, "已断开与网络的连接", "提示", JOptionPane.INFORMATION_MESSAGE);
            threadPool.shutdownNow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

