package indi.wirsnow.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author : wirsnow
 * @date : 2022/12/7 11:04
 * @description:
 */
public class ChatClientAPP {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 56458);
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

            System.out.println("输入数据:");
            Scanner scanner = new Scanner(System.in);
            String data = scanner.nextLine();
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            socket.shutdownOutput();

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String info;

            System.out.println("客户端ip:" + socket.getInetAddress().getHostAddress());

            while ((info = bufferedReader.readLine()) != null) {
                System.out.println("客户端接收:" + info);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            outputStreamWriter.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
