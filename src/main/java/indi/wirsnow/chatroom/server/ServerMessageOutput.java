package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author : wirsnow
 * @date : 2023/1/17 21:28
 * @description : 服务端发送消息类
 */
public class ServerMessageOutput implements Runnable {
    private final ChatUniversalData chatUniversalData;

    public ServerMessageOutput(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
    }


    /**
     *
     */
    @Override
    public void run() {
    }
}

