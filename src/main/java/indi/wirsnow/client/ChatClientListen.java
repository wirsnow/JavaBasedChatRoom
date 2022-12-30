package indi.wirsnow.client;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author : wirsnow
 * @date : 2022/12/27 20:23
 * @description : 监听客户端收到的消息
 */
public class ChatClientListen implements Runnable {
    private final ObjectInputStream ois;

    public ChatClientListen(ObjectInputStream ois) {
        this.ois = ois;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(ois.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
