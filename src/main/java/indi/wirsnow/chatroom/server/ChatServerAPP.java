package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.swingui.ChatFrame;
import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;

/**
 * @author : wirsnow
 * @date : 2022/12/8 19:24
 * @description : 该类用于启动服务端
 */
public class ChatServerAPP {
    public static void main(String[] args) {
        // 如果要构建jar, 先查看ChatUniversalUtil第157行的注释

        // 创建数据类
        ChatUniversalData chatUniversalData = new ChatUniversalData();
        // 启动ui界面
        SwingUtilities.invokeLater(() -> new ChatFrame("Server", chatUniversalData));
    }
}
