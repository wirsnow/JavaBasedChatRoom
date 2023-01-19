package indi.wirsnow.chatroom.util;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author : wirsnow
 * @date : 2023/1/14 20:35
 * @description : 储存客户端与服务端不同的类之间需要交换的数据
 */
public class ChatUniversalData {
    private final JList<String> userList = new JList<>();       // 用户列表
    private final JTextArea messageArea = new JTextArea();      // 消息区域
    private final JTextArea editorArea = new JTextArea();       // 编辑区域
    private final JTextField ipField = new JTextField("127.0.0.1");     // IP输入框
    private final JTextField portField = new JTextField("56448");       // port输入框
    private final JTextField userField = new JTextField("当前在线: 0人"); // 在线人数
    public BufferedReader in = null;    // 输入流
    public PrintWriter out = null;      // 输出流
    private Socket socket = null;       // 服务端与客户端的socket
    private String userName = null;     // 用户名
    private String toUserName = null;   // 要发送的用户名
    private boolean Connected = false;  // 是否已连接
    private Map<String, Socket> allOnlineUser = new TreeMap<>();

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
        this.Connected = connected;
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

    public void setAllOnlineUser(Map<String, Socket> allOnlineUser) {
        this.allOnlineUser = allOnlineUser;
    }


}
