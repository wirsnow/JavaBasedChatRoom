package indi.wirsnow.chatroom.swingui.listener;

import indi.wirsnow.chatroom.client.ChatClientThread;
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
import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author : wirsnow
 * @date : 2022/12/10 14:53
 * @description : ChatFrame的监听器
 */
public class ChatFrameListener implements ActionListener {
    private final JTextArea messageArea;
    private final JTextArea editorArea;
    private final Map<String, Socket> allOnlineUser;
    private final ChatUniversalData chatUniversalData;


    public ChatFrameListener(ChatUniversalData chatUniversalData) {
        this.messageArea = chatUniversalData.getMessageArea();
        this.editorArea = chatUniversalData.getEditorArea();
        this.allOnlineUser = chatUniversalData.getAllOnlineUser();
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
                    connect();
                } else {
                    connect2server();
                }
            }
            case "disconnect" -> chatUniversalData.setConnected(false);
            default -> throw new IllegalStateException("Unexpected value: " + result);
        }
    }

    /**
     * 服务器连接到网络
     */
    private void connect() {
        if (chatUniversalData.getConnected()) {
            JOptionPane.showMessageDialog(null, "服务器已连接网络，无需重复连接", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            new ServerThreadStart(chatUniversalData);
        }
    }

    /**
     * 客户端连接到服务器
     */
    private void connect2server() {
        if (chatUniversalData.getConnected()) {
            JOptionPane.showMessageDialog(null, "客户端已连接，请勿重复点击", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            new ChatClientThread(chatUniversalData);
        }
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        try {
            String userName = chatUniversalData.getUserName();
            Socket socket = allOnlineUser.get(userName);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("disconnect://" + userName);
            oos.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "断开连接失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
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

        //如果未输入内容，提示先输入内容
        if ("".equals(message)) {   //如果输入框为空，不发送消息
            JOptionPane.showMessageDialog(null, "消息不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //如果未选择用户，提示先选择用户
        if (Objects.equals(chatUniversalData.getToUserName(), null)) {
            JOptionPane.showMessageDialog(null, "请选择聊天对象", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //如果选择的用户是自己，提示不能选择自己
        if (Objects.equals(chatUniversalData.getToUserName(), chatUniversalData.getUserName())) {
            JOptionPane.showMessageDialog(null, "不能选择自己", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        editorArea.setText("");   //清空输入框

        String userName = chatUniversalData.getUserName();
        String text = userName + " " + time + "\n" + message;   //拼接消息
        messageArea.append(text + "\n");    //将消息发送到显示框(本地)


        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(chatUniversalData.getSocket().getOutputStream()), true);
            message = message.replace("\n", "\\/n").replace("\r", "\\/r");
            text = toUserName + "-to:" + "text://" + message;       //拼接消息
            System.out.println(text);
            writer.println(text);
//            String[] strs = message.split("\n");
//            if (strs.length == 1) {
//                text = toUserName + "-to:" + "text://" + message;       //拼接消息
//                System.out.println(text);
//                writer.println(text);
//            } else {
//                for (String string : strs) {
//                    text = toUserName + "-to:" + "texs://" + string;       //拼接消息
//                    System.out.println(text);
//                    writer.println(text);
//                }
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
