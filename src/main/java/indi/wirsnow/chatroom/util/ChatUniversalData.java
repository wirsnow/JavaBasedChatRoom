package indi.wirsnow.chatroom.util;

import javax.swing.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wirsnow
 * @date : 2023/1/14 20:35
 * @description : 储存服务端不同的类之间需要交换的数据
 */
public class ChatUniversalData {
    private Socket socket;
    private String userName = null;
    private String toUserName = null;
    private boolean Connected = false;
    private final JList<String> userList = new JList<>();        // 用户列表
    private final JTextArea messageArea = new JTextArea();
    private final JTextArea editorArea = new JTextArea();
    private final JTextField ipField = new JTextField("127.0.0.1");     // IP输入框
    private final JTextField portField = new JTextField("56448");       // port输入框
    private final JTextField userField = new JTextField("当前在线: 0人"); // 在线人数
    private final Map<String, Socket> allOnlineUser = new HashMap<>();

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public boolean getConnected() {
        return Connected;
    }

    public void setConnected(boolean connected) {
        Connected = connected;
    }

    public JList<String> getUserList() {
        return userList;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public JTextArea getEditorArea() {
        return editorArea;
    }

    public JTextField getIpField() {
        return ipField;
    }

    public JTextField getPortField() {
        return portField;
    }

    public JTextField getUserField() {
        return userField;
    }

    public Map<String, Socket> getAllOnlineUser() {
        return allOnlineUser;
    }


}
