package indi.wirsnow.client;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author : wirsnow
 * @date : 2022/12/27 20:23
 * @description : 监听客户端收到的消息
 */
public class ChatClientListen implements Runnable {
    private final Socket socket;

    public ChatClientListen(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                System.out.println(objectInputStream.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
