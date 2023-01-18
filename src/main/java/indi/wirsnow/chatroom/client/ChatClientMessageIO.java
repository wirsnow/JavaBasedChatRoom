package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static indi.wirsnow.chatroom.util.ChatUtil.appendAndFlush;
import static indi.wirsnow.chatroom.util.ChatUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/15 14:34
 * @description : 监听客户端消息输入输出
 */
public class ChatClientMessageIO {
    private final Socket socket;
    private final ChatUniversalData chatUniversalData;

    public ChatClientMessageIO(Socket socket, ChatUniversalData chatUniversalData) {
        this.socket = socket;
        this.chatUniversalData = chatUniversalData;

        ChatClientMessageInput();
    }

    /**
     * 监听客户端消息输入
     */

    public void ChatClientMessageInput() {
        BufferedReader reader;
        try {
            System.out.println(socket.isClosed());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(socket.isClosed());
            while (true) {
                System.out.println(socket.isClosed());
                String message = reader.readLine();
                System.out.println(socket.isClosed());
                System.out.println("收到消息：" + message);
                JTextArea messageArea = chatUniversalData.getMessageArea();
                String[] messageArray = message.split("-from:");
                String fromUserName = messageArray[0];
                StringBuilder messageContent = new StringBuilder();
                for (int i = 1; i < messageArray.length; i++) {
                    messageContent.append(messageArray[i]);
                }
                message = messageContent.toString();
                String command = message.substring(0, 4);
                message = message.substring(7); // 获取://之后的数据

                System.out.println("command:" + command);
                System.out.println("message:" + message);
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
                    case "list" -> {
                        if (Objects.equals(fromUserName, "Server")) {
                            Map<String, Socket> allOnlineUser = new TreeMap<>();
                            // string转map
                            String[] strings = message.split(",");
                            for (String str : strings) {
                                String[] s = str.split("=");
                                allOnlineUser.put(s[0], null);
                            }
                            JOptionPane.showMessageDialog(null, allOnlineUser.toString(), "错误", JOptionPane.ERROR_MESSAGE);
                            chatUniversalData.setAllOnlineUser(allOnlineUser);
                            flushUserList(chatUniversalData);
                        }
                    }
                    case "text" -> {
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
                        String time = dateFormat.format(date);
                        appendAndFlush(messageArea, fromUserName + " " + time + "\n" + message);
                    }
                    case "texs" -> {
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
                        String time = dateFormat.format(date);
                        appendAndFlush(messageArea, fromUserName + " " + time + "\n" + message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}