package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

import static indi.wirsnow.chatroom.util.ChatUtil.appendAndFlush;
import static indi.wirsnow.chatroom.util.ChatUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/1 14:03
 * @description : 服务端线程类
 */
public class ChatServerThread {
    private static final ExecutorService threadPool = new ThreadPoolExecutor(
            2,
            21,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("线程池已满，拒绝连接"));
    private final ChatUniversalData chatUniversalData;

    public ChatServerThread(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
        connect();
    }

    public void connect() {
        JTextArea messageArea = chatUniversalData.getMessageArea();
        int port = Integer.parseInt(chatUniversalData.getPortField().getText());
        Map<String, Socket> allOnlineUser = chatUniversalData.getAllOnlineUser();
        threadPool.execute(() -> {
            // 创建服务器端ServerSocket，指定绑定的端口，并监听此端口
            try (ServerSocket serverSocket = new ServerSocket(port)) {

                System.out.println("服务器启动成功");
                chatUniversalData.setConnected(true);
                appendAndFlush(messageArea, "服务器启动成功，等待客户端连接...\n");
                // 调用accept()方法开始监听，等待客户端的连接
                while (chatUniversalData.getConnected()) {
                    try {
                        Thread.sleep(10);   //  休眠10ms，防止CPU占用过高
                        Socket socket = serverSocket.accept();
                        chatUniversalData.ois = new ObjectInputStream(socket.getInputStream());
                        chatUniversalData.oos = new ObjectOutputStream(socket.getOutputStream());
                        String userName = chatUniversalData.ois.readUTF();
                        System.out.println("客户端" + userName + "连接成功");
                        // 刷新在线列表
                        allOnlineUser.put(userName, socket);
                        flushUserList(chatUniversalData);
                        System.out.println(chatUniversalData.getAllOnlineUser());
                        Thread.sleep(100);
                        // 把allOnlineUser发送给所有客户端
                        chatUniversalData.oos.writeUTF("Server-from:list://" + allOnlineUser);
                        // 为每个客户端创建一个线程
                        for (Map.Entry<String, Socket> entry : allOnlineUser.entrySet()) {
                            if (entry.getValue().isConnected() && !Objects.equals(entry.getKey(), userName)) {
                                ObjectInputStream oisTemp = new ObjectInputStream(entry.getValue().getInputStream());
                                ObjectOutputStream oosTemp = new ObjectOutputStream(entry.getValue().getOutputStream());
                                oosTemp.writeUTF("Server-from:list://" + allOnlineUser);
                                oosTemp.flush();
                                oisTemp.close();
                                oosTemp.close();
                                System.out.println("已发送给" + entry.getKey());
                            }
                        }
                        // 向所有人发送上线消息
                        chatUniversalData.setSocket(socket);
                        appendAndFlush(messageArea, "客户端" + socket.getInetAddress().getHostAddress() + "连接成功\n");
                        // 启动转发线程
                        threadPool.execute(new ServerForwardMessage(chatUniversalData));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                chatUniversalData.setConnected(false);
                JOptionPane.showMessageDialog(null, "端口号已被占用", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

