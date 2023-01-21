package indi.wirsnow.chatroom.util;

import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.base64ToFile;
import static indi.wirsnow.chatroom.util.ChatUniversalUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/19 14:58
 * @description : 解析收到的消息
 */
public class ChatMessageParse {
    public static String parseMessage(ChatUniversalData chatUniversalData, String message) {
        String[] messageArray = message.split("-from:");    // 消息格式：发送者-from:消息类型://消息内容
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
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);
        switch (command) {
            case "logi" -> {
                if (Objects.equals(fromUserName, "Server")) {
                    chatUniversalData.getAllOnlineUser().put(message, null);
                    flushUserList(chatUniversalData);
                    return null;
                }
            }
            case "exit" -> {
                if (Objects.equals(fromUserName, "Server")) {
                    chatUniversalData.getAllOnlineUser().remove(message);
                    flushUserList(chatUniversalData);
                    return null;
                }
            }
            case "list" -> {
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
            case "file" ->{
                String[] tempArray = message.split("-name:");    // 消息格式：发送者-from:消息类型://消息内容
                String fileName = tempArray[0];
                StringBuilder tempContent = new StringBuilder();
                if(tempArray.length > 2){
                    for (int i = 1; i < tempArray.length; i++) {
                        tempContent.append(tempArray[i]);
                    }
                }else if (tempArray.length == 2) {
                    tempContent.append(tempArray[1]);
                }
                String base64 = tempContent.toString();
                if(Objects.equals(tempArray[1], "0")){
                    base64 = "0";
                }
                String filePath = base64ToFile(chatUniversalData, base64, fileName);
                // String hyperlinks = "<html><A href='file:///"+filepath.replace("\\", "/")+"'>"+fileName+"</A></html>";
                return fromUserName + " " + time + "\n已接收文件: "+ fileName + "\n已保存至: " + filePath + "\n";
            }
            case "text" -> {
                return fromUserName + " " + time + "\n" + message + "\n";
            }
            case "texs" -> {
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

}
