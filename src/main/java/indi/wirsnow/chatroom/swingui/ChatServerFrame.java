package indi.wirsnow.chatroom.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import indi.wirsnow.chatroom.swingui.listener.ChatFocusListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * @author : wirsnow
 * @date : 2023/1/1 1:19
 * @description : 用于创建服务端的右侧功能面板
 */
public class ChatServerFrame extends ChatFrame{
    private final Map<String, Socket> map;

    static {
        FlatIntelliJLaf.setup();
    }

    /**
     * 构造方法
     *
     * @param userName    用户名
     * @param messageArea 聊天记录显示框
     * @param editorArea  文字输入框
     * @param map         用户信息
     */
    public ChatServerFrame(String userName,
                           JTextArea messageArea,
                           JTextArea editorArea,
                           Map<String, Socket> map) throws IOException {
        super(userName, messageArea, editorArea, map);
        this.map = map;
        init();
    }

    public void init() {
        // 连接按钮、断开按钮、IP栏、端口栏
        JButton connectButton = new JButton("连接");
        JButton disconnectButton = new JButton("断开");
        JTextField ipField = new JTextField("127.0.0.1");
        JTextField portField = new JTextField("56448");
        JTextField userField = new JTextField("当前在线: 0人");  // 在线人数
        JList<JButton> userList = new JList<>();  // 在线用户列表


        GridBagLayout gridBagLayout = new GridBagLayout();                  // 创建网格布局
        GridBagConstraints gridBagConstraints = new GridBagConstraints();   // 创建网格约束
        gridBagLayout.rowHeights = new int[]{30, 30, 30, 130}; // 设置最小行高
        functionPanel.setLayout(gridBagLayout);

        // 添加ip与端口输入框
        {
            ipField.setBackground(Color.WHITE);    // 设置IP框背景颜色
            ipField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
            ipField.addFocusListener(new ChatFocusListener(ipField, "请输入ip地址")); // 添加焦点监听器
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, ipField, 0, 0, 1, 1, 1, 0); // 添加组件

            portField.setBackground(Color.WHITE);    // 设置端口框背景颜色
            portField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
            portField.addFocusListener(new ChatFocusListener(portField, "请输入端口号")); // 添加焦点监听器
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, portField, 1, 0, 1, 1, 1, 0); // 添加组件
        }

        // 添加连接与断开按钮
        {
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, connectButton, 0, 1, 1, 1, 1, 0); // 添加组件
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, disconnectButton, 1, 1, 1, 1, 1, 0); // 添加组件
        }

        // 添加在线人数显示
        {
            userField.setEditable(false);   // 设置不可编辑
            userField.setBorder(null);    // 设置无边框
            userField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, userField, 0, 2, 2, 1, 1, 0); // 添加组件
        }

        // 添加用户列表
        {
            userList.setBackground(Color.WHITE);    // 设置背景颜色
            userList.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
            userList.setBorder(null);    // 设置无边框
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, userList, 0, 3, 2, 1, 1, 1); // 添加组件
        }

        frame.setVisible(true);
    }
    public static void main(String[] args) throws IOException {
        new ChatServerFrame("服务器", new JTextArea(), new JTextArea(), null);
    }
}