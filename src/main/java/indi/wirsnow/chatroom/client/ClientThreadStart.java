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
    // 线程池
    private final ExecutorService threadPool = new ThreadPoolExecutor(
            2,
            5,
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
    public ClientThreadStart(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
        connect();
    }

    /**
     * 连接方法
     */
    private void connect() {
        // 获取ip与端口号
        String ip = chatUniversalData.getIpField().getText();
        int port = Integer.parseInt(chatUniversalData.getPortField().getText());
        // 创建客户端Socket，指定服务器地址和端口
        threadPool.execute(() -> {
            try (Socket socket = new Socket(ip, port)) {
                // 将数据传入数据类并刷新用户列表
                chatUniversalData.setConnected(true);
                chatUniversalData.setSocket(socket);
                flushUserList(chatUniversalData);
                // 向服务器发送用户名
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                writer.println("Server-MyUserName-to:" + chatUniversalData.getUserName());
                // 启动监听线程
                threadPool.execute(() -> disconnect(socket));
                // 运行客户端接收消息类
                new ClientMessageInput(socket, chatUniversalData);
            } catch (Exception e) {
                // 连接失败
                JOptionPane.showMessageDialog(null, "连接失败\n请检查服务器状态", "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }


    /**
     * 断开连接方法
     *
     * @param socket socket
     */
    private void disconnect(Socket socket) {
        // 等待连接断开
        while (chatUniversalData.getConnected()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // 关闭socket
        try {
            socket.close();
            JOptionPane.showMessageDialog(null, "与服务器断开连接", "提示", JOptionPane.INFORMATION_MESSAGE);
            threadPool.shutdownNow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
