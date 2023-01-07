package indi.wirsnow.chatroom.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author : wirsnow
 * @date : 2022/12/31 23:20
 * @description : 通用ui界面
 */
public class ChatFrame {
    private final JTextArea messageArea;    // 聊天记录显示框
    private final JTextArea editorArea;     // 文字输入框
    private final ChatFrameListener listener; // 监听器
    protected static final JFrame frame = new JFrame("聊天窗口");  // 创建总的窗口

    static {
        FlatIntelliJLaf.setup();
    }

    /**
     * 构造方法
     *
     * @param messageArea 聊天记录显示框
     * @param editorArea  文字输入框
     */
    public ChatFrame(String signal,
                     JTextArea messageArea,
                     JTextArea editorArea,
                     ChatFrameListener listener) {
        this.messageArea = messageArea;
        this.editorArea = editorArea;
        this.listener = listener;
        createFrame();

        if (Objects.equals(signal, "Client")) {
            ChatLoginFrame chatLoginFrame = new ChatLoginFrame();
            chatLoginFrame.login(frame, this.listener);
        } else if (Objects.equals(signal, "Server")) {
            this.listener.setUserName("Server");
            frame.setVisible(true);
        }
    }

    /**
     * 初始化组件
     */
    public void createFrame() {
        // 窗体整体设置
        frame.setResizable(true);  // 设置窗口可改变大小
        frame.setSize(800, 600);    // 设置窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭方式
        frame.setMinimumSize(new Dimension(500, 500)); // 设置窗口最小大小
        frame.setLocationRelativeTo(null);  // 设置窗口居中

        // 左侧聊天面板
        ChatLeftPanel chatLeftPanel = new ChatLeftPanel(frame, messageArea, editorArea, listener);
        chatLeftPanel.createLeftPanel();

        // 右侧功能面板
        ChatRightPanel chatRightPanel = new ChatRightPanel(frame, listener);
        chatRightPanel.createRightPanel();

    }
}
