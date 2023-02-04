package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author : wirsnow
 * @date : 2023/1/19 22:56
 * @description : 客户端发送消息类
 */
public class ClientMessageOutput {

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
        Socket socket = chatUniversalData.getSocket();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        writer.println(toUserName + "-to:audi://" + fileName + "-name:" + base64);
    }

    /**
     * 发送截图
     *
     * @param chatUniversalData 数据类
     * @param toUserName        接收者
     * @param fileName          文件名
     * @param base64            base64编码
     * @throws IOException IO异常
     */
    public void sendScreen(ChatUniversalData chatUniversalData, String toUserName, String fileName, String base64) throws IOException {
        Socket socket = chatUniversalData.getSocket();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        writer.println(toUserName + "-to:icon://" + fileName + "-name:" + base64);
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
        Socket socket = chatUniversalData.getSocket();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        writer.println(toUserName + "-to:file://" + fileName + "-name:" + base64);
    }

    /**
     * 发送下线消息
     *
     * @param chatUniversalData 数据类
     * @throws IOException IO异常
     */
    public void sendDisconnectMessage(ChatUniversalData chatUniversalData) throws IOException {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(chatUniversalData.getSocket().getOutputStream()), true);

        out.println(chatUniversalData.getUserName() + "-to:LogOut");
    }

    /**
     * 发送文本消息
     *
     * @param chatUniversalData 数据类
     * @param toUserName        接收者
     * @param message           消息
     * @throws IOException IO异常
     */
    public void sendText(ChatUniversalData chatUniversalData, String toUserName, String message) throws IOException {
        // 获取输入流并拆分消息
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(chatUniversalData.getSocket().getOutputStream()), true);

        String[] strs = message.split("\n");
        // 如果消息为单行，不处理；如果消息为多行，则拆分为多行重新发送
        if (strs.length <= 1) {
            writer.println(toUserName + "-to:" + "text://" + message);
        } else {
            writer.println(toUserName + "-to:texs://☩");  // 用于标记多行消息的开始
            for (String string : strs) {
                switch (string) {
                    case "☩", "❊" -> string = "□";  // 替换特殊字符
                    case "\n", "\r", "" -> string = "❊";    // 用于标记多行消息的换行
                }
                writer.println(toUserName + "-to:texs://" + string + "\n");
            }
        }
    }
}
