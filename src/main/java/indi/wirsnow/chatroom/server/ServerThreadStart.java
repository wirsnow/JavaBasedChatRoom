package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.flushUserList;
import static indi.wirsnow.chatroom.util.ChatUniversalUtil.messageInsertText;

/**
 * @author : wirsnow
 * @date : 2023/1/1 14:03
 * @description : 服务端多线程启动器
 */
public class ServerThreadStart {
    // 线程池
    private final ExecutorService threadPool = new ThreadPoolExecutor(
            5,
            21,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("线程池已满，拒绝连接"));
    private final ChatUniversalData chatUniversalData;

    /**
     * 构造方法
     *
     * @param chatUniversalData 数据类
     */
    public ServerThreadStart(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
        connect();
    }

    /**
     * 连接方法
     */
    private void connect() {
        // 获取端口号
        int port = Integer.parseInt(chatUniversalData.getPortField().getText());

        threadPool.execute(() -> {
            // 创建服务器端ServerSocket，指定绑定的端口，并监听此端口
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                // 将数据传入数据类并刷新用户列表
                chatUniversalData.setConnected(true);
                flushUserList(chatUniversalData);
                // 插入提示信息
                messageInsertText(chatUniversalData.getMessagePane(), "服务器启动成功，等待客户端连接...\n");
                // 启动监听线程
                threadPool.execute(() -> disconnect(serverSocket));
                // 调用accept()方法开始监听，等待客户端的连接
                while (chatUniversalData.getConnected()) {
                    try {
                        Socket socket = serverSocket.accept();
                        chatUniversalData.setSocket(socket);
                        // 启动接收消息线程
                        threadPool.execute(new ServerMessageInput(socket, chatUniversalData));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                // 如果报错，说明端口号已被占用
                chatUniversalData.setConnected(false);
                JOptionPane.showMessageDialog(null, "端口号已被占用" + e, "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * 断开连接方法
     *
     * @param serverSocket 服务器端ServerSocket
     */
    private void disconnect(ServerSocket serverSocket) {
        // 等待连接断开
        while (chatUniversalData.getConnected()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // 关闭所有连接
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

