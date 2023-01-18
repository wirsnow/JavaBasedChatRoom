package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

/**
 * @author : wirsnow
 * @date : 2023/1/14 18:08
 * @description : 服务端用来转发客户端消息的类
 */
public class ServerMessageInput implements Runnable {
    private final Map<String, Socket> allOnlineUser;
    private final ChatUniversalData chatUniversalData;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public ServerMessageInput(ServerSocket serverSocket, ChatUniversalData chatUniversalData) {
        this.allOnlineUser = chatUniversalData.getAllOnlineUser();
        this.chatUniversalData = chatUniversalData;

        try {
            Socket socket = serverSocket.accept();
            chatUniversalData.setSocket(socket);
            System.out.println("客户端连接成功");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter((new OutputStreamWriter(socket.getOutputStream())), true);
            out.println("Server-from:gteu://00");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收并转发客户端消息
     */
    @Override
    public void run() {
        String message;
        // 监听客户端消息，如果有发送至服务器的，就转发给指定用户
        try {
            while ((message = in.readLine()) != null) {
                String[] messageArray = message.split("-to:");  // 消息格式：接收者-to:消息格式://消息内容
                String targetUser = messageArray[0];    //  目标用户
                StringBuilder messageContent = new StringBuilder();
                for (int i = 1; i < messageArray.length; i++) {
                    messageContent.append(messageArray[i]);
                }
                message = messageContent.toString();    //  消息内容
                if (Objects.equals(targetUser, "Server-MyUserName")) allOnlineUser.put(message, socket);
                System.out.println("收到消息：" + message);
//                switch (message) {
//                    case "GetUserList" -> {
//                        // 将在线用户列表发送给客户端
//                        chatUniversalData.oos.writeUTF("Server-from:list://" + allOnlineUser.toString());
//                        chatUniversalData.oos.flush();
//                    }
//                    case "LogOut" -> {
//                        // 如果是退出消息，就将用户从在线用户列表中移除
//                        allOnlineUser.remove(targetUser);
//                        flushUserList(chatUniversalData);
//                        // 通知其他用户有用户下线
//                        for (Map.Entry<String, Socket> entry : allOnlineUser.entrySet()) {
//                            if (entry.getValue() != null) {
//                                ObjectInputStream oisTemp = new ObjectInputStream(entry.getValue().getInputStream());
//                                ObjectOutputStream oosTemp = new ObjectOutputStream(entry.getValue().getOutputStream());
//                                oosTemp.writeObject("Server-from:exit://" + targetUser);
//                                oosTemp.flush();
//                            }
//                        }
//                    }
//                    default -> {
//                        Socket targetSocket = allOnlineUser.get(targetUser);
//                        ObjectInputStream oisTemp = new ObjectInputStream(targetSocket.getInputStream());
//                        ObjectOutputStream oosTemp = new ObjectOutputStream(targetSocket.getOutputStream());
//                        oosTemp.writeObject(userName + "-from:" + message);
//                        oosTemp.flush();
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
