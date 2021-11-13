package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class HelloController {
    private  Client client;
    private final String END = "/end";
    private static final String LOGOUT = "/logout";
    private static boolean logout;

    public HelloController() {
        client = new Client(this);
    }
    
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;

    @FXML
    private void ButtonClick(ActionEvent actionEvent) {
        textArea.setStyle("-fx-font-size: 12px; -fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick;");
        String mess = textField.getText();
        if (!mess.isEmpty()){
            client.sendMessage(mess);
            textField.clear();
            textField.focusedProperty();
        }
    }
    public void getMessage(String a){
        textArea.appendText(a+"\n");
    }

    @FXML
    private void menuExit(ActionEvent actionEvent) {
        exitButtonAction();
    }

    public void exitButtonAction(){
        //if (!logout){
        if (client.connectedCheck()){
            client.sendMessage(END);
        }
        if (client.connectedCheck()){
            client.sendMessage(END);
        }
    //    }
        System.exit(0);
    }

    public void menuLogout(ActionEvent actionEvent) {
        client.sendMessage(LOGOUT);
       // logout=true;
    }
}