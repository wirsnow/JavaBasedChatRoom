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
import java.awt.image.MultiResolutionImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static indi.wirsnow.chatroom.util.ChatUniversalUtil.*;

/**
 * @author : wirsnow
 * @date : 2022/12/10 14:53
 * @description : 接收动作事件的侦听器
 */
public class ChatFrameListener implements ActionListener {
    private final JTextPane messagePane;
    private final JTextArea editorArea;
    private final ChatUniversalData chatUniversalData;
    private final ServerMessageOutput serverMessageOutput = new ServerMessageOutput();
    private final ClientMessageOutput clientMessageOutput = new ClientMessageOutput();


    public ChatFrameListener(ChatUniversalData chatUniversalData) {
        this.messagePane = chatUniversalData.getMessagePane();
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
        if (isNotChooseRightUser(chatUniversalData)) return;

        Robot robot = new Robot();
        //获取屏幕大小
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()); // width=1549,height=871,小于正常值
        MultiResolutionImage multiResolutionImage = robot.createMultiResolutionScreenCapture(rectangle);
        java.util.List<Image> resolutionVariants = multiResolutionImage.getResolutionVariants();
        BufferedImage image = (BufferedImage) resolutionVariants.get(1);

        String userName = chatUniversalData.getUserName();
        String toUserName = chatUniversalData.getToUserName();
        String thisPath = System.getProperty("user.dir") + "/" + userName + "/myScreenshots";

        File dir = new File(thisPath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        // 如果存在先删除
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);

        File screenTemp = new File(thisPath + "/temp_" + date.getTime() + ".png");
        ImageIO.write(image, "png", screenTemp);

        String fileName = "screenshots_" + date.getTime() + ".png";
        String base64 = fileToBase64(screenTemp);

        if (Objects.equals(base64, "") || base64 == null) base64 = "0";
        try {
            if (Objects.equals(userName, "Server")) {
                serverMessageOutput.sendScreen(chatUniversalData, toUserName, fileName, base64);
            } else {
                clientMessageOutput.sendScreen(chatUniversalData, toUserName, fileName, base64);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageInsertText(messagePane, userName + " " + time + " -> " + toUserName + "\n");    //将消息发送到显示框(本地)
        messageInsertImage(messagePane, screenTemp);    //将图片发送到显示框(本地)
    }

    /**
     * 发送语音
     *
     * @throws LineUnavailableException 语音异常
     * @throws IOException              保存异常
     */
    private void sendAudio() throws LineUnavailableException, IOException {
        if (isNotChooseRightUser(chatUniversalData)) return;
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
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        String result = e.getActionCommand();
        switch (result) {
            case "send" -> sendText();
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
            case "sendFile" -> sendFile();
            case "connect" -> {
                if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
                    serverConnect();
                } else {
                    clientConnect();
                }
            }
            case "disconnect" -> {
                if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
                    serverDisconnect();
                } else {
                    clientDisconnect();
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + result);
        }
    }

    /**
     * 发送消息
     */
    private void sendText() {
        if (isNotChooseRightUser(chatUniversalData)) return;
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);
        String message = editorArea.getText();   //获取输入框的内容
        String toUserName = chatUniversalData.getToUserName();

        if ("".equals(message.strip())) {
            // 如果输入框为空
            JOptionPane.showMessageDialog(null, "消息不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
            editorArea.setText("");   //清空输入框
            return;
        } else if (message.length() > 1000) {
            // 如果输入框内容超过1000个字符
            String tip = "消息长度不能超过1000\n请删除" + (message.length() - 1000) + "个字";
            JOptionPane.showMessageDialog(null, tip, "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        editorArea.setText("");   //清空输入框

        String userName = chatUniversalData.getUserName();
        String text = userName + " " + time + " -> " + toUserName + "\n" + message + "\n";   //拼接消息
        messageInsertText(messagePane, text);    //将消息发送到显示框(本地)

        try {
            if (Objects.equals(userName, "Server")) {
                serverMessageOutput.sendText(chatUniversalData, toUserName, message);
            } else {
                clientMessageOutput.sendText(chatUniversalData, toUserName, message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送文件
     */
    private void sendFile() {
        if (isNotChooseRightUser(chatUniversalData)) return;

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

        Date date = new Date();
        String fileName = file.getName();
        String base64 = fileToBase64(file);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);
        String userName = chatUniversalData.getUserName();
        String toUserName = chatUniversalData.getToUserName();
        if (Objects.equals(base64, "") || base64 == null) base64 = "0";

        try {
            if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
                serverMessageOutput.sendFile(chatUniversalData, toUserName, fileName, base64);
            } else {
                clientMessageOutput.sendFile(chatUniversalData, toUserName, fileName, base64);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageInsertText(messagePane, userName + " " + time + " -> " + toUserName + "\n已发送文件: " + fileName + "\n");    //将消息发送到显示框(本地)
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
    private void clientConnect() {
        if (chatUniversalData.getConnected()) {
            JOptionPane.showMessageDialog(null, "客户端已连接，请勿重复点击", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            new ClientThreadStart(chatUniversalData);
        }
    }

    /**
     * 服务器断开网络
     */
    private void serverDisconnect() {
        if (!chatUniversalData.getConnected()) {
            JOptionPane.showMessageDialog(null, "服务器已断开，请勿重复点击", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            chatUniversalData.setConnected(false);
            chatUniversalData.getUserList().setModel(new DefaultListModel<>());
            chatUniversalData.getUserField().setText("当前在线: 0");
        }
    }

    /**
     * 客户端断开连接
     */
    private void clientDisconnect() {
        if (!chatUniversalData.getConnected()) {
            JOptionPane.showMessageDialog(null, "客户端已断开，请勿重复点击", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            chatUniversalData.setConnected(false);
            chatUniversalData.getUserList().setModel(new DefaultListModel<>());
            chatUniversalData.getUserField().setText("当前在线: 0");
            try {
                clientMessageOutput.sendDisconnectMessage(chatUniversalData);
            } catch (IOException ignored) {
            }
        }

    }


}
