package indi.wirsnow.chatroom.swingui.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author : wirsnow
 * @date : 2023/1/2 0:15
 * @description : 接收键盘焦点的侦听器
 */
public class ChatFocusListener implements FocusListener {
    private final String string;
    private final JTextField textField;

    /**
     * 构造方法
     *
     * @param textField 文本框
     * @param string    提示文字
     */
    public ChatFocusListener(JTextField textField, String string) {
        this.textField = textField;
        this.string = string;
    }

    /**
     * 重写焦点获取事件
     *
     * @param e 事件
     */
    @Override
    public void focusGained(FocusEvent e) {
        // 获取焦点时，清空文本框并设置用户输入的字体颜色为黑色
        if (textField.getText().equals(string)) {
            textField.setText("");
            textField.setForeground(Color.black);
        }
    }

    /**
     * 重写焦点失去事件
     *
     * @param e 事件
     */
    @Override
    public void focusLost(FocusEvent e) {
        // 失去焦点时，判断文本框内容是否为空，为空则显示提示文字并设置字体颜色为灰色
        if (textField.getText().equals("")) {
            textField.setForeground(Color.gray);
            textField.setText(string);
        }
    }
}
