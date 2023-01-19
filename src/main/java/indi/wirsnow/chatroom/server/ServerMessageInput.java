package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

import static indi.wirsnow.chatroom.util.ChatUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/14 18:08
 * @description : 服务端用来转发客户端消息的类
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

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter((new OutputStreamWriter(socket.getOutputStream())), true);
            while ((message = in.readLine()) != null && !Objects.equals(message.trim(), "")) {
                String[] messageArray = message.split("-to:");  // 消息格式：接收者-to:消息格式://消息内容
                String targetUser = messageArray[0];    //  目标用户
                StringBuilder messageContent = new StringBuilder();
                for (int i = 1; i < messageArray.length; i++) {
                    messageContent.append(messageArray[i]);
                }
                message = messageContent.toString();    //  消息内容
                if (Objects.equals(targetUser, "Server-MyUserName")) {
                    allOnlineUser.put(message, socket);
                    System.out.println("用户" + message + "上线");
                    flushUserList(chatUniversalData);
                    userName = message;
                    continue;
                }
                switch (message) {
                    case "GetUserList" -> // 将在线用户列表发送给客户端
                            out.println("Server-from:list://" + allOnlineUser.toString());
                    case "LogOut" -> {
                        // 如果是退出消息，就将用户从在线用户列表中移除
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
                            message = message.replace("\\/n", "\n").replace("\\/r", "\r");
                            chatUniversalData.getMessageArea().append(message);
                        } else {
                            Socket targetSocket = allOnlineUser.get(targetUser);
                            PrintWriter outTemp = new PrintWriter((new OutputStreamWriter(targetSocket.getOutputStream())), true);
                            outTemp.println(userName + "-from:" + message);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
