package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.*;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/8 23:41
 * @description : 客户端多线程启动器
 */
public class ClientThreadStart {
    private final ExecutorService threadPool = new ThreadPoolExecutor(
            2,
            5,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("线程池已满，拒绝连接"));
    private final ChatUniversalData chatUniversalData;


    public ClientThreadStart(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
        connect();
    }

    private void connect() {
        String ip = chatUniversalData.getIpField().getText();
        int port = Integer.parseInt(chatUniversalData.getPortField().getText());

        threadPool.execute(() -> {
            try (Socket socket = new Socket(ip, port)) {
                chatUniversalData.setConnected(true);
                chatUniversalData.setSocket(socket);
                flushUserList(chatUniversalData);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                writer.println("Server-MyUserName-to:" + chatUniversalData.getUserName());
                // 断开与服务器的连接
                threadPool.execute(() -> disconnect(socket));
                // 运行ClientMessageInput
                new ClientMessageInput(socket, chatUniversalData);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "连接失败\n" + e, "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    private void disconnect(Socket socket) {
        while (chatUniversalData.getConnected()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            socket.close();
            JOptionPane.showMessageDialog(null, "与服务器断开连接", "提示", JOptionPane.INFORMATION_MESSAGE);
            threadPool.shutdownNow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
