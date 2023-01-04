package indi.wirsnow.chatroom.swingui.listener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author : wirsnow
 * @date : 2023/1/4 17:23
 * @description : ChatServerFrame的监视器
 */
public class ChatServerFrameListener implements ActionListener {
    private final JTextField ipField;
    private final JTextField portField;
    public ChatServerFrameListener(JTextField ipField, JTextField portField) {
        this.ipField = ipField;
        this.portField = portField;
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String result = e.getActionCommand();
        switch (result) {
            case "connect":
                connect();
            case "disconnect":
                disconnect();
            default:
                throw new IllegalStateException("Unexpected value: " + result);

        }

    }

    private void connect() {

    }

    private void disconnect() {

    }
}
