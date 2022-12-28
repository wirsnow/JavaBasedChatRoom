package indi.wirsnow.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author : wirsnow
 * @date : 2022/12/8 19:24
 * @description : 监听服务端收到的消息
 */
public class ChatServerListen implements Runnable {
    private final Socket socket;

    public ChatServerListen(Socket socket) {
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
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
