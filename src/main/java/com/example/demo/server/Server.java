package com.example.demo.server;

import com.example.demo.constant.ConstantsMess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends ConstantsMess {
    private static final int SERVER_PORT = 8189;
    private final BaseAuthService baseAuth;
    private final Map<String, ClientHandlers> clients;

    public Server() {
        this.baseAuth = new BaseAuthService();
        this.clients = new HashMap<>();
    }

    public void Connection(){
        System.out.println("Server STARTED");
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true){
               final Socket socket = serverSocket.accept();
               new ClientHandlers(socket,this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BaseAuthService getBaseAuth(){
        return this.baseAuth;
    }
    public boolean isNickBusy(String nick){
            if(clients.containsKey(nick)){
                return true;
            }
        return false;
    }
    public void subscribe(ClientHandlers client){
        clients.put(client.getNick(),client);
    }
    public void unsubscribe(ClientHandlers client){
        clients.remove(client.getNick());
    }
    public void broadcastMsg(String s) {
        for (ClientHandlers client:clients.values()) {
            client.sendMessage(s);
        }
    }
    public void sendMessageToNick(String nick, String message, String from) {
        if (nick!=null){
            if(clients.containsKey(nick)){
                clients.get(nick).sendMessage(from+": "+message);
            }
        }
    }
    public void sendClientsNicks(){
        StringBuilder sb = new StringBuilder(CLIENTS+" ");
        for (ClientHandlers client:clients.values()) {
            sb.append(client.getNick()).append(" ");
        }
        broadcastMsg(sb.toString());
    }
}
