package indi.wirsnow.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import indi.wirsnow.swingui.listener.ChatFrameListener;
import indi.wirsnow.swingui.util.ChatFrameUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author : wirsnow
 * @date : 2022/12/31 23:20
 * @description : 通用ui界面
 */
public class ChatFrame {

    private final String userName;          // 用户名
    private final JTextArea messageArea;    // 聊天记录显示框
    private final JTextArea editorArea;     // 文字输入框
    private final ObjectOutputStream oos;   // 输出流
    private final ObjectInputStream ois;    // 输入流
    private static final JFrame frame = new JFrame("聊天窗口");  // 创建总的窗口
    private static final JPanel messagePanel = new JPanel();        // 创建左侧聊天面板
    private static final JPanel functionPanel = new JPanel();       // 创建右侧功能面板
    private static final JToolBar toolBar = new JToolBar();             // 工具栏
    private static final JButton sendButton = new JButton("发送");  // 发送按钮
    private static final JButton sendAudioButton = new JButton();       // 发送语音按钮
    private static final JButton sendFileButton = new JButton();        // 发送文件按钮
    private static final JButton screenshotsButton = new JButton();     // 截图按钮


    static {
        FlatIntelliJLaf.setup();
    }

    /**
     * 构造方法
     *
     * @param userName    用户名
     * @param messageArea 聊天记录显示框
     * @param editorArea  文字输入框
     * @param oos         输出流
     * @param ois         输入流
     */
    public ChatFrame(String userName,
                     JTextArea messageArea,
                     JTextArea editorArea,
                     ObjectOutputStream oos,
                     ObjectInputStream ois) {
        this.userName = userName;
        this.messageArea = messageArea;
        this.editorArea = editorArea;
        this.oos = oos;
        this.ois = ois;
        initComponents();
    }
    public static void main(String[] args) {
        new ChatFrame("wirsnow", new JTextArea(), new JTextArea(), null, null);
    }

    /**
     * 初始化组件
     */
    void initComponents() {
        ChatFrameListener chatFrameListener = new ChatFrameListener(userName, messageArea, editorArea, oos); // 创建监听器对象

        // 窗体整体设置
        frame.setResizable(true);  // 设置窗口可改变大小
        frame.setSize(800, 500);    // 设置窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭方式
        frame.setMinimumSize(new Dimension(500, 500)); // 设置窗口最小大小
        frame.setLocationRelativeTo(null);  // 设置窗口居中


        // 左侧聊天面板
        {
            GridBagLayout gridBagLayout = new GridBagLayout();                  // 创建网格布局
            GridBagConstraints gridBagConstraints = new GridBagConstraints();   // 创建网格约束
            messagePanel.setLayout(gridBagLayout);              // 设置布局
            messagePanel.setBorder(BorderFactory.createTitledBorder("聊天记录"));  // 设置边框
            gridBagLayout.rowHeights = new int[]{235, 40, 130, 30}; // 设置最小行高

            JScrollPane messageScrollPane = new JScrollPane(messageArea);   // 创建滚动条
            JScrollPane editorScrollPane = new JScrollPane(editorArea);     // 创建滚动条

            // 设置聊天记录显示框
            {
                ChatFrameUtil.setAreaDefault(messageArea);    // 设置消息框格式
                messageArea.setEditable(false); // 设置不可编辑
                messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 设置水平滚动条不可见
                ChatFrameUtil.addGridBagComponent(messagePanel,gridBagLayout, gridBagConstraints, messageScrollPane, 0, 0, 1, 1, 1, 1); // 添加组件
            }

            // 添加工具栏
            {
                //设置截图按钮
                ChatFrameUtil.autoBarIcon(screenshotsButton, "screenshots.png");  // 设置按钮图标
                ChatFrameUtil.setTLC(screenshotsButton, "屏幕截图", "screenshots", chatFrameListener);   // 设置按钮格式

                // 设置发送语音按钮
                ChatFrameUtil.autoBarIcon(sendAudioButton, "AudioButton.png");
                ChatFrameUtil.setTLC(sendAudioButton, "发送语音", "sendAudio", chatFrameListener);

                // 设置发送文件按钮
                ChatFrameUtil.autoBarIcon(sendFileButton, "FileButton.png");
                ChatFrameUtil.setTLC(sendFileButton, "发送文件", "sendFile", chatFrameListener);

                //将截图、语音、文件按钮添加到工具栏
                toolBar.add(screenshotsButton);
                toolBar.add(sendAudioButton);
                toolBar.add(sendFileButton);
                toolBar.setFloatable(false);
                toolBar.setMaximumSize(new Dimension(200, 25));
                toolBar.setMinimumSize(new Dimension(200, 25));

                ChatFrameUtil.addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, toolBar, 0, 1, 1, 1, 1, 0); // 添加组件
            }

            // 设置文字输入框
            {
                ChatFrameUtil.setAreaDefault(editorArea);    // 设置输入框格式
                editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 设置水平滚动条不可见
                ChatFrameUtil.addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, editorScrollPane, 0, 2, 1, 1, 1, 0); // 添加组件
            }

            //设置发送按钮
            {
                sendButton.setMinimumSize(new Dimension(100, 25)); // 设置按钮最小大小
                sendButton.addActionListener(chatFrameListener); // 添加监听器
                sendButton.setActionCommand("send"); // 设置按钮命令
                ChatFrameUtil.addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, sendButton, 0, 3, 1, 1, 1, 0); // 添加组件
            }
        }
        frame.add(messagePanel, BorderLayout.CENTER);    // 添加聊天面板

        // 右侧功能面板
        {
            functionPanel.setMaximumSize(new Dimension(200, 100000)); // 设置最大大小
            functionPanel.setMinimumSize(new Dimension(150, 400)); // 设置最小大小
            if(userName == null){
                new ChatServerFrame(functionPanel);
            }else {
                ChatClientFrame.setFunctionPanel(functionPanel);
            }
        }
        frame.add(functionPanel, BorderLayout.EAST);     // 添加功能面板
        frame.setVisible(true); // 设置窗口可见
    }
}
