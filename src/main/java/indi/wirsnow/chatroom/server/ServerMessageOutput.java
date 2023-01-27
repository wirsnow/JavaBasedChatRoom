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
    public void sendAudio(ChatUniversalData chatUniversalData, String toUserName, String fileName, String base64) throws IOException {
        if (Objects.equals(toUserName, "所有人")) {
            for (String userName : chatUniversalData.getAllOnlineUser().keySet()) {
                Socket socket = chatUniversalData.getAllOnlineUser().get(userName);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                writer.println("Server-from:audi://" + fileName + "-name:" + base64);
            }
        } else {
            Socket socket = chatUniversalData.getAllOnlineUser().get(toUserName);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("Server-from:audi://" + fileName + "-name:" + base64);
        }
    }
    public void sendScreen(ChatUniversalData chatUniversalData, String toUserName, String fileName, String base64) throws IOException {
        if (Objects.equals(toUserName, "所有人")) {
            for (String userName : chatUniversalData.getAllOnlineUser().keySet()) {
                Socket socket = chatUniversalData.getAllOnlineUser().get(userName);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                writer.println("Server-from:icon://" + fileName + "-name:" + base64);
            }
        } else {
            Socket socket = chatUniversalData.getAllOnlineUser().get(toUserName);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("Server-from:icon://" + fileName + "-name:" + base64);
        }
    }
    public void sendFile(ChatUniversalData chatUniversalData, String toUserName, String fileName, String base64) throws IOException {
        if (Objects.equals(toUserName, "所有人")) {
            for (String userName : chatUniversalData.getAllOnlineUser().keySet()) {
                Socket socket = chatUniversalData.getAllOnlineUser().get(userName);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                writer.println("Server-from:file://" + fileName + "-name:" + base64);
            }
        } else {
            Socket socket = chatUniversalData.getAllOnlineUser().get(toUserName);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("Server-from:file://" + fileName + "-name:" + base64);
        }
    }

    public void sendTextMessage(ChatUniversalData chatUniversalData, String toUserName, String message) throws IOException {
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
            writer.println("Server-from:texs://☩");
            for (String string : strs) {
                switch (string) {
                    case "☩", "❊" -> string = "□";
                    case "\n", "\r", "" -> string = "❊";
                }
                writer.println("Server-from:texs://" + string + "\n");
            }
        }
    }
}

