package indi.wirsnow.chatroom.swingui.listener;

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
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : wirsnow
 * @date : 2022/12/10 14:53
 * @description : ChatFrame的监听器
 */
public class ChatFrameListener implements ActionListener {
    private final String userName;
    private final JTextArea messageArea;
    private final JTextArea editorArea;
    private final ObjectOutputStream oos;


    public ChatFrameListener(String userName, JTextArea messageArea, JTextArea editorArea, ObjectOutputStream oos) {
        this.userName = userName;
        this.messageArea = messageArea;
        this.editorArea = editorArea;
        this.oos = oos;
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
        oos.writeObject("file:" + file);
        oos.flush();
        messageArea.append(oos.toString());
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        String result = e.getActionCommand();
        switch (result) {
            case "send" -> {
                try {
                    send();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
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
            default -> throw new IllegalStateException("Unexpected value: " + result);
        }
    }

    /**
     * 发送消息
     */
    private void send() throws IOException {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");    //设置日期格式
        String time = dateFormat.format(date);
        String message = editorArea.getText();   //获取输入框的内容
        editorArea.setText("");   //清空输入框
        if ("".equals(message)) {   //如果输入框为空，不发送消息
            return;
        }
        String text = userName + " " + time + "\n" + message;       //拼接消息
        try {
            messageArea.append(text + "\n");    //将消息发送到显示框(本地)
            oos.writeObject("to://" + userName + "://" + message);   //将消息发送到服务器
            oos.flush();    //刷新流
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
