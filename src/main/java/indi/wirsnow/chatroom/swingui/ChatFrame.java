package indi.wirsnow.chatroom.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import indi.wirsnow.chatroom.client.ClientMessageOutput;
import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;
import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * @author : wirsnow
 * @date : 2022/12/31 23:20
 * @description : 通用ui界面
 */
public class ChatFrame {
    protected static final JFrame frame = new JFrame("聊天窗口");  // 创建总窗口

    static {
        FlatIntelliJLaf.setup();
    }

    private final ChatFrameListener listener; // 监听器
    private final ChatUniversalData chatUniversalData;    // 数据

    /**
     * 构造方法
     *
     * @param signal            服务端/客户按标志
     *                          Server: 服务端
     *                          Client: 客户端
     * @param listener          监听器
     * @param chatUniversalData 通用数据
     */
    public ChatFrame(String signal, ChatFrameListener listener, ChatUniversalData chatUniversalData) {
        this.listener = listener;
        this.chatUniversalData = chatUniversalData;
        judgeCS(signal);
    }

    /**
     * 判断是服务端还是客户端
     */
    private void judgeCS(String signal) {
        if (Objects.equals(signal, "Client")) {
            createFrame();
            (new ChatLoginFrame(frame, chatUniversalData)).login();
        } else if (Objects.equals(signal, "Server")) {
            chatUniversalData.setUserName("Server");
            frame.setTitle("Server");
            createFrame();
            frame.setVisible(true);
        }
    }

    /**
     * 初始化组件
     */
    private void createFrame() {

        // 窗体整体设置
        frame.setResizable(true);  // 设置窗口可改变大小
        frame.setSize(800, 600);    // 设置窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭方式
        frame.setMinimumSize(new Dimension(500, 500)); // 设置窗口最小大小
        frame.setLocationRelativeTo(null);  // 设置窗口居中
        frame.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        if(!Objects.equals(chatUniversalData.getUserName(), "Server")) {
                            super.windowClosing(e);
                            try {
                                ClientMessageOutput clientMessageOutput = new ClientMessageOutput();
                                clientMessageOutput.sendDisconnectMessage(chatUniversalData);
                            }catch (Exception ignored) {}
                        }
                    }
                }
        );
        // 左侧聊天面板
        ChatLeftPanel chatLeftPanel = new ChatLeftPanel(frame, listener, chatUniversalData);
        chatLeftPanel.createLeftPanel();

        // 右侧功能面板
        ChatRightPanel chatRightPanel = new ChatRightPanel(frame, listener, chatUniversalData);
        chatRightPanel.createRightPanel();
    }
}
