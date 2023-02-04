package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.swingui.ChatFrame;
import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;

/**
 * @author : wirsnow
 * @date : 2022/12/7 11:04
 * @description : 该类用于启动客户端
 */
public class ChatClientAPP {
    public static void main(String[] args) {
        // 如果要构建jar, 先查看ChatUniversalUtil第157行的注释

        // 创建数据类
        ChatUniversalData chatUniversalData = new ChatUniversalData();
        // 启动ui界面
        SwingUtilities.invokeLater(() -> new ChatFrame("Client", chatUniversalData));
    }
}
