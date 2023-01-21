package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

/**
 * @author : wirsnow
 * @date : 2023/1/17 21:28
 * @description : 服务端发送消息类
 */
public class ServerMessageOutput {
    public void sendMessage(ChatUniversalData chatUniversalData, String toUserName, String message) throws IOException {
        if (Objects.equals(toUserName, "所有人")) {
            for (String userName : chatUniversalData.getAllOnlineUser().keySet()) {
                Socket socket = chatUniversalData.getAllOnlineUser().get(userName);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                messageModify(writer, message);
            }
        } else {
            Socket socket = chatUniversalData.getAllOnlineUser().get(toUserName);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            messageModify(writer, message);
        }
    }

    private void messageModify(PrintWriter writer, String message) {
        String[] strs = message.split("\n");
        if (strs.length <= 1) {
            writer.println("Server-from:text://" + message);
        } else {
            writer.println("Server-from:texs://start");
            for (String string : strs) {
                switch (string) {
                    case "start" -> string = "startstart";
                    case "newline" -> string = "newlinenewline";
                    case "\n", "\r", "" -> string = "newline";
                }
                writer.println("Server-from:texs://" + string + "\n");
            }
        }
    }
}

