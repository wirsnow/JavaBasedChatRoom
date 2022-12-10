package indi.wirsnow.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import indi.wirsnow.swingui.listener.ChatFrameListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author : wirsnow
 * @date : 2022/12/7 11:07
 * @description : 实现客户端聊天框显示
 */
public class ChatFrame {
    /*
    该类主要利用swing图形化相关技术，设计聊天软件的UI界面，
    UI界面要求至少有一个聊天记录显示框、文字输入框和发送按钮，
    UI界面设计要求简洁美观，功能上简单易学，致于设计成何种样式不作具体的限制，可自由发挥。
    聊天记录显示框可以显示客户端和服务器端双方的聊天记录，
    聊天记录包括发送的信息的人、时间以及发送的信息内容，可以正确地显示发送信息的顺序。
    点击发送按钮可以将输入的信息发送并显示出来，输入框信息为空时，点击发送按钮，程序不作反应。
    */
    static {
        FlatIntelliJLaf.setup();
    }

    private static final JFrame frame = new JFrame("聊天窗口");  // 创建窗口
    private static final JButton sendButton = new JButton("发送");// 发送按钮
    private static final JButton sendAudioButton = new JButton();   // 发送语音按钮
    private static final JButton sendFileButton = new JButton();    // 发送文件按钮
    private static final JButton screenshotsButton = new JButton(); // 截图按钮
    private static final JTextArea messageArea = new JTextArea();   // 聊天记录显示框
    private static final JScrollPane messageScrollPane = new JScrollPane(messageArea); //滚动条
    private static final JTextArea editorArea = new JTextArea();    // 文字输入框
    private static final JScrollPane editorScrollPane = new JScrollPane(editorArea); //滚动条
    private static final JToolBar toolBar = new JToolBar();         // 工具栏
    private static String sender;                                   // 发送者
    private static String receiver;                                 // 接收者
    private static String message;                                  // 消息
    private static String time;                                     // 时间
    private static final GridBagLayout gridBagLayout = new GridBagLayout();  // 设置窗口布局方式
    private static final GridBagConstraints constraints = new GridBagConstraints(); // 创建约束对象
    private static final ChatFrameListener chatFrameListener = new ChatFrameListener(); // 创建监听器对象


    /**
     * 构造方法
     */
    public ChatFrame() {
        constraints.fill = GridBagConstraints.BOTH; // 设置填充方式
        frame.setSize(600, 500);    // 设置窗口大小
        frame.setLocationRelativeTo(null);  // 设置窗口居中
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭方式
        frame.setMinimumSize(new Dimension(400, 400)); // 设置窗口最小大小
        frame.setLayout(gridBagLayout); // 设置窗口布局方式
        frame.setResizable(true);  // 设置窗口可改变大小
        frame.setVisible(true); // 设置窗口可见

        gridBagLayout.rowHeights = new int[]{235, 25, 130};

        // 设置聊天记录显示框
        setAreaDefault(messageArea);    // 设置消息框格式
        messageArea.setEditable(false); // 设置不可编辑
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 设置水平滚动条不可见
        addComponent(frame, messageScrollPane, 0, 0, 1, 1, 1.0, 1.0); // 添加组件

        // 设置文字输入框
        setAreaDefault(editorArea);    // 设置输入框格式
        editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 设置水平滚动条不可见
        addComponent(frame, editorScrollPane, 0, 2, 1, 1, 1, 0); // 添加组件

        //设置截图按钮
        autoIcon(screenshotsButton, "src/main/resources/icons/screenshots.png"); // 设置按钮图标
        screenshotsButton.setToolTipText("屏幕截图"); // 设置按钮提示
        screenshotsButton.addActionListener(chatFrameListener); // 添加监听器
        screenshotsButton.setActionCommand("screenshots");

        // 设置发送语音按钮
        autoIcon(sendAudioButton, "src/main/resources/icons/AudioButton.png"); // 设置按钮图标
        sendAudioButton.setToolTipText("发送语音"); // 设置按钮提示
        sendAudioButton.addActionListener(chatFrameListener); // 添加监听器
        sendAudioButton.setActionCommand("sendAudio");

        // 设置发送文件按钮
        autoIcon(sendFileButton, "src/main/resources/icons/FileButton.png"); // 设置按钮图标
        sendFileButton.setToolTipText("发送文件");  // 设置按钮提示
        sendFileButton.addActionListener(chatFrameListener); // 添加监听器
        sendFileButton.setActionCommand("sendFile");    // 设置按钮命令

        //将截图、语音、文件按钮添加到工具栏
        toolBar.add(screenshotsButton);
        toolBar.add(sendAudioButton);
        toolBar.add(sendFileButton);
        toolBar.setMaximumSize(new Dimension(200, 25));
        toolBar.setMinimumSize(new Dimension(200, 25));
        addComponent(frame, toolBar, 0, 1, 1, 1, 1, 0); // 添加组件

        sendButton.setMinimumSize(new Dimension(100, 25));
        addComponent(frame, sendButton, 0, 3, 1, 1, 1, 0); // 添加组件
    }

    /**
     * 设置按钮图标
     *
     * @param button   按钮
     * @param iconPath 图标路径
     */
    private static void autoIcon(@NotNull JButton button, String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(newImg));
    }

    /**
     * 设置文本框的默认格式
     *
     * @param area 文本框
     */
    private static void setAreaDefault(@NotNull JTextArea area) {
        area.setTabSize(4);     // 设置tab键的长度
        area.setLineWrap(true); // 设置自动换行
        area.setWrapStyleWord(true);    // 设置断行不断字
        area.setBackground(Color.WHITE);    // 设置聊天记录显示框背景颜色
        area.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
    }

    /**
     * 添加组件
     *
     * @param frame      窗口
     * @param component  组件
     * @param gridx      组件所在列
     * @param gridy      组件所在行
     * @param gridwidth  组件所占列数
     * @param gridheight 组件所占行数
     * @param weightx    x方向权重
     * @param weighty    y方向权重
     */
    private static void addComponent(@NotNull JFrame frame, JComponent component, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty) {
        constraints.gridx = gridx;  // 设置组件所在列
        constraints.gridy = gridy;  // 设置组件所在行
        constraints.gridwidth = gridwidth;  // 设置组件所占列数
        constraints.gridheight = gridheight;    // 设置组件所占行数
        constraints.weightx = weightx;  // 设置组件在水平方向上的拉伸比例
        constraints.weighty = weighty;  // 设置组件在垂直方向上的拉伸比例
        constraints.anchor = GridBagConstraints.CENTER; // 设置组件对齐方式
        constraints.fill = GridBagConstraints.BOTH; // 设置填充方式
        gridBagLayout.setConstraints(component, constraints); // 设置组件
        frame.add(component);   // 添加组件
    }

    public static void main(String[] args){
        new ChatFrame();
    }
}
