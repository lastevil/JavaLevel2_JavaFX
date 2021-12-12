package com.example.demo.server;

import com.example.demo.constant.ConstantsMess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHandlers {
    private static final Logger LOGGER = LogManager.getLogger(ClientHandlers.class);
    private final Socket socket;
    private final Server server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nickname;
    private boolean authEnd;
    ConstantsMess con;

    public ClientHandlers(Socket socket, Server server, ExecutorService exSer) {
        try {
            this.nickname = "";
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            //поток подсчета времяни, отключение после 120 Сек при неподключении
            //поток работы приложения
            exSer.execute(()->{
                try{
                    authenticate();
                    readMessages();
                } finally {
                    closeConnection();
                }
            });
        } catch (IOException e) {
            LOGGER.error("THREAD ERROR");
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
                server.unsubscribe(this);
                socket.close();
                server.sendClientsNicks();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (unsNick.equals("")){
            LOGGER.info("Clients app is closed");
        }
        else {
            LOGGER.info(unsNick + " logout");
            server.broadcastMsg(unsNick+" отключен");
        }
    }

    private void authenticate() {
        timeOutThread();
        while (true) {
            try {
                authEnd = false;
                String str = null;
                str = in.readUTF();
                //блок регистрации
                if (str.startsWith(con.REG.getAttribute())) {
                    LOGGER.info(this.getNick()+" send command "+con.REG);
                    String[] split = str.split(" ");
                    String nickname = split[1];
                    String login = split[2];
                    String password = split[3];
                    if (server.getBaseAuth().registration(nickname, login, password)) {
                        sendMessage(login + " зарегистрирован под ником " + nickname);
                    } else sendMessage("Пользователь уже существует");
                }
                //блок авторизации
                if (str.startsWith(con.AUTH.getAttribute())) {
                    String[] split = str.split(" ");
                    String login = split[1];
                    String password = split[2];
                    String nick = this.server.getBaseAuth().getNickByLoginPass(login, password);
                    LOGGER.info(nick+" send command "+con.AUTH);
                    if (nick != null) {
                        if (server.isNickBusy(nick)) {
                            sendMessage(nick + " уже авторизован");
                            continue;
                        }
                        else {
                        sendMessage(con.AUTHOK.getAttribute() + nick);
                        authEnd = true;
                        this.nickname = nick;
                        server.subscribe(this);
                        LOGGER.info(nick + " login");
                        server.broadcastMsg("Пользователь " + nick + " подключен");
                        server.sendClientsNicks();
                        break;
                        }
                    } else {
                        LOGGER.warn("Error login data");
                        sendMessage("Неверные учетные данные");
                    }
                }
                //завершение работы клиента
                if (str.startsWith(con.END.getAttribute())) {
                    LOGGER.info(this.getNick()+" send command " + con.END);
                    sendMessage(con.END.getAttribute());
                    break;
                }
            } catch (IOException e) {
                LOGGER.error("IO Error");
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
                    if (msg.equals(con.LOGOUT.getAttribute())){
                        LOGGER.info(this.getNick()+" send command " + con.LOGOUT);
                        sendMessage("Вы вышли ожидание входа");
                        String unsNick = this.nickname;
                        server.unsubscribe(this);
                        server.broadcastMsg(unsNick+ " вышел");
                        server.sendClientsNicks();
                        authenticate();
                        continue;
                    }
                    if (msg.equals(con.END.getAttribute())){
                        LOGGER.info(this.getNick()+" send command " + con.END);
                        sendMessage("Вы отключены от сервера");
                        sendMessage(con.END.getAttribute());
                        break;
                    }
                    if(msg.startsWith(con.TO.getAttribute())){
                        LOGGER.info(this.getNick()+" send message");
                       final String[] s = msg.split(" ", 3);
                        sendMessage("to " +s[1]+ ": "+s[2]);
                        server.sendMessageToNick(s[1],s[2],getNick());
                        continue;
                    }
                    if(msg.startsWith(con.ALL.getAttribute())){
                        LOGGER.info(this.getNick()+" send message");
                        final String[] s = msg.split(" ", 2);
                        server.broadcastMsg("Всем от "+getNick()+": "+s[1]);
                    }
                    if (msg.startsWith(con.CHN_NICK.getAttribute())){
                        LOGGER.info(this.getNick()+" send command "+con.CHN_NICK);
                        String[] split = msg.split(" ");
                        server.getBaseAuth().nickChange(this.nickname,split[1]);
                        server.unsubscribe(this);
                        this.nickname=split[1];
                        server.subscribe(this);
                        sendMessage("ник изменен на "+this.nickname);
                        server.sendClientsNicks();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void timeOutThread(){
        new Thread(()->{
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!authEnd) {
                LOGGER.warn("AUTH TIMEOUT");
                sendMessage(con.AUTH_TIMEOUT.getAttribute());
            }
        }).start();
    }
}
