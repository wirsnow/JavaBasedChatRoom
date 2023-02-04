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
import java.util.List;
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

    /**
     * 构造方法
     *
     * @param chatUniversalData 数据类
     */
    public ChatFrameListener(ChatUniversalData chatUniversalData) {
        this.messagePane = chatUniversalData.getMessagePane();
        this.editorArea = chatUniversalData.getEditorArea();
        this.chatUniversalData = chatUniversalData;
    }

    /**
     * 重写动作事件
     *
     * @param e 事件
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        String result = e.getActionCommand();
        switch (result) {
            case "send" -> sendText();              // 发送消息
            case "screenshots" -> screenshots();    // 发送截图
            case "sendAudio" -> sendAudio();        // 发送语音
            case "sendFile" -> sendFile();          // 发送文件
            case "connect" -> {                     // 连接
                if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
                    serverConnect();
                } else {
                    clientConnect();
                }
            }
            case "disconnect" -> {                  // 断开连接
                if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
                    serverDisconnect();
                } else {
                    clientDisconnect();
                }
            }
            default -> throw new IllegalStateException("错误的值: " + result);
        }
    }

    /**
     * 发送消息
     */
    private void sendText() {
        // 判断是否选择了正确的用户
        if (isNotChooseRightUser(chatUniversalData)) return;

        // 获取当前时间和输入框的内容
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = dateFormat.format(date);
        String message = editorArea.getText();   //获取输入框的内容
        String toUserName = chatUniversalData.getToUserName();

        if ("".equals(message.strip())) {
            // 如果输入框为回车、空格等, 提示用户并清空输入框
            JOptionPane.showMessageDialog(null, "消息不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
            editorArea.setText("");
            return;
        } else if (message.length() > 1000) {
            // 如果输入框内容超过1000个字符
            String tip = "消息长度不能超过1000\n请删除" + (message.length() - 1000) + "个字";
            JOptionPane.showMessageDialog(null, tip, "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        editorArea.setText("");

        // 拼接消息并插入消息到消息面板
        String userName = chatUniversalData.getUserName();
        String text = userName + " " + time + " -> " + toUserName + "\n" + message + "\n";
        messageInsertText(messagePane, text);

        // 调用对应的发送消息方法
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
     * 截图
     */
    private void screenshots() {
        // 判断是否选择了正确的用户
        if (isNotChooseRightUser(chatUniversalData)) return;

        // 利用robot截图
        BufferedImage image = null;
        try {
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            // 获取高分屏的截图
            MultiResolutionImage multiResolutionImage = robot.createMultiResolutionScreenCapture(rectangle);
            List<Image> resolutionVariants = multiResolutionImage.getResolutionVariants();
            // 获取第二个截图, 第一个截图为低分辨率
            image = (BufferedImage) resolutionVariants.get(1);
        } catch (Exception ignored) {
        }

        // 获取用户名与路径
        String userName = chatUniversalData.getUserName();
        String toUserName = chatUniversalData.getToUserName();
        String thisPath = System.getProperty("user.dir") + "/" + userName + "/myScreenshots";
        // 如果文件夹不存在，创建
        File dir = new File(thisPath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }

        // 获取当前时间
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);

        // 将截图保存到本地
        File screenTemp = new File(thisPath + "/temp_" + date.getTime() + ".png");
        try {
            ImageIO.write(image, "png", screenTemp);
        } catch (IOException ignored) {
        }

        // 将截图转为base64
        String fileName = "screenshots_" + date.getTime() + ".png";
        String base64 = fileToBase64(screenTemp);
        if (Objects.equals(base64, "") || base64 == null) base64 = "0";
        // 调用对应的发送截图方法
        try {
            if (Objects.equals(userName, "Server")) {
                serverMessageOutput.sendScreen(chatUniversalData, toUserName, fileName, base64);
            } else {
                clientMessageOutput.sendScreen(chatUniversalData, toUserName, fileName, base64);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 插入消息和图片到消息面板
        messageInsertText(messagePane, userName + " " + time + " -> " + toUserName + "\n");
        messageInsertImage(messagePane, screenTemp);
    }

    /**
     * 发送语音
     */
    private void sendAudio() {
        // 判断是否选择了正确的用户
        if (isNotChooseRightUser(chatUniversalData)) return;

        // 提示正在录音
        JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);             // 设置窗口置顶
        dialog.setTitle("录音中");                // 设置标题
        dialog.setSize(250, 100);   // 设置大小
        dialog.setLocationRelativeTo(null);         // 设置窗口居中
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);          // 设置关闭窗口时释放资源
        dialog.setLayout(new FlowLayout());         // 设置布局
        JLabel label = new JLabel("正在录音，已录制0秒", JLabel.CENTER);   // 提示正在录音

        // 停止录音按钮
        JButton stopButton = new JButton("停止");
        stopButton.addActionListener(e -> dialog.dispose());

        // 添加组件
        dialog.add(label);
        dialog.add(stopButton);
        dialog.setVisible(true);

        try {
            // 设置音频格式
            AudioFormat format = new AudioFormat(8000F, 16, 2, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

            // 获取当前时间、用户名、路径
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
            String time = dateFormat.format(date);
            String userName = chatUniversalData.getUserName();
            String toUserName = chatUniversalData.getToUserName();
            String filename = "audio_" + date.getTime() + ".wav";
            String thisPath = System.getProperty("user.dir") + "\\" + userName + "\\audio\\";

            // 如果文件夹不存在，创建
            File file = new File(thisPath + filename);
            File dir = new File(thisPath);
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }

            // 开启录音线程
            new Thread(() -> {
                try {
                    // 开启录音
                    targetDataLine.open(format);
                    targetDataLine.start();
                    AudioSystem.write(new AudioInputStream(targetDataLine), AudioFileFormat.Type.WAVE, file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    JOptionPane.showMessageDialog(null, "请检查音频输入设备/录音权限是否开启", "错误", JOptionPane.ERROR_MESSAGE);
                    dialog.setVisible(false);
                }
            }).start();


            new Thread(() -> {
                // 动态显示录制时间
                int audioTime = 0;
                while (dialog.isVisible()) {
                    // 设置提示信息
                    label.setText("正在录音，已录制" + audioTime + "秒");
                    try {
                        audioTime++;
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    // 如果录制时间超过三分钟，提示并停止录音
                    if (audioTime > 180) {
                        JOptionPane.showMessageDialog(null, "不能录制超过三分钟！", "警告", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        break;
                    }
                }

                // 停止录音并转换为base64
                targetDataLine.stop();
                targetDataLine.close();
                String base64 = fileToBase64(file);

                // 调用对应的发送录音方法
                try {
                    if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
                        serverMessageOutput.sendAudio(chatUniversalData, toUserName, filename, base64);
                    } else {
                        clientMessageOutput.sendAudio(chatUniversalData, toUserName, filename, base64);
                    }
                    // 插入消息到消息面板
                    messageInsertText(messagePane, userName + " " + time + " -> " + toUserName + "\n已发送录音: " + filename + "\n");    //将消息发送到显示框(本地)
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } catch (Exception ignored) {
        }
    }

    /**
     * 发送文件
     */
    private void sendFile() {
        // 判断是否选择了正确的用户
        if (isNotChooseRightUser(chatUniversalData)) return;

        // 文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);   //设置只能选择文件
        fileChooser.showDialog(new JLabel(), "选择");
        File file = fileChooser.getSelectedFile();  //获取选择的文件

        // 判断文件大小
        if (file == null) {
            JOptionPane.showMessageDialog(null, "请重新选择文件", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (file.length() > 1024 * 1024 * 100) {
            // 此处为了保险，限制文件大小为100M，还没详细测试过
            JOptionPane.showMessageDialog(null, "文件大小不能超过100M", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 获取日期
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);
        // 获取文件名和其他信息
        String fileName = file.getName();
        String userName = chatUniversalData.getUserName();
        String toUserName = chatUniversalData.getToUserName();
        // 转换base64
        String base64 = fileToBase64(file);
        if (Objects.equals(base64, "") || base64 == null) base64 = "0";

        // 调用对应的发送文件方法
        try {
            if (Objects.equals(chatUniversalData.getUserName(), "Server")) {
                serverMessageOutput.sendFile(chatUniversalData, toUserName, fileName, base64);
            } else {
                clientMessageOutput.sendFile(chatUniversalData, toUserName, fileName, base64);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 插入消息到消息面板
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
            // 清空用户列表并调用断开连接方法
            chatUniversalData.setConnected(false);
            chatUniversalData.getUserList().setModel(new DefaultListModel<>());
            chatUniversalData.getUserField().setText("当前在线: 0");
            try {
                serverMessageOutput.sendDisconnectMessage(chatUniversalData);
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * 客户端断开连接
     */
    private void clientDisconnect() {
        if (!chatUniversalData.getConnected()) {
            JOptionPane.showMessageDialog(null, "客户端已断开，请勿重复点击", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            // 清空用户列表并调用断开连接方法
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
