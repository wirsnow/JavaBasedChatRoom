package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.swingui.ChatFrame;
import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;
import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;

/**
 * @author : wirsnow
 * @date : 2022/12/7 11:04
 * @description : 启动客户端
 */
public class ChatClientAPP {
    public static void main(String[] args) {
        ChatUniversalData chatUniversalData = new ChatUniversalData();  // 创建数据传输类
        ChatFrameListener listener = new ChatFrameListener(chatUniversalData);  // 创建监听器
        SwingUtilities.invokeLater(() -> new ChatFrame("Client", listener, chatUniversalData)); // 启动ui界面
    }
}
