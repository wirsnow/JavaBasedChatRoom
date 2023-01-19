package indi.wirsnow.chatroom.util;

import javax.swing.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/19 14:58
 * @description : 解析收到的消息
 */
public class ChatMessageParse {
    public static String parseMessage(ChatUniversalData chatUniversalData, String message) {
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
                    return null;
                }
            }
            case "exit" -> {
                if (Objects.equals(fromUserName, "Server")) {
                    Map<String, Socket> map = chatUniversalData.getAllOnlineUser();
                    map.remove(message);
                    flushUserList(chatUniversalData);
                    return null;
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
                    return null;
                }
            }
            case "text" -> {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
                String time = dateFormat.format(date);
                return fromUserName + " " + time + "\n" + message + "\n";
            }
            case "texs" -> {
                switch (message) {
                    case "start" -> {
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
                        String time = dateFormat.format(date);
                        return fromUserName + " " + time + "\n";
                    }
                    case "startstart" -> {
                        return "start";
                    }
                    case "newlinenewline" -> {
                        return "newline";
                    }
                    case "newline" -> {
                        return "\n";
                    }
                    default -> {
                        return message + "\n";
                    }
                }

            }
        }
        return null;
    }

}
