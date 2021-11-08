package com.example.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;


public class Server {
    private static final int SERVER_PORT = 8189;
    private static boolean THREAD_STATUS = false;
    private static String str;

    public static void main(String[] args) {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Сервер запущен, ожидаем подключения...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //Поток на чтение
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String str = null;
                        try {
                            str = in.readUTF();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (str.equals("/end")) {
                            try {
                                System.out.println("Сервер завершил работу");
                                out.writeUTF("/end");
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.exit(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!str.isEmpty()){
                            System.out.println("Сообщение от клиента: "+str);
                        }
                    }

                    }
            }).start();

            //Поток на отправку
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Thread.currentThread().isDaemon();
                    while (true) {
                    try {
                        str = scanner.nextLine();
                            if (!str.equals(""))
                            {
                                out.writeUTF(str);
                            }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
