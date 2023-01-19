package indi.wirsnow.chatroom.swingui;

import indi.wirsnow.chatroom.swingui.listener.ChatFocusListener;
import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.awt.*;

/**
 * @author : wirsnow
 * @date : 2023/1/9 12:55
 * @description: 客户端登录框
 */
public class ChatLoginFrame {

    private final JFrame mFrame;
    private final ChatUniversalData chatUniversalData;

    public ChatLoginFrame(JFrame mFrame, ChatUniversalData chatUniversalData) {
        this.mFrame = mFrame;
        this.chatUniversalData = chatUniversalData;
    }

    public void login() {
        JFrame frame = new JFrame("登录");
        JLabel userNameTip = new JLabel("用户名:");
        JTextField userNameField = new JTextField();
        JLabel space = new JLabel(" ");
        JButton loginButton = new JButton("登录");
        JButton cancelButton = new JButton("取消");

        // 登录框
        frame.setResizable(false);
        frame.setSize(300, 130);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // 设置窗口居中
        frame.setLayout(new FlowLayout());

        // 用户名提示
        userNameTip.setPreferredSize(new Dimension(50, 30));

        // 用户名输入框
        userNameField.setPreferredSize(new Dimension(200, 30));
        userNameField.addFocusListener(new ChatFocusListener(userNameField, "请输入用户名"));

        // 空白
        space.setPreferredSize(new Dimension(300, 10));

        // 取消按钮
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.addActionListener(e -> System.exit(0));


        // 登录按钮
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.setVerticalAlignment(SwingConstants.BOTTOM);
        loginButton.addActionListener(e -> {
            String userName;
            userName = userNameField.getText();
            if (userName.equals("")) {
                JOptionPane.showMessageDialog(frame, "用户名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (userName.equals("Server") || userName.equals("Server-MyUserName") || userName.equals("所有人")) {
                JOptionPane.showMessageDialog(frame, "名称中含有非法字符", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                chatUniversalData.setUserName(userName);
                frame.dispose();
                mFrame.setTitle(userName);
                mFrame.setVisible(true);
            }
        });

        // 添加组件
        frame.add(userNameTip);
        frame.add(userNameField);
        frame.add(space);
        frame.add(loginButton);
        frame.add(cancelButton);

        // 显示窗口
        frame.setVisible(true);
    }
}
