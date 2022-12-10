package indi.wirsnow.swingui.listener;

import indi.wirsnow.swingui.BaseFrame;
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

/**
 * @author : wirsnow
 * @date : 2022/12/10 14:53
 * @description : 聊天框监听器
 */
public class FrameListener implements ActionListener {

    /**
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        String result = e.getActionCommand();
        switch(result){
            case "send":
                send();
                break;
            case "screenshots":
                try {
                    screenshots();
                } catch (AWTException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "sendAudio":
                try {
                    sendAudio();
                } catch (LineUnavailableException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "sendFile":
                sendFile();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }
    }
    /**
     * 发送消息
     */
    private void send(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");    //设置日期格式
        BaseFrame.time = dateFormat.format(date);
        BaseFrame.message = BaseFrame.editorArea.getText();   //获取输入框的内容
        BaseFrame.editorArea.setText("");   //清空输入框
        if(BaseFrame.message.equals("")){   //如果输入框为空，不发送消息
            return;
        }
        String text = BaseFrame.sender + " "+ BaseFrame.time + "\n" + BaseFrame.message;    //设置聊天记录
        BaseFrame.messageArea.append(text + "\n");
    }

    /**
     * 截图
     * @throws AWTException 截图异常
     * @throws IOException 保存异常
     */
    private static void screenshots() throws AWTException, IOException {
        Robot robot = new Robot();
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage image = robot.createScreenCapture(rectangle);
        ImageIO.write(image, "png", new File("src\\main\\resources\\images\\screenshots.png"));
    }

    /**
     * 发送语音
     * @throws LineUnavailableException 语音异常
     * @throws IOException 保存异常
     */
    private static void sendAudio() throws LineUnavailableException, IOException {
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
    private static void sendFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.showDialog(new JLabel(), "选择");
        File file = fileChooser.getSelectedFile();
        if (file.isDirectory()) {
            System.out.println("文件夹:" + file.getAbsolutePath());
        } else if (file.isFile()) {
            System.out.println("文件:" + file.getAbsolutePath());
        }
        System.out.println("文件名:" + file.getName());
    }
}
