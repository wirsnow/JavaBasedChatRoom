package indi.wirsnow.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import indi.wirsnow.swingui.listener.ChatClientFrameListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author : wirsnow
 * @date : 2023/1/1 1:20
 * @description : 用于创建客户端的右侧功能面板
 */
public class ChatClientFrame {
    //显示在线人数
    private static final JTextField onlineNum = new JTextField("在线人数: 0");
    private static final JList<String> list = new JList<>();
    private static final JScrollPane scrollPane = new JScrollPane(list);
    private static final JButton offlineButton = new JButton("离线");

    static {
        FlatIntelliJLaf.setup();
    }

    public static void setFunctionPanel(JPanel functionPanel) {
        GridBagLayout gridBagLayout = new GridBagLayout();                  // 创建网格布局
        GridBagConstraints gridBagConstraints = new GridBagConstraints();   // 创建网格约束
        functionPanel.setLayout(gridBagLayout);              // 设置布局

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);




    }
    private static void addUserList(String onlineUser){
        JButton user = new JButton(onlineUser);

        user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

        list.add(user);
    }
}
