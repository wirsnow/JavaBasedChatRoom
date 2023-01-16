package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

import static indi.wirsnow.chatroom.util.ChatUtil.appendAndFlush;
import static indi.wirsnow.chatroom.util.ChatUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/1 14:03
 * @description : 服务端线程类
 */
public class ChatServerThread {
    private final ChatUniversalData chatUniversalData;
    private static final ExecutorService threadPool = new ThreadPoolExecutor(
            2,
            21,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("线程池已满，拒绝连接"));

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
                ObjectOutputStream oos;
                ObjectInputStream ois;
                System.out.println("服务器启动成功");
                chatUniversalData.setConnected(true);
                appendAndFlush(messageArea, "服务器启动成功，等待客户端连接...\n");
                // 调用accept()方法开始监听，等待客户端的连接
                while (chatUniversalData.getConnected()) {
                    try {
                        Thread.sleep(10);   //  休眠10ms，防止CPU占用过高
                        Socket socket = serverSocket.accept();
                        ois = new ObjectInputStream(socket.getInputStream());
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        String userName = ois.readUTF();
                        System.out.println("客户端" + userName + "连接成功");
                        // 刷新在线列表
                        allOnlineUser.put(userName, socket);
                        flushUserList(chatUniversalData);
                        System.out.println(chatUniversalData.getAllOnlineUser());

                        // 向所有人发送上线消息
                        for(Map.Entry<String, Socket> entry : allOnlineUser.entrySet()) {
                            ois = new ObjectInputStream(entry.getValue().getInputStream());
                            oos = new ObjectOutputStream(entry.getValue().getOutputStream());
                            oos.writeObject("Server-from:list://" + allOnlineUser);
                            oos.flush();
                        }

                        chatUniversalData.setSocket(socket);
                        appendAndFlush(messageArea, "客户端" + socket.getInetAddress().getHostAddress() + "连接成功\n");
                        // 启动转发线程
                        threadPool.execute(new ServerForwardMessage(chatUniversalData));
                    } catch (Exception ignored) {}
                }
            } catch (Exception e) {
                chatUniversalData.setConnected(false);
                JOptionPane.showMessageDialog(null, "端口号已被占用", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

