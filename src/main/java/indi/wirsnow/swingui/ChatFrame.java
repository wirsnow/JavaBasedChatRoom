package indi.wirsnow.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;

/**
 * @author : wirsnow
 * @date : 2022/12/7 11:07
 * @description : 实现客户端聊天框显示
 */
public class ChatFrame {
    /*
    该类主要利用swing图形化相关技术，设计聊天软件的UI界面，
    UI界面要求至少有一个聊天记录显示框、文字输入框和发送按钮，
    UI界面设计要求简洁美观，功能上简单易学，致于设计成何种样式不作具体的限制，可自由发挥。
    聊天记录显示框可以显示客户端和服务器端双方的聊天记录，
    聊天记录包括发送的信息的人、时间以及发送的信息内容，可以正确地显示发送信息的顺序。
    点击发送按钮可以将输入的信息发送并显示出来，输入框信息为空时，点击发送按钮，程序不作反应。
    */
    static {
        FlatIntelliJLaf.setup();
    }
    public static void main(String[] args){
        new BaseFrame("小雪");
    }
}
