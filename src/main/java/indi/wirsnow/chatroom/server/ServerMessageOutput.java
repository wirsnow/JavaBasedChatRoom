package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

/**
 * @author : wirsnow
 * @date : 2023/1/17 21:28
 * @description : 服务端发送消息类
 */
public class ServerMessageOutput {
    /**
     * 发送录音
     *
     * @param chatUniversalData 数据类
     * @param toUserName        接收者
     * @param fileName          文件名
     * @param base64            base64编码
     * @throws IOException IO异常
     */
    public void sendAudio(ChatUniversalData chatUniversalData, String toUserName, String fileName, String base64) throws IOException {
        if (Objects.equals(toUserName, "所有人")) {
            // 发送给所有人
            for (String userName : chatUniversalData.getAllOnlineUser().keySet()) {
                Socket socket = chatUniversalData.getAllOnlineUser().get(userName);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                writer.println("Server-from:audi://" + fileName + "-name:" + base64);
            }
        } else {
            Socket socket = chatUniversalData.getAllOnlineUser().get(toUserName);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            writer.println("Server-from:audi://" + fileName + "-name:" + base64);
        }
    }

    /**
     * 发送图片
     *
     * @param chatUniversalData 数据类
     * @param toUserName        接收者
     * @param fileName          文件名
     * @param base64            base64编码
     * @throws IOException IO异常
     */
    public void sendScreen(ChatUniversalData chatUniversalData, String toUserName, String fileName, String base64) throws IOException {
        if (Objects.equals(toUserName, "所有人")) {
            // 发送给所有人
            for (String userName : chatUniversalData.getAllOnlineUser().keySet()) {
                Socket socket = chatUniversalData.getAllOnlineUser().get(userName);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                writer.println("Server-from:icon://" + fileName + "-name:" + base64);
            }
        } else {
            Socket socket = chatUniversalData.getAllOnlineUser().get(toUserName);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            writer.println("Server-from:icon://" + fileName + "-name:" + base64);
        }
    }

    /**
     * 发送文件
     *
     * @param chatUniversalData 数据类
     * @param toUserName        接收者
     * @param fileName          文件名
     * @param base64            base64编码
     * @throws IOException IO异常
     */
    public void sendFile(ChatUniversalData chatUniversalData, String toUserName, String fileName, String base64) throws IOException {
        if (Objects.equals(toUserName, "所有人")) {
            // 发送给所有人
            for (String userName : chatUniversalData.getAllOnlineUser().keySet()) {
                Socket socket = chatUniversalData.getAllOnlineUser().get(userName);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                writer.println("Server-from:file://" + fileName + "-name:" + base64);
            }
        } else {
            Socket socket = chatUniversalData.getAllOnlineUser().get(toUserName);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            writer.println("Server-from:file://" + fileName + "-name:" + base64);
        }
    }

    /**
     * 发送消息
     *
     * @param chatUniversalData 数据类
     * @param toUserName        接收者
     * @param message           消息
     * @throws IOException IO异常
     */
    public void sendText(ChatUniversalData chatUniversalData, String toUserName, String message) throws IOException {
        if (Objects.equals(toUserName, "所有人")) {
            // 发送给所有人
            for (String userName : chatUniversalData.getAllOnlineUser().keySet()) {
                Socket socket = chatUniversalData.getAllOnlineUser().get(userName);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                // 修改消息并发送
                messageModify(writer, message);
            }
        } else {
            Socket socket = chatUniversalData.getAllOnlineUser().get(toUserName);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            // 修改消息并发送
            messageModify(writer, message);
        }
    }

    /**
     * 发送下线消息
     *
     * @param chatUniversalData 数据类
     * @throws IOException IO异常
     */
    public void sendDisconnectMessage(ChatUniversalData chatUniversalData) throws IOException {
        // 向所有人发送
        for (String name : chatUniversalData.getAllOnlineUser().keySet()) {
            Socket socket = chatUniversalData.getAllOnlineUser().get(name);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            writer.println("Server-from:exit://Server");
        }
    }

    /**
     * 用于文本消息的多行与单行发送
     *
     * @param writer  输出流
     * @param message 消息
     */
    private void messageModify(PrintWriter writer, String message) {
        String[] strs = message.split("\n");
        // 如果消息为单行，不处理；如果消息为多行，则拆分为多行重新发送
        if (strs.length <= 1) {
            writer.println("Server-from:text://" + message);
        } else {
            writer.println("Server-from:texs://☩"); // 用于标记多行消息的开始
            for (String string : strs) {
                switch (string) {
                    case "☩", "❊" -> string = "□";  // 替换特殊字符
                    case "\n", "\r", "" -> string = "❊";    // 用于标记多行消息的换行
                }
                writer.println("Server-from:texs://" + string + "\n");
            }
        }
    }


}

