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

    public ChatFocusListener(JTextField textField, String string) {
        this.textField = textField;
        this.string = string;
    }

    @Override
    public void focusGained(FocusEvent e) {
        //获取焦点时，清空文本框
        if (textField.getText().equals(string)) {
            textField.setText("");     //将提示文字清空
            textField.setForeground(Color.black);  //设置用户输入的字体颜色为黑色
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        //失去焦点时，判断文本框内容是否为空，为空则显示提示文字
        if (textField.getText().equals("")) {
            textField.setForeground(Color.gray); //将提示文字设置为灰色
            textField.setText(string);     //显示提示文字
        }
    }
}
