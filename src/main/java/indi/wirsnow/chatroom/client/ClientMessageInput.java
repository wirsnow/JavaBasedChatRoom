package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

import static indi.wirsnow.chatroom.util.ChatMessageParse.parseMessage;
import static indi.wirsnow.chatroom.util.ChatUniversalUtil.appendAndFlush;

/**
 * @author : wirsnow
 * @date : 2023/1/15 14:34
 * @description : 客户端接收消息类
 */
public class ClientMessageInput {
    private final Socket socket;
    private final ChatUniversalData chatUniversalData;

    public ClientMessageInput(Socket socket, ChatUniversalData chatUniversalData) {
        this.socket = socket;
        this.chatUniversalData = chatUniversalData;

        ChatClientMessageInput();
    }

    /**
     * 监听客户端消息输入
     */

    public void ChatClientMessageInput() {
        String message;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (chatUniversalData.getConnected()) {
                message = reader.readLine();
                if (message == null || Objects.equals(message.strip(), "")) {
                    continue;
                }
                System.out.println("收到消息：" + message);
                message = parseMessage(chatUniversalData, message);
                appendAndFlush(chatUniversalData.getMessageArea(), message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}