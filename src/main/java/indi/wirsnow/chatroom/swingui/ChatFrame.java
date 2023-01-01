package indi.wirsnow.chatroom.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author : wirsnow
 * @date : 2022/12/31 23:20
 * @description : 通用ui界面
 */
public class ChatFrame {

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

    private final String userName;          // 用户名
    private final JTextArea messageArea;    // 聊天记录显示框
    private final JTextArea editorArea;     // 文字输入框
    private final Map<String, Socket> map;  // 用户信息

    /**
     * 构造方法
     *
     * @param userName    用户名
     * @param messageArea 聊天记录显示框
     * @param editorArea  文字输入框
     * @param map         用户信息
     */
    public ChatFrame(String userName,
                     JTextArea messageArea,
                     JTextArea editorArea,
                     Map<String, Socket> map) throws IOException {
        this.userName = userName;
        this.messageArea = messageArea;
        this.editorArea = editorArea;
        this.map = map;
        initComponents();
    }

    /**
     * 设置工具栏按钮图标
     *
     * @param button   按钮
     * @param iconPath 图标路径
     */
    public static void autoBarIcon(JButton button, String iconPath) {
        ImageIcon icon = new ImageIcon("src/main/resources/icons/" + iconPath);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(newImg));
    }

    /**
     * 设置工具栏按钮格式
     *
     * @param button   按钮
     * @param Tips     按钮提示
     * @param command  按钮命令
     * @param listener 监听器
     */
    public static void setTLC(JButton button, String Tips, String command, ChatFrameListener listener) {
        button.setToolTipText(Tips);
        button.setActionCommand(command);
        button.addActionListener(listener);
    }

    /**
     * 设置文本框的默认格式
     *
     * @param area 文本框
     */
    public static void setAreaDefault(JTextArea area) {
        area.setTabSize(4);     // 设置tab键的长度
        area.setLineWrap(true); // 设置自动换行
        area.setWrapStyleWord(true);    // 设置断行不断字
        area.setBackground(Color.WHITE);    // 设置聊天记录显示框背景颜色
        area.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
    }

    /**
     * 添加组件
     *
     * @param jPanel             面板
     * @param gridBagLayout      网格布局
     * @param gridBagConstraints 约束
     * @param component          组件
     * @param gridx              组件所在列
     * @param gridy              组件所在行
     * @param gridwidth          组件所占列数
     * @param gridheight         组件所占行数
     * @param weightx            x方向权重
     * @param weighty            y方向权重
     */
    public static void addGridBagComponent(JPanel jPanel, GridBagLayout gridBagLayout, GridBagConstraints gridBagConstraints, JComponent component, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty) {
        gridBagConstraints.gridx = gridx;  // 设置组件所在列
        gridBagConstraints.gridy = gridy;  // 设置组件所在行
        gridBagConstraints.gridwidth = gridwidth;      // 设置组件所占列数
        gridBagConstraints.gridheight = gridheight;    // 设置组件所占行数
        gridBagConstraints.weightx = weightx;  // 设置组件在水平方向上的拉伸比例
        gridBagConstraints.weighty = weighty;  // 设置组件在垂直方向上的拉伸比例
        gridBagConstraints.anchor = GridBagConstraints.CENTER; // 设置组件对齐方式
        gridBagConstraints.fill = GridBagConstraints.BOTH;     // 设置填充方式
        gridBagLayout.setConstraints(component, gridBagConstraints); // 设置组件
        jPanel.add(component);       // 添加组件
    }

    /**
     * 初始化组件
     */
    void initComponents() throws IOException {
        Socket socket = map.get(userName);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
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
                setAreaDefault(messageArea);    // 设置消息框格式
                messageArea.setEditable(false); // 设置不可编辑
                messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 设置水平滚动条不可见
                addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, messageScrollPane, 0, 0, 1, 1, 1, 1); // 添加组件
            }

            // 添加工具栏
            {
                //设置截图按钮
                autoBarIcon(screenshotsButton, "screenshots.png");  // 设置按钮图标
                setTLC(screenshotsButton, "屏幕截图", "screenshots", chatFrameListener);   // 设置按钮格式

                // 设置发送语音按钮
                autoBarIcon(sendAudioButton, "AudioButton.png");
                setTLC(sendAudioButton, "发送语音", "sendAudio", chatFrameListener);

                // 设置发送文件按钮
                autoBarIcon(sendFileButton, "FileButton.png");
                setTLC(sendFileButton, "发送文件", "sendFile", chatFrameListener);

                //将截图、语音、文件按钮添加到工具栏
                toolBar.add(screenshotsButton);
                toolBar.add(sendAudioButton);
                toolBar.add(sendFileButton);
                toolBar.setFloatable(false);
                toolBar.setMaximumSize(new Dimension(200, 25));
                toolBar.setMinimumSize(new Dimension(200, 25));

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
                sendButton.addActionListener(chatFrameListener); // 添加监听器
                sendButton.setActionCommand("send"); // 设置按钮命令
                addGridBagComponent(messagePanel, gridBagLayout, gridBagConstraints, sendButton, 0, 3, 1, 1, 1, 0); // 添加组件
            }
        }
        frame.add(messagePanel, BorderLayout.CENTER);    // 添加聊天面板

        // 右侧功能面板
        functionPanel.setMaximumSize(new Dimension(200, 100000)); // 设置最大大小
        functionPanel.setMinimumSize(new Dimension(150, 400)); // 设置最小大小
    }
}
