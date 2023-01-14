package indi.wirsnow.chatroom.client;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author : wirsnow
 * @date : 2023/1/8 23:41
 * @description : 客户端响应
 */
public class ChatClientThread implements Runnable{
    private final Socket socket;
    private final Map<String, Socket> map;
    private final JTextArea messageArea;

    public ChatClientThread(Socket socket, Map<String, Socket> map, JTextArea messageArea) {
        super();
        this.socket = socket;
        this.map = map;
        this.messageArea = messageArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream ois = (new ObjectInputStream(socket.getInputStream()));
                String string = ois.toString();
                if (string.startsWith("login://")) {  //登录
                    String userName = string.split("://")[1];
                    map.put(userName, socket);
                    messageArea.append("用户" + userName + "已上线");
                    continue;
                }
                if (string.startsWith("to://")) {     //私聊
                    String[] strings = string.split("://");
                    String userName = strings[1];
                    String message = strings[2];
                    Socket socket = map.get(userName);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    continue;
                }
                if (string.startsWith("exit://")) {   //退出
                    String userName = string.split("://")[1];
                    map.remove(userName);
                    messageArea.append("用户" + userName + "已下线");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
