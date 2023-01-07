package indi.wirsnow.chatroom.swingui;

import indi.wirsnow.chatroom.swingui.listener.ChatFocusListener;
import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;

import javax.swing.*;
import java.awt.*;

/**
 * @author : wirsnow
 * @date : 2022/12/9 2:55
 * @description: 实现登录框
 */
public class ChatLoginFrame {
    private String userName = null;

    public void login(JFrame mFrame, ChatFrameListener listener) {
        JFrame frame = new JFrame("登录");
        JTextField userNameField = new JTextField();
        JButton cancelButton = new JButton("取消");
        JButton loginButton = new JButton("登录");

        // 登录框
        frame.setResizable(false);
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // 设置窗口居中
        frame.setLayout(new FlowLayout());

        // 用户名输入框
        userNameField.setPreferredSize(new Dimension(200, 30));
        userNameField.addFocusListener(new ChatFocusListener(userNameField, "请输入用户名"));

        // 取消按钮
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.addActionListener(e -> System.exit(0));

        // 登录按钮
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(e -> {
            String userName = userNameField.getText();
            if (userName.equals("")) {
                JOptionPane.showMessageDialog(frame, "用户名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                this.userName = userName;
                listener.setUserName(this.userName);
                frame.dispose();
                mFrame.setVisible(true);
            }
        });

        // 添加组件
        frame.add(userNameField);
        frame.add(cancelButton);
        frame.add(loginButton);

        // 显示窗口
        frame.setVisible(true);
    }
}
