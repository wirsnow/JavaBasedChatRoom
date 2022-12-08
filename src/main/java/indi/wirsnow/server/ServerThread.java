package indi.wirsnow.server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author : wirsnow
 * @date : 2022/12/8 19:24
 * @description:
 */
public class ServerThread extends Thread {
    Socket socket;
    InetAddress inetAddress;

    public ServerThread(Socket socket, InetAddress inetAddress) {
        this.socket = socket;
        this.inetAddress = inetAddress;
    }

    @Override
    public void run() {
        InputStream inputStream = null; // 字节输入流
        InputStreamReader inputStreamReader = null; // 字符输入流
        BufferedReader bufferedReader = null;   // 缓冲输入流
        OutputStream outputStream = null;   // 字节输出流
        OutputStreamWriter outputStreamWriter = null;   // 字符输出流

        try {
            inputStream = socket.getInputStream();  // 获取输入流
            // 将字节输入流转换为字符输入流
            inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            // 将字符输入流转换为缓冲输入流
            bufferedReader = new BufferedReader(inputStreamReader);
            String info;

            while ((info = bufferedReader.readLine()) != null) {
                System.out.println("服务端接收:" + "{'from_client':'" + socket.getInetAddress().getHostAddress() + "','data':'" + info + "'}");
            }
            socket.shutdownInput();

            outputStream = socket.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write("{'to_client':'" + inetAddress.getHostAddress() + "','data':'我是服务器数据'}");
            outputStreamWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
