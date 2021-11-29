package com.example.demo;

import com.example.demo.constant.ConstantsMess;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static com.example.demo.HelloApplication.*;

public class Client extends ConstantsMess {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;


    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    HelloController controller;
    private ObservableList<String> observableList = null;

    Client(HelloController controller){
        this.controller=controller;
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() throws IOException{
        socket =new Socket(SERVER_ADDR,SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String strFromServer = in.readUTF();
                        if (strFromServer.equalsIgnoreCase(END)) {
                            break;
                        }
                        if (strFromServer.equalsIgnoreCase(AUTH_TIMEOUT)){

                            controller.exitButtonAction();
                        }
                        if (strFromServer.startsWith(CLIENTS)){
                            ArrayList<String> list = new ArrayList<>();
                            String[] nickList = strFromServer.split(" ");
                            nickList[0]=ALL;
                            for (String s: nickList) {
                                list.add(s);
                            }
                            observableList = FXCollections.observableList(list);
                            Platform.runLater(() -> {
                                try {
                                    controller.comboBox.setItems(observableList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            setCombobox();

                        }
                        if (strFromServer.startsWith(AUTHOK)){
                            controller.loginForm.setDisable(true);
                            controller.chatForm.setDisable(false);
                            controller.chatSendForm.setVisible(true);
                        }
                        else if (!strFromServer.startsWith(CLIENTS)){
                        controller.getMessage(strFromServer);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(String mess){
        try {
            out.writeUTF(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connectedCheck(){
        if(socket != null && out != null && in != null){
                return true;
        }
        return false;
    }

    public ObservableList<String> getClientsList(){
        return observableList;
    }
}
