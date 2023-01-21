package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author : wirsnow
 * @date : 2023/1/19 22:56
 * @description : 客户端发送消息类
 */
public class ClientMessageOutput {

    public void sendFile(ChatUniversalData chatUniversalData, String toUserName, String fileName, String base64) throws IOException {
        Socket socket = chatUniversalData.getSocket();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        writer.println(toUserName + "-to:file://" + fileName + "-name:" + base64);
    }

    public void sendDisconnectMessage(ChatUniversalData chatUniversalData) throws IOException {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(chatUniversalData.getSocket().getOutputStream()), true);
        out.println(chatUniversalData.getUserName() + "-to:LogOut");
    }

    public void sendTextMessage(ChatUniversalData chatUniversalData, String toUserName, String message) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(chatUniversalData.getSocket().getOutputStream()), true);
        String[] strs = message.split("\n");
        if (strs.length <= 1) {
            writer.println(toUserName + "-to:" + "text://" + message);
        } else {
            writer.println(toUserName + "-to:texs://☩");
            for (String string : strs) {
                switch (string) {
                    case "☩", "❊" -> string = "□";
                    case "\n", "\r", "" -> string = "❊";
                }
                writer.println(toUserName + "-to:texs://" + string + "\n");
            }
        }
    }
}
