package com.example.demo.server;

import com.example.demo.constant.ConstantsMess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientHandlers extends ConstantsMess {

    private final Socket socket;
    private final Server server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nickname;
    private boolean authEnd;

    public ClientHandlers(Socket socket, Server server) {
        try {
            this.nickname = "";
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            //поток подсчета времяни, отключение после 120 Сек при неподключении
            new Thread(()->{
                long ct = System.currentTimeMillis();
                while (true){
                    if (System.currentTimeMillis()-ct>120000 && !authEnd){
                    sendMessage(AUTH_TIMEOUT);
                    break;
                    }
                    if (authEnd){
                        break;
                    }
                }
            }).start();
            //поток работы приложения
            new Thread(()->{
                try{
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
                server.sendClientsNicks();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (unsNick!=""){server.broadcastMsg(unsNick+" отключен");}
    }

    private void authenticate() {
        while (true){
            try {
                authEnd=false;
                String str = null;
                str = in.readUTF();
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
                       authEnd=true;
                       this.nickname=nick;
                       server.subscribe(this);
                       server.broadcastMsg("Пользователь "+nick+ " подключен");
                       server.sendClientsNicks();
                       System.out.println(nick+ " Login");
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
                        server.sendClientsNicks();
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
                    if(msg.startsWith(ALL)){
                        final String[] s = msg.split(" ", 2);
                    server.broadcastMsg("Всем от "+getNick()+": "+s[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
