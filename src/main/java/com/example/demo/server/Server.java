package com.example.demo.server;

import com.example.demo.constant.ConstantsMess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final int SERVER_PORT = 8189;
    private final BaseAuthService baseAuth;
    private final Map<String, ClientHandlers> clients;
    ConstantsMess con;

    public Server() {
        this.baseAuth = new BaseAuthService();
        this.clients = new HashMap<>();

    }

    public void Connection(){
        LOGGER.info("Server STARTED");
        ExecutorService ex = Executors.newCachedThreadPool();
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true){
               final Socket socket = serverSocket.accept();
               new ClientHandlers(socket,this,ex);
            }

        } catch (IOException e) {
            LOGGER.error("Error creation of socket");
            e.printStackTrace();
        }
    }

    public BaseAuthService getBaseAuth(){
        return this.baseAuth;
    }
    public boolean isNickBusy(String nick){
            if(clients.containsKey(nick)){
                LOGGER.warn("nickname "+nick+ " is busy");
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
        StringBuilder sb = new StringBuilder(con.CLIENTS.getAttribute()+" ");
        for (ClientHandlers client:clients.values()) {
            sb.append(client.getNick()).append(" ");
        }
        broadcastMsg(sb.toString());
    }
}
