package com.example.demo.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;


public class ClientHandlers {
    private static final String END = "/end";
    private static final String AUTH = "/auth";
    private static final String LOGOUT = "/logout";
    private static final String AUTHOK = "/authok";
    private static final String TO = "/w";

    private final Socket socket;
    private final Server server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nickname;

    public ClientHandlers(Socket socket, Server server) {
        try {
            this.nickname = "";
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            //создание потока
            new Thread(()->{
                try{
                    boolean errLunch = false;
                    authenticate();
                    readMessages();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        String unsNick = getNick();
        if (in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null){
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(socket !=null){
            try {
                System.out.println("Client "+this.getNick()+" logout");
                server.unsubscribe(this);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (unsNick!=null){server.broadcastMsg(unsNick+" отключен");}
    }

    private void authenticate() {
        while (true){
            try {
               String str = in.readUTF();
               if(str.startsWith(AUTH)){
                   String[] split = str.split(" ");
                   String login = split[1];
                   String password = split[2];
                   String nick = this.server.getBaseAuth().getNickByLoginPass(login, password);
                   if (nick!=null){
                       if (server.isNickBusy(nick)){
                           sendMessage(nick+" уже авторизован");
                           continue;
                       }
                       sendMessage(AUTHOK + nick);
                       this.nickname=nick;
                       server.subscribe(this);
                       System.out.println(nick+ " Login");
                       server.broadcastMsg("Пользователь "+nick+ " подключен");
                       break;
                   }
                   else{
                       sendMessage("Неверные учетные данные");
                   }


               }
               if (str.startsWith(END)){
                   sendMessage(END);
                   break;
               }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getNick() {
        return nickname;
    }

    public void sendMessage(String s) {
        try {
            out.writeUTF(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() {
            try {
                while (true) {
                    String msg = in.readUTF();
                    if (msg.equals(LOGOUT)){
                        sendMessage("Вы вышли ожидание входа");
                        String unsNick = this.nickname;
                        server.unsubscribe(this);
                        server.broadcastMsg(unsNick+ " вышел");
                        System.out.println(getNick() +" Logout");
                        authenticate();
                        continue;
                    }
                    if (msg.equals(END)){
                        sendMessage("Вы отключены от сервера");
                        sendMessage(END);
                        break;
                    }
                    if(msg.startsWith(TO)){
                       final String[] s = msg.split(" ", 3);
                        sendMessage("to " +s[1]+ ": "+s[2]);
                        server.sendMessageToNick(s[1],s[2],getNick());
                        continue;
                    }
                    server.broadcastMsg(getNick()+": "+msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
