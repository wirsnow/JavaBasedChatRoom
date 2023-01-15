package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.swingui.ChatFrame;
import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;
import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;

/**
 * @author : wirsnow
 * @date : 2022/12/8 19:24
 * @description : 启动服务端
 */
public class ChatServerAPP {
    public static void main(String[] args) {
        ChatUniversalData chatUniversalData = new ChatUniversalData();  // 创建数据传输类
        ChatFrameListener listener = new ChatFrameListener(chatUniversalData);  // 创建监听器
        SwingUtilities.invokeLater(() -> new ChatFrame("Server", listener, chatUniversalData)); // 启动ui界面
    }
}
