package indi.wirsnow.client;

import com.alibaba.fastjson2.JSONObject;

import java.io.ObjectOutputStream;

/**
 * @author : wirsnow
 * @date : 2022/12/27 20:55
 * @description : 验证连接是否存在
 */
public class ChatClientHeart implements Runnable {

    private final ObjectOutputStream objectOutputStream;

    public ChatClientHeart(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5000);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "heart");
                jsonObject.put("sender", "heart");
                jsonObject.put("message", "heart");
                objectOutputStream.writeObject(jsonObject);
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

