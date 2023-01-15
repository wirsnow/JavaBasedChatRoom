package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static indi.wirsnow.chatroom.util.ChatUtil.appendAndFlush;
import static indi.wirsnow.chatroom.util.ChatUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/15 14:34
 * @description : 监听客户端消息输入输出
 */
public class ChatClientMessageIO {
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private final ChatUniversalData chatUniversalData;

    public ChatClientMessageIO(ObjectOutputStream oos, ObjectInputStream ois, ChatUniversalData chatUniversalData) {
        this.oos = oos;
        this.ois = ois;
        this.chatUniversalData = chatUniversalData;
        ChatClientMessageInput();
    }

    /**
     * 监听客户端消息输入
     */
    public void ChatClientMessageInput() {
        while (true) {
            try {
                Thread.sleep(10);   //  休眠10ms，防止CPU占用过高
                String message = ois.readObject().toString();
                JTextArea messageArea = chatUniversalData.getMessageArea();
                String[] messageArray = message.split("-from:");
                String fromUserName = messageArray[0];
                StringBuilder messageContent = new StringBuilder();
                for (int i = 1; i < messageArray.length; i++) {
                    messageContent.append(messageArray[i]);
                }
                message = messageContent.toString();
                String command = message.substring(0, 4);
                message = message.substring(7); //  ://
                switch (command) {
                    case "logi" -> {
                        if (Objects.equals(fromUserName, "Server")) {
                            Map<String, Socket> map = chatUniversalData.getAllOnlineUser();
                            map.put(message, null);
                            flushUserList(chatUniversalData);
                        }
                    }
                    case "exit" -> {
                        if (Objects.equals(fromUserName, "Server")) {
                            Map<String, Socket> map = chatUniversalData.getAllOnlineUser();
                            map.remove(message);
                            flushUserList(chatUniversalData);
                        }
                    }
                    case "text" -> {
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
                        String time = dateFormat.format(date);
                        appendAndFlush(messageArea, fromUserName + " " + time + "\n" + message);
                    }
                }

            } catch (Exception ignored) {
            }
        }
    }

}
