package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

import static indi.wirsnow.chatroom.util.ChatMessageParse.parseMessage;
import static indi.wirsnow.chatroom.util.ChatUniversalUtil.messageInsertText;

/**
 * @author : wirsnow
 * @date : 2023/1/15 14:34
 * @description : 客户端接收消息类
 */
public class ClientMessageInput {
    private final Socket socket;    // 客户端socket
    private final ChatUniversalData chatUniversalData;  // 数据

    /**
     * 构造方法
     *
     * @param socket            客户端socket
     * @param chatUniversalData 数据类
     */
    public ClientMessageInput(Socket socket, ChatUniversalData chatUniversalData) {
        this.socket = socket;
        this.chatUniversalData = chatUniversalData;
        // 监听客户端消息输入
        ChatClientMessageInput();
    }

    /**
     * 监听客户端消息输入
     */
    public void ChatClientMessageInput() {
        String message;
        BufferedReader reader;
        try {
            // 获取输入流
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 如果连接状态为true,则一直监听
            while (chatUniversalData.getConnected()) {
                message = reader.readLine();
                // 如果消息为空或者消息为空字符串，则跳过
                if (message == null || Objects.equals(message.strip(), "")) {
                    continue;
                }
                // 解析并插入消息到消息面板
                message = parseMessage(chatUniversalData, message);
                messageInsertText(chatUniversalData.getMessagePane(), message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}