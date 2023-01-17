package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

import static indi.wirsnow.chatroom.util.ChatUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/8 23:41
 * @description : 客户端响应
 */
public class ChatClientThread {
    private static final ExecutorService threadPool = new ThreadPoolExecutor(
            2,
            4,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("线程池已满，拒绝连接"));
    private final ChatUniversalData chatUniversalData;


    public ChatClientThread(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
        connect();
    }

    private void connect() {
        String ip = chatUniversalData.getIpField().getText();
        int port = Integer.parseInt(chatUniversalData.getPortField().getText());
        String userName = chatUniversalData.getUserName();

        threadPool.execute(() -> {
            try (Socket socket = new Socket(ip, port)) {
                chatUniversalData.oos = new ObjectOutputStream(socket.getOutputStream());
                chatUniversalData.ois = new ObjectInputStream(socket.getInputStream());
                chatUniversalData.setConnected(true);
                chatUniversalData.setSocket(socket);
                chatUniversalData.oos.writeUTF(userName);
                chatUniversalData.oos.flush();
                flushUserList(chatUniversalData);
                // 多线程运行ChatClientMessageIO
                threadPool.execute(() -> new ChatClientMessageIO(chatUniversalData));
            } catch (Exception e) {
                chatUniversalData.setConnected(false);
                JOptionPane.showMessageDialog(null, "连接失败\n" + e, "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });

    }
}
