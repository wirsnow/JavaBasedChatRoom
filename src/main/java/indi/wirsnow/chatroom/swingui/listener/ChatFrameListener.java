package indi.wirsnow.chatroom.swingui.listener;

import indi.wirsnow.chatroom.client.ClientMessageOutput;
import indi.wirsnow.chatroom.client.ClientThreadStart;
import indi.wirsnow.chatroom.server.ServerMessageOutput;
import indi.wirsnow.chatroom.server.ServerThreadStart;
import indi.wirsnow.chatroom.util.ChatUniversalData;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author : wirsnow
 * @date : 2022/12/10 14:53
 * @description : 接收动作事件的侦听器
 */
public class ChatFrameListener implements ActionListener {
    private final JTextArea messageArea;
    private final JTextArea editorArea;
    private final ChatUniversalData chatUniversalData;


    public ChatFrameListener(ChatUniversalData chatUniversalData) {
        this.messageArea = chatUniversalData.getMessageArea();
        this.editorArea = chatUniversalData.getEditorArea();
        this.chatUniversalData = chatUniversalData;
    }

    /**
     * 截图
     *
     * @throws AWTException 截图异常
     * @throws IOException  保存异常
     */
    private void screenshots() throws AWTException, IOException {
        Robot robot = new Robot();
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage image = robot.createScreenCapture(rectangle);
        ImageIO.write(image, "png", new File("src\\main\\resources\\images\\screenshots.png"));
    }

    /**
     * 发送语音
     *
     * @throws LineUnavailableException 语音异常
     * @throws IOException              保存异常
     */
    private void sendAudio() throws LineUnavailableException, IOException {
        AudioFormat format = new AudioFormat(8000F, 16, 2, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        targetDataLine.open(format);
        targetDataLine.start();
        AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
        File audioFile = new File("src\\main\\resources\\audio\\audio.wav");
        AudioSystem.write(audioInputStream, fileType, audioFile);
        targetDataLine.close();
    }

    /**
     * 发送文件
     */
    private void sendFile() throws IOException {

        JFileChooser fileChooser = new JFileChooser();  //创建文件选择器
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);   //设置只能选择文件
        fileChooser.showDialog(new JLabel(), "选择");  //打开文件选择器
        File file = fileChooser.getSelectedFile();  //获取选择的文件
        if (file == null) {
            return;
        } else if (file.length() > 1024 * 1024 * 100) {
            JOptionPane.showMessageDialog(null, "文件大小不能超过100M", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        chatUniversalData.out.println("file://" + file);
        messageArea.append(chatUniversalData.out.toString());
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        String result = e.getActionCommand();
        switch (result) {
            case "send" -> send();
            case "screenshots" -> {
                try {
                    screenshots();
                } catch (AWTException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case "sendAudio" -> {
                try {
                    sendAudio();
                } catch (LineUnavailableException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case "sendFile" -> {
                try {
                    sendFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case "connect" -> {
                if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
                    serverConnect();
                } else {
                    client2server();
                }
            }
            case "disconnect" -> disconnect();
            default -> throw new IllegalStateException("Unexpected value: " + result);
        }
    }

    /**
     * 服务器连接到网络
     */
    private void serverConnect() {
        if (chatUniversalData.getConnected()) {
            JOptionPane.showMessageDialog(null, "服务器已连接，请勿重复点击", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            new ServerThreadStart(chatUniversalData);
        }
    }

    /**
     * 客户端连接到服务器
     */
    private void client2server() {
        if (chatUniversalData.getConnected()) {
            JOptionPane.showMessageDialog(null, "客户端已连接，请勿重复点击", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            new ClientThreadStart(chatUniversalData);
        }
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        chatUniversalData.setConnected(false);
    }

    /**
     * 发送消息
     */
    private void send() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);
        String message = editorArea.getText();   //获取输入框的内容
        String toUserName = chatUniversalData.getToUserName();

        if (Objects.equals(chatUniversalData.getToUserName(), null)) {
            // 如果未选择发送对象
            JOptionPane.showMessageDialog(null, "请选择聊天对象", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }else if (Objects.equals(chatUniversalData.getToUserName(), chatUniversalData.getUserName())) {
            // 如果选择的用户是自己
            JOptionPane.showMessageDialog(null, "不能选择自己", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }else if ("".equals(message.strip())) {
            // 如果输入框为空
            JOptionPane.showMessageDialog(null, "消息不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
            editorArea.setText("");   //清空输入框
            return;
        }else if (message.length() > 1000) {
            // 如果输入框内容超过1000个字符
            String tip = "消息长度不能超过1000\n请删除" + (message.length() - 1000) + "个字";
            JOptionPane.showMessageDialog(null, tip, "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        editorArea.setText("");   //清空输入框

        String userName = chatUniversalData.getUserName();
        String text = userName + " " + time + "\n" + message;   //拼接消息
        messageArea.append(text + "\n");    //将消息发送到显示框(本地)

        try {
            if (Objects.equals(userName, "Server")) {
                ServerMessageOutput serverMessageOutput = new ServerMessageOutput();
                serverMessageOutput.sendMessage(chatUniversalData, toUserName, message);
            } else {
                ClientMessageOutput clientMessageOutput = new ClientMessageOutput();
                clientMessageOutput.sendMessage(chatUniversalData, toUserName, message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
