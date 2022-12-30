package indi.wirsnow.client;

import com.alibaba.fastjson2.JSONObject;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author : wirsnow
 * @date : 2022/12/27 20:28
 * @description : 发送消息
 */
public class ChatClientOutput implements Runnable {
    private static String message;

    private final String sender;
    private final Socket socket;

    public ChatClientOutput(Socket socket, String sender) {
        this.socket = socket;
        this.sender = sender;
    }

    public static void setMessage(String me) {
        message = me;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                if(message != null){
                    System.out.println(message);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "client");
                    jsonObject.put("sender", sender);
                    jsonObject.put("message", message);
                    objectOutputStream.writeObject(jsonObject);
                    objectOutputStream.flush();
                    message = null;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
