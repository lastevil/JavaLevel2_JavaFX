package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;


public class HelloController {
    private  Client client;
    private final String EXIT = "/end";

    public HelloController() {
        client = new Client(this);
    }
    
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;

    @FXML
    private void ButtonClick(ActionEvent actionEvent) {
        String mess = textField.getText();
        if (!mess.isEmpty()){
            textArea.setStyle("-fx-font-size: 12px; -fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick;");
            textArea.appendText(mess+"\n");
            client.sendMessage(mess);
            textField.clear();
            textField.focusedProperty();
        }
    }
    public void getMessage(String a){
        textArea.appendText("Сервер: "+a+"\n");
        if (a.equals(EXIT)){
            System.exit(0);
        }
    }
    @FXML
    private void menuExit(ActionEvent actionEvent) {
        client.sendMessage(EXIT);
        System.exit(0);
    }

    public void exitButtonAction(){
        client.sendMessage(EXIT);
        System.exit(0);
    }
//Добавил для завершения работы при отключеном сервере
    public void menuHardExit(ActionEvent actionEvent) {
        System.exit(-1);
    }
}