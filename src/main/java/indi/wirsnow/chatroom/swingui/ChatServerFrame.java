package indi.wirsnow.chatroom.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * @author : wirsnow
 * @date : 2023/1/1 1:19
 * @description : 用于创建服务端的右侧功能面板
 */
public class ChatServerFrame extends ChatFrame {
    static {
        FlatIntelliJLaf.setup();
    }

    private final JPanel functionPanel;

    /**
     * 构造方法
     *
     * @param functionPanel 右侧功能面板
     * @param userName      用户名
     * @param messageArea   聊天记录显示框
     * @param editorArea    文字输入框
     * @param map           用户信息
     */
    public ChatServerFrame(JPanel functionPanel,
                           String userName,
                           JTextArea messageArea,
                           JTextArea editorArea,
                           Map<String, Socket> map) throws IOException {

        super(userName, messageArea, editorArea, map);
        this.functionPanel = functionPanel;
    }

}