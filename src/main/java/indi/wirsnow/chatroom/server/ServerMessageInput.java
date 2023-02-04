package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

import static indi.wirsnow.chatroom.util.ChatMessageParse.parseMessage;
import static indi.wirsnow.chatroom.util.ChatUniversalUtil.flushUserList;
import static indi.wirsnow.chatroom.util.ChatUniversalUtil.messageInsertText;

/**
 * @author : wirsnow
 * @date : 2023/1/14 18:08
 * @description : 服务端接收消息类
 */
public class ServerMessageInput implements Runnable {
    private final Socket socket;
    private final ChatUniversalData chatUniversalData;

    /**
     * 构造方法
     *
     * @param socket            客户端socket
     * @param chatUniversalData 数据
     */
    public ServerMessageInput(Socket socket, ChatUniversalData chatUniversalData) {
        this.socket = socket;
        this.chatUniversalData = chatUniversalData;
    }

    /**
     * 接收并转发客户端消息
     */
    @Override
    public void run() {
        String userName = null;
        String message;
        Map<String, Socket> allOnlineUser = chatUniversalData.getAllOnlineUser();
        BufferedReader in;
        PrintWriter out;
        try {
            // 获取输入输出流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter((new OutputStreamWriter(socket.getOutputStream())), true);
            // 如果连接状态为true,则一直监听
            while (chatUniversalData.getConnected()) {
                message = in.readLine();
                // 如果消息为空或者消息为空字符串，则跳过
                if (message == null || Objects.equals(message.strip(), "")) {
                    continue;
                }
                // 拆分消息, 消息格式: 接收者-to:消息格式://消息内容
                String[] messageArray = message.split("-to:");
                String targetUser = messageArray[0];    //  目标用户
                StringBuilder messageContent = new StringBuilder();
                // 拼接消息内容
                for (int i = 1; i < messageArray.length; i++) {
                    messageContent.append(messageArray[i]);
                    if (i != messageArray.length - 1) {
                        messageContent.append("-to:");
                    }
                }
                String result = messageContent.toString();    //  消息内容
                // 接收新上线的用户名并通知其他客户端
                if (Objects.equals(targetUser, "Server-MyUserName")) {
                    // 将新上线的用户加入在线用户列表并刷新用户列表
                    allOnlineUser.put(result, socket);
                    flushUserList(chatUniversalData);
                    // 向新上线的用户发送当前用户列表
                    userName = result;
                    out.println("Server-from:list://" + allOnlineUser.keySet());
                    // 通知其他用户有新用户上线
                    for (Map.Entry<String, Socket> entry : allOnlineUser.entrySet()) {
                        if (entry.getValue() != null) {
                            Socket targetSocket = entry.getValue();
                            PrintWriter outTemp = new PrintWriter((new OutputStreamWriter(targetSocket.getOutputStream())), true);
                            outTemp.println("Server-from:logi://" + userName);
                        }
                    }
                    continue;
                }
                switch (result) {
                    case "GetUserList" -> out.println("Server-from:list://" + allOnlineUser.toString());
                    case "LogOut" -> {
                        // 如果是退出消息，就将用户从在线用户列表中移除并刷新用户列表
                        allOnlineUser.get(targetUser).close();
                        allOnlineUser.remove(targetUser);
                        flushUserList(chatUniversalData);
                        // 通知其他用户有用户下线
                        for (Map.Entry<String, Socket> entry : allOnlineUser.entrySet()) {
                            if (entry.getValue() != null) {
                                Socket targetSocket = entry.getValue();
                                PrintWriter outTemp = new PrintWriter((new OutputStreamWriter(targetSocket.getOutputStream())), true);
                                outTemp.println("Server-from:exit://" + targetUser);
                            }
                        }

                    }
                    default -> {
                        // 如果目标用户为Server，则正常接收，否则转发消息
                        if (Objects.equals(targetUser, "Server")) {
                            // 解析并插入消息到消息面板
                            message = userName + "-from:" + result;
                            result = parseMessage(chatUniversalData, message);
                            messageInsertText(chatUniversalData.getMessagePane(), result);
                        } else {
                            // 获取目标用户的socket并转发消息
                            Socket targetSocket = allOnlineUser.get(targetUser);
                            PrintWriter outTemp = new PrintWriter((new OutputStreamWriter(targetSocket.getOutputStream())), true);
                            outTemp.println(userName + "-from:" + result);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
