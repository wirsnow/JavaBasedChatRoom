package indi.wirsnow.chatroom.client;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author : wirsnow
 * @date : 2023/1/19 22:56
 * @description : 客户端发送消息类
 */
public class ClientMessageOutput {
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
            writer.println(toUserName + "-to:texs://start");
            for (String string : strs) {
                switch (string) {
                    case "start" -> string = "startstart";
                    case "newline" -> string = "newlinenewline";
                    case "\n", "\r", "" -> string = "newline";
                }
                writer.println(toUserName + "-to:texs://" + string + "\n");
            }
        }
    }
}
