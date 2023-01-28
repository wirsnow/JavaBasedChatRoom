package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

import static indi.wirsnow.chatroom.util.ChatMessageParse.parseMessage;
import static indi.wirsnow.chatroom.util.ChatUniversalUtil.*;

/**
 * @author : wirsnow
 * @date : 2023/1/14 18:08
 * @description : 服务端接收消息类
 */
public class ServerMessageInput implements Runnable {
    private final Map<String, Socket> allOnlineUser;
    private final ChatUniversalData chatUniversalData;
    private final Socket socket;

    public ServerMessageInput(Socket socket, ChatUniversalData chatUniversalData) {
        this.socket = socket;
        this.allOnlineUser = chatUniversalData.getAllOnlineUser();
        this.chatUniversalData = chatUniversalData;
    }

    /**
     * 接收并转发客户端消息
     */
    @Override
    public void run() {
        String userName = null;
        String message;
        // 监听客户端消息，如果有发送至服务器的，就转发给指定用户
        BufferedReader in;
        PrintWriter out;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter((new OutputStreamWriter(socket.getOutputStream())), true);
            while (chatUniversalData.getConnected()) {
                message = in.readLine();
                if (message == null || Objects.equals(message.strip(), "")) {
                    continue;
                }
                String[] messageArray = message.split("-to:");  // 消息格式：接收者-to:消息格式://消息内容
                String targetUser = messageArray[0];    //  目标用户
                StringBuilder messageContent = new StringBuilder();
                for (int i = 1; i < messageArray.length; i++) {
                    messageContent.append(messageArray[i]);
                    if (i != messageArray.length - 1) {
                        messageContent.append("-to:");
                    }
                }
                String result = messageContent.toString();    //  消息内容
                if (Objects.equals(targetUser, "Server-MyUserName")) {
                    allOnlineUser.put(result, socket);
                    flushUserList(chatUniversalData);
                    userName = result;
                    System.out.println("用户" + userName + "上线了");
                    out.println("Server-from:list://" + allOnlineUser.keySet());

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
                    case "GetUserList" -> // 将在线用户列表发送给客户端
                            out.println("Server-from:list://" + allOnlineUser.toString());
                    case "LogOut" -> {
                        // 如果是退出消息，就将用户从在线用户列表中移除
                        // 获取value
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
                        if (Objects.equals(targetUser, "Server")) {
                            message = userName + "-from:" + result;
                            result = parseMessage(chatUniversalData, message);
                            messageInsertText(chatUniversalData.getMessagePane(), result);
                        } else {
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
