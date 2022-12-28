package indi.wirsnow.server;

import com.alibaba.fastjson2.JSONObject;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author : wirsnow
 * @date : 2022/12/27 20:01
 * @description : 实现服务端发送消息
 */
public class ChatServerOutput implements Runnable {
    private final Socket socket;
    private final String message = "";

    public ChatServerOutput(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "text");
                jsonObject.put("sender", "server");
                jsonObject.put("message", message);
                objectOutputStream.writeObject(jsonObject);
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}