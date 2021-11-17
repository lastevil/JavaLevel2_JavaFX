package com.example.demo;

import com.example.demo.constant.ConstantsMess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;



public class HelloController extends ConstantsMess {

    private  Client client;

    public HelloController() {
        client = new Client(this);
    }
    @FXML
    public HBox chatSendForm;
    @FXML
    public HBox loginForm;
    @FXML
    public HBox chatForm;
    @FXML
    private TextField textFieldLogin;
    @FXML
    private TextField textFieldPassword;
    @FXML
    public ComboBox comboBox;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;



    @FXML
    private void buttonClick(ActionEvent actionEvent) {
        textArea.setStyle("-fx-font-size: 12px; -fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick;");
        String mess = textField.getText();
        if (!mess.isEmpty()){
            if(comboBox.getValue().toString().equals(ALL)){
            client.sendMessage(ALL+" "+mess);
            }
            else {
                client.sendMessage(TO+" "+comboBox.getValue().toString()+" "+mess);
            }
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
        loginForm.setDisable(false);
        chatForm.setDisable(true);
        chatSendForm.setVisible(false);
        comboBox.setValue(null);//Не смог обойти при повторном логиине...
    }
    public void buttonLogin(ActionEvent actionEvent) {
        client.sendMessage(AUTH+" "+textFieldLogin.getText()+" "+textFieldPassword.getText());
    }
}