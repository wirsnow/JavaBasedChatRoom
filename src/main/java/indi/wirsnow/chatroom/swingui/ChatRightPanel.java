package indi.wirsnow.chatroom.swingui;

import indi.wirsnow.chatroom.swingui.listener.ChatFocusListener;
import indi.wirsnow.chatroom.swingui.listener.ChatFrameListener;
import indi.wirsnow.chatroom.util.ChatUniversalData;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.addGridBagComponent;

/**
 * @author : wirsnow
 * @date : 2023/1/7 19:57
 * @description : 右侧功能面板
 */
public class ChatRightPanel {
    private final JFrame frame;                 // 父窗口
    private final ChatFrameListener listener;   // 监听器
    private final ChatUniversalData chatUniversalData;  // 数据类
    private final JPanel functionPanel = new JPanel();  // 功能面板


    /**
     * 构造函数
     *
     * @param frame             父窗口
     * @param listener          监听器
     * @param chatUniversalData 数据类
     */
    public ChatRightPanel(JFrame frame, ChatFrameListener listener, ChatUniversalData chatUniversalData) {
        this.frame = frame;
        this.listener = listener;
        this.chatUniversalData = chatUniversalData;
    }

    /**
     * 创建右侧功能面板
     */
    public void createRightPanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();                  // 创建网格布局
        GridBagConstraints gridBagConstraints = new GridBagConstraints();   // 创建网格约束
        gridBagLayout.rowHeights = new int[]{30, 30, 30, 130};              // 设置最小行高
        functionPanel.setLayout(gridBagLayout);                             // 设置布局
        functionPanel.setMaximumSize(new Dimension(200, 100000)); // 设置最大大小
        functionPanel.setMinimumSize(new Dimension(150, 400)); // 设置最小大小

        JTextField ipField = chatUniversalData.getIpField();
        JTextField portField = chatUniversalData.getPortField();
        JTextField userField = chatUniversalData.getUserField(); // 在线人数
        JButton connectButton = new JButton("连接");      // 连接按钮
        JButton disconnectButton = new JButton("断开");   // 断开按钮
        JList<String> userList = chatUniversalData.getUserList();        // 用户列表
        JScrollPane userListPane = new JScrollPane(userList); // 列表滚动条

        // 添加ip与端口输入框
        {
            ipField.setBackground(Color.WHITE);     // 设置IP框背景颜色
            ipField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
            ipField.addFocusListener(new ChatFocusListener(ipField, "请输入ip地址")); // 添加焦点监听器

            portField.setBackground(Color.WHITE);   // 设置端口框背景颜色
            portField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
            portField.addFocusListener(new ChatFocusListener(portField, "请输入端口号")); // 添加焦点监听器
        }

        // 添加连接与断开按钮
        {
            connectButton.addActionListener(listener);  // 添加监听器
            connectButton.setActionCommand("connect");  // 设置命令

            disconnectButton.addActionListener(listener);       // 添加监听器
            disconnectButton.setActionCommand("disconnect");    // 设置命令
        }

        // 添加在线人数显示
        {
            userField.setEditable(false);   // 设置不可编辑
            userField.setBorder(null);      // 设置无边框
            userField.setFont(new Font("微软雅黑", Font.PLAIN, 14));    // 设置字体
        }

        // 添加用户列表
        {
            userList.setBackground(Color.WHITE);    // 设置背景颜色
            userList.setFont(new Font("微软雅黑", Font.PLAIN, 14));    // 设置字体
            userList.setBorder(null);               // 设置无边框
            userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     // 设置单选模式
            userList.addListSelectionListener(e -> {
                if (e.getValueIsAdjusting()) {
                    // 将选中的用户设置为聊天对象
                    int[] indices = userList.getSelectedIndices();
                    ListModel<String> listModel = userList.getModel();
                    chatUniversalData.setToUserName(listModel.getElementAt(indices[0]));
                }
            });
        }

        // 添加组件
        {
            // ip/端口输入框
            if (!Objects.equals(chatUniversalData.getUserName(), "Server")) {
                addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, ipField, 0, 0, 1, 1, 1, 0); // 添加组件
                addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, portField, 1, 0, 1, 1, 1, 0); // 添加组件
            } else {
                addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, portField, 0, 0, 2, 1, 1, 0); // 添加组件
            }
            // 连接/断开按钮
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, connectButton, 0, 1, 1, 1, 1, 0); // 添加组件
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, disconnectButton, 1, 1, 1, 1, 1, 0); // 添加组件
            // 在线人数/用户列表
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, userField, 0, 2, 2, 1, 1, 0); // 添加组件
            addGridBagComponent(functionPanel, gridBagLayout, gridBagConstraints, userListPane, 0, 3, 2, 1, 1, 1); // 添加组件
        }
        frame.add(functionPanel, BorderLayout.EAST);    // 添加聊天面板至父窗口
    }
}
