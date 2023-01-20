package indi.wirsnow.chatroom.swingui;

import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;
import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.awt.*;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.*;

/**
 * @author : wirsnow
 * @date : 2023/1/7 19:56
 * @description : 左侧聊天面板
 */
public class ChatLeftPanel {
    private final JPanel messagePanel = new JPanel();            // 聊天面板
    private final JToolBar toolBar = new JToolBar();             // 总工具栏
    private final JButton sendButton = new JButton("发送");  // 发送按钮
    private final JButton sendAudioButton = new JButton();       // 语音按钮
    private final JButton sendFileButton = new JButton();        // 文件按钮
    private final JButton screenshotsButton = new JButton();     // 截图按钮
    private final JFrame frame;             // 父窗口
    private final ChatFrameListener listener; // 监听器
    private final ChatUniversalData chatUniversalData;

    public ChatLeftPanel(JFrame frame, ChatFrameListener listener, ChatUniversalData chatUniversalData) {
        this.frame = frame;
        this.listener = listener;
        this.chatUniversalData = chatUniversalData;
    }

    public void createLeftPanel() {
        // 左侧聊天面板
        GridBagLayout gridBagLayout = new GridBagLayout();                  // 创建网格布局
        GridBagConstraints gridBagConstraints = new GridBagConstraints();   // 创建网格约束
        messagePanel.setLayout(gridBagLayout);              // 设置布局
        messagePanel.setBorder(BorderFactory.createTitledBorder("聊天记录"));  // 设置边框
        messagePanel.requestFocus();    // 获取焦点
        gridBagLayout.rowHeights = new int[]{235, 40, 130, 30}; // 设置最小行高

        JTextArea messageArea = chatUniversalData.getMessageArea();
        JTextArea editorArea = chatUniversalData.getEditorArea();
        JScrollPane messageScrollPane = new JScrollPane(messageArea);   // 创建滚动条
        JScrollPane editorScrollPane = new JScrollPane(editorArea);     // 创建滚动条

        // 设置聊天记录显示框
        {
            setAreaDefault(messageArea);    // 设置消息框格式
            messageArea.setEditable(false); // 设置不可编辑
            messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 设置水平滚动条不可见
            addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, messageScrollPane, 0, 0, 1, 1, 1, 1); // 添加组件
        }

        // 添加工具栏
        {
            //设置截图按钮
            autoBarIcon(screenshotsButton, "screenshots.png");  // 设置按钮图标
            setTLC(screenshotsButton, "屏幕截图", "screenshots", listener);   // 设置按钮格式

            // 设置发送语音按钮
            autoBarIcon(sendAudioButton, "AudioButton.png");
            setTLC(sendAudioButton, "发送语音", "sendAudio", listener);

            // 设置发送文件按钮
            autoBarIcon(sendFileButton, "FileButton.png");
            setTLC(sendFileButton, "发送文件", "sendFile", listener);

            //将截图、语音、文件按钮添加到工具栏
            toolBar.add(screenshotsButton);
            toolBar.add(sendAudioButton);
            toolBar.add(sendFileButton);
            toolBar.setFloatable(false);
            toolBar.setMaximumSize(new Dimension(200, 40));
            toolBar.setMinimumSize(new Dimension(200, 40));

            addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, toolBar, 0, 1, 1, 1, 1, 0); // 添加组件
        }

        // 设置文字输入框
        {
            setAreaDefault(editorArea);    // 设置输入框格式
            editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 设置水平滚动条不可见
            addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, editorScrollPane, 0, 2, 1, 1, 1, 0); // 添加组件
        }

        //设置发送按钮
        {
            sendButton.setMinimumSize(new Dimension(100, 25)); // 设置按钮最小大小
            sendButton.addActionListener(listener); // 添加监听器
            sendButton.setActionCommand("send"); // 设置按钮命令
            addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, sendButton, 0, 3, 1, 1, 1, 0); // 添加组件
        }

        frame.add(messagePanel, BorderLayout.CENTER);    // 添加聊天面板
    }


}
