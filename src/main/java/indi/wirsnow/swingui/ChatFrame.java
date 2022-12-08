package indi.wirsnow.swingui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author : wirsnow
 * @date : 2022/12/7 11:07
 * @description: 实现客户端聊天框显示
 */
public class ChatFrame implements ActionListener {
    /*
    该类主要利用swing图形化相关技术，设计聊天软件的UI界面，
    UI界面要求至少有一个聊天记录显示框、文字输入框和发送按钮，
    UI界面设计要求简洁美观，功能上简单易学，致于设计成何种样式不作具体的限制，可自由发挥。
    聊天记录显示框可以显示客户端和服务器端双方的聊天记录，
    聊天记录包括发送的信息的人、时间以及发送的信息内容，可以正确地显示发送信息的顺序。
    点击发送按钮可以将输入的信息发送并显示出来，输入框信息为空时，点击发送按钮，程序不作反应。
    */

    private static final JFrame frame = new JFrame("聊天窗口");  // 创建窗口
    private static final JButton sendButton = new JButton("发送");// 发送按钮
    private static final JButton sendAudioButton = new JButton();   // 发送语音按钮
    private static final JButton sendFileButton = new JButton();    // 发送文件按钮
    private static final JButton screenshotsButton = new JButton(); // 截图按钮
    private static final JTextArea textArea = new JTextArea();      // 聊天记录显示框
    private static final JScrollPane scrollPane = new JScrollPane(textArea); //滚动条
    private static final JTextField textField = new JTextField();   // 文字输入框
    private static final JPanel userListPanel = new JPanel();       // 用户列表面板
    private static String sender;                                   // 发送者
    private static String receiver;                                 // 接收者
    private static String message;                                  // 消息
    private static String time;                                     // 时间

    public ChatFrame(){
        frame.setSize(800, 600);    // 设置窗口大小
        frame.setLocationRelativeTo(null);  // 设置窗口居中
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭方式
        frame.setLayout(null);  // 设置窗口布局为绝对布局
        frame.setResizable(false);  // 设置窗口不可改变大小

        sendButton.setBounds(600, 500, 80, 30); // 设置发送按钮位置和大小
        sendButton.addActionListener(this); // 添加监听事件
        sendButton.setActionCommand("send"); // 设置监听事件的命令
        frame.add(sendButton);  // 添加发送按钮

        sendAudioButton.setBounds(700, 500, 80, 30); // 设置发送语音按钮位置和大小
        sendAudioButton.addActionListener(this); // 添加监听事件
        sendAudioButton.setActionCommand("sendAudio"); // 设置监听事件的命令
        frame.add(sendAudioButton); // 添加发送语音按钮

        sendFileButton.setBounds(600, 550, 80, 30); // 设置发送文件按钮位置和大小
        sendFileButton.addActionListener(this); // 添加监听事件
        sendFileButton.setActionCommand("sendFile"); // 设置监听事件的命令
        frame.add(sendFileButton);  // 添加发送文件按钮

        screenshotsButton.setBounds(700, 550, 80, 30); // 设置截图按钮位置和大小
        screenshotsButton.addActionListener(this); // 添加监听事件
        screenshotsButton.setActionCommand("screenshots"); // 设置监听事件的命令
        frame.add(screenshotsButton);   // 添加截图按钮

        textArea.setBounds(0, 0, 600, 500); // 设置聊天记录显示框位置和大小
        textArea.setEditable(false);    // 设置聊天记录显示框不可编辑
        textArea.setLineWrap(true); // 设置自动换行
        frame.add(scrollPane);  // 添加滚动条

        textField.setBounds(0, 500, 600, 30); // 设置文字输入框位置和大小
        frame.add(textField);   // 添加文字输入框

        userListPanel.setBounds(600, 0, 200, 500);
    }
    public static void main(String[] args) {
        new ChatFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
