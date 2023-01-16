package indi.wirsnow.chatroom.util;

import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author : wirsnow
 * @date : 2023/1/7 19:59
 * @description : 创建界面时需要用到的函数
 */
public class ChatUtil {
    /**
     * 刷新在线用户列表
     *
     * @param chatUniversalData 数据传输类
     */
    public static void flushUserList(ChatUniversalData chatUniversalData){
        DefaultListModel<String> dlm = new DefaultListModel<>();    // 创建列表模型
        if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
            dlm.add(0, "日志");
            dlm.add(1, "所有人");
        }
        dlm.addAll(chatUniversalData.getAllOnlineUser().keySet());  // 添加所有用户
        chatUniversalData.getUserList().setModel(dlm);              // 设置列表模型
    }

    /**
     * 立即刷新界面
     *
     * @param textArea  文本域
     * @param message   消息
     */
    public static void appendAndFlush(JTextArea textArea, String message) {
        textArea.append(message);
        textArea.paintImmediately(textArea.getBounds());
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
}
