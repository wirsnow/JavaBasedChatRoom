package indi.wirsnow.chatroom.util;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.*;

/**
 * @author : wirsnow
 * @date : 2023/1/19 14:58
 * @description : 解析收到的消息
 */
public class ChatMessageParse {
    /**
     * 解析消息
     *
     * @param chatUniversalData 数据类
     * @param message           收到的消息
     * @return 返回解析后的消息
     */
    public static String parseMessage(ChatUniversalData chatUniversalData, String message) {
        // 消息格式：发送者-from:消息类型://消息内容
        String[] messageArray = message.split("-from:");
        String fromUserName = messageArray[0];  // 获取发送者
        StringBuilder messageContent = new StringBuilder();
        for (int i = 1; i < messageArray.length; i++) {
            messageContent.append(messageArray[i]);
        }

        message = messageContent.toString();        // 获取消息内容
        String command = message.substring(0, 4);   // 获取消息类型
        message = message.substring(7);   // 获取 :// 之后的数据

        // 获取当前时间
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);

        switch (command) {
            case "logi" -> {
                // 服务器通知有登录
                if (Objects.equals(fromUserName, "Server")) {
                    chatUniversalData.getAllOnlineUser().put(message, null);
                    flushUserList(chatUniversalData);
                    return null;
                }
            }
            case "exit" -> {
                // 服务器通知有用户下线
                if (Objects.equals(fromUserName, "Server")) {
                    if (message.equals("Server")) {
                        // 如果是服务器下线，则清空列表，关闭连接
                        chatUniversalData.getUserList().setModel(new DefaultListModel<>());
                        chatUniversalData.getUserField().setText("当前在线: 0");
                        try {
                            chatUniversalData.getSocket().close();
                        } catch (IOException ignored) {
                        }
                        chatUniversalData.setConnected(false);
                        return null;
                    }
                    // 如果不是服务器下线，则移除用户
                    chatUniversalData.getAllOnlineUser().remove(message);
                    flushUserList(chatUniversalData);
                    return null;
                }
            }
            case "list" -> {
                // 服务器发送所有在线用户
                if (Objects.equals(fromUserName, "Server")) {

                    Map<String, Socket> allOnlineUser = new TreeMap<>();
                    // string转map
                    String[] strings = message.substring(1, message.length() - 1).split(", ");
                    for (String str : strings) {
                        allOnlineUser.put(str, null);
                    }

                    chatUniversalData.setAllOnlineUser(allOnlineUser);
                    flushUserList(chatUniversalData);
                    return null;
                }
            }
            case "file" -> {
                // 消息类型为文件
                String[] splitResult = fileSplit(message);
                String fileName = splitResult[0];   // 获取文件名
                String base64 = splitResult[1];     // 获取base64编码
                if (Objects.equals(base64, "0")) {
                    base64 = "0";
                }
                // 将base64编码转换为文件
                String filePath = base64ToFile(chatUniversalData, base64, fileName, "file") + "\\" + fileName;
                return fromUserName + " " + time + "\n已接收文件: " + fileName + "\n已保存至: " + filePath + "\n";
            }
            case "icon" -> {
                // 消息类型为图片
                String[] splitResult = fileSplit(message);
                String fileName = splitResult[0];   // 获取文件名
                String base64 = splitResult[1];     // 获取base64编码
                // 将base64编码转换为文件
                String filePath = base64ToFile(chatUniversalData, base64, fileName, "screenshots") + "\\" + fileName;
                // 将图片插入到聊天框
                messageInsertText(chatUniversalData.getMessagePane(), fromUserName + " " + time + "\n");
                messageInsertImage(chatUniversalData.getMessagePane(), new File(filePath));
                return null;
            }
            case "audi" -> {
                // 消息类型为语音
                String[] splitResult = fileSplit(message);
                String fileName = splitResult[0];   // 获取文件名
                String base64 = splitResult[1];     // 获取base64编码
                // 将base64编码转换为文件
                String filePath = base64ToFile(chatUniversalData, base64, fileName, "audio") + "\\" + fileName;
                return fromUserName + " " + time + "\n已接收语音: " + fromUserName + "\n已保存至: " + filePath + "\n";
            }
            case "text" -> {
                // 消息类型为单行文本
                return fromUserName + " " + time + "\n" + message + "\n";
            }
            case "texs" -> {
                // 消息类型为多行文本
                // "☩", "❊", "□"都是特殊符号，用来解决多行信息发送的问题
                // 虽然是个不好的办法，但是好像还挺好用...
                switch (message) {
                    case "☩" -> {
                        return fromUserName + " " + time + "\n";
                    }
                    case "❊" -> {
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

    /**
     * 解析文件消息，获取文件名和文件的base64编码
     *
     * @param message 消息
     * @return 文件名和文件的base64编码
     */
    private static String[] fileSplit(String message) {
        // 消息格式：发送者-from:消息类型://消息内容
        String[] tempArray = message.split("-name:");
        String fileName = tempArray[0];    // 获取文件名
        StringBuilder tempContent = new StringBuilder();
        if (tempArray.length > 2) {
            for (int i = 1; i < tempArray.length; i++) {
                tempContent.append(tempArray[i]);
            }
        } else if (tempArray.length == 2) {
            tempContent.append(tempArray[1]);
        }

        return new String[]{fileName, tempContent.toString()};
    }
}
