package indi.wirsnow.chatroom.server;

import indi.wirsnow.chatroom.util.ChatUniversalData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author : wirsnow
 * @date : 2023/1/17 21:28
 * @description : 服务端一对一聊天类
 */
public class ServerMessageOutput implements Runnable {
    private final ChatUniversalData chatUniversalData;

    public ServerMessageOutput(ChatUniversalData chatUniversalData) {
        this.chatUniversalData = chatUniversalData;
    }

//        allOnlineUser = chatUniversalData.getAllOnlineUser();
//
//         try {
//            Socket socket = serverSocket.accept();
//            System.out.println("客户端连接成功");
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter out = new PrintWriter((new OutputStreamWriter(socket.getOutputStream())), true);
//            String userName = in.readLine();
//            System.out.println("客户端" + userName + "连接成功");
//            // 刷新在线列表
//            allOnlineUser.put(userName, socket);
//            flushUserList(chatUniversalData);
//            System.out.println(chatUniversalData.getAllOnlineUser());
//            // 把allOnlineUser发送给所有客户端
//                        out.println("Server-from:list://" + allOnlineUser);
//                        for (Map.Entry<String, Socket> entry : allOnlineUser.entrySet()) {
//                            if (entry.getValue().isConnected() && !Objects.equals(entry.getKey(), userName)) {
//                                PrintWriter outTemp = new PrintWriter((new OutputStreamWriter(entry.getValue().getOutputStream())), true);
//                                BufferedReader inTemp = new BufferedReader(new InputStreamReader(entry.getValue().getInputStream()));
//                                outTemp.println("Server-from:list://" + allOnlineUser);
//                                System.out.println("已发送给" + entry.getKey());
//                            }
//                        }
//            chatUniversalData.setSocket(socket);
//            appendAndFlush(messageArea, "客户端" + socket.getInetAddress().getHostAddress() + "连接成功\n");
//            // 启动转发线程
//            threadPool.execute(new ServerMessageInput(out,in,chatUniversalData));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    /**
     *
     */
    @Override
    public void run() {
        try {
            synchronized (ServerMessageOutput.class) {
                while (true) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    String str = in.readLine();

                    if (chatUniversalData.getAllOnlineUser().isEmpty()) {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

