package com.example.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;



    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    HelloController controller;
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
                        if (strFromServer.equalsIgnoreCase("/end")) {
                            controller.getMessage("/end");
                            break;
                        }
                        controller.getMessage(strFromServer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            closeConnection();
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

    public void closeConnection(){
        if (in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(socket !=null){
            try {
                socket.close();
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
    }
}
