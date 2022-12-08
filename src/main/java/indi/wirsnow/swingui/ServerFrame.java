package indi.wirsnow.swingui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wirsnow
 * @date : 2022/12/8 19:37
 * @description:
 */
public class ServerFrame extends JFrame implements ActionListener {
    /*
      该类主要用于创建服务器端的图形界面
     */
    private final Map<Integer, Socket> clients = new HashMap<Integer, Socket>();
    private final JTextArea msg = new JTextArea("服务端");
    private final JButton msgSend = new JButton("发送");

    public ServerFrame() {
        this.setVisible(true);
        this.setSize(500, 600);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                System.exit(0);
            }
        });

        msgSend.addActionListener(this);
        msgSend.setActionCommand("sendMsg");
        msg.setAutoscrolls(true);
        msg.setColumns(40);
        msg.setRows(30);

        JScrollPane scrollPane = new JScrollPane(msg);
        this.add(scrollPane);
        this.add(msgSend);
    }

    public static void main(String[] args) {
        new ServerFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
