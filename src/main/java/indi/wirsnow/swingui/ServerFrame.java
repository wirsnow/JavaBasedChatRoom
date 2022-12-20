package indi.wirsnow.swingui;

import com.formdev.flatlaf.FlatIntelliJLaf;

/**
 * @author : wirsnow
 * @date : 2022/12/8 19:37
 * @description:
 */
public class ServerFrame {
    /*
      该类主要用于创建服务器端的图形界面
     */
    static {
        FlatIntelliJLaf.setup();
    }

    public static void main(String[] args) {

        new ChatFrame("小明");
    }


}
