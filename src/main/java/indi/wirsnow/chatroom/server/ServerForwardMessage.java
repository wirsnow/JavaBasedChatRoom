package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import static indi.wirsnow.chatroom.util.ChatUtil.flushUserList;

/**
 * @author : wirsnow
 * @date : 2023/1/14 18:08
 * @description : 服务端用来转发客户端消息的类
 */
public class ServerForwardMessage implements Runnable {
    private final ChatUniversalData chatUniversalData;

    public ServerForwardMessage(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;

    }

    /**
     * 接收并转发客户端消息
     */
    @Override
    public void run() {
        String userName = chatUniversalData.getUserName();
        Socket mySocket = chatUniversalData.getSocket();
        Map<String, Socket> allOnlineUser = chatUniversalData.getAllOnlineUser();
        // 监听客户端消息，如果有发送至服务器的，就转发给指定用户
        try {
            ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
            while (true) {
                Thread.sleep(10);   //  休眠10ms，防止CPU占用过高
                String message = ois.readUTF();
                String[] messageArray = message.split("-to:");  // 消息格式：接收者-to:消息格式://消息内容
                String targetUser = messageArray[0];    //  目标用户
                StringBuilder messageContent = new StringBuilder();
                for (int i = 1; i < messageArray.length; i++) {
                    messageContent.append(messageArray[i]);
                }
                message = messageContent.toString();    //  消息内容
                switch (message) {
                    case "LogOut" -> {
                        // 如果是退出消息，就将用户从在线用户列表中移除
                        allOnlineUser.remove(targetUser);
                        flushUserList(chatUniversalData);
                        // 通知其他用户有用户下线
                        for (Map.Entry<String, Socket> entry : allOnlineUser.entrySet()) {
                            if (entry.getValue() != null) {
                                ObjectOutputStream oos = new ObjectOutputStream(entry.getValue().getOutputStream());
                                oos.writeObject("Server-from:exit://" + targetUser);
                                oos.flush();
                                oos.close();
                            }
                        }
                    }
                    default -> {
                        Socket targetSocket = allOnlineUser.get(targetUser);
                        ObjectOutputStream oos = new ObjectOutputStream(targetSocket.getOutputStream());
                        oos.writeObject(userName + "-from:" + message);
                        oos.flush();
                        oos.close();
                    }

                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
