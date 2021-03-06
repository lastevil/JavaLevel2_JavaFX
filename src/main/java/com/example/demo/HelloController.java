package com.example.demo;

import com.example.demo.constant.ConstantsMess;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Optional;

public class HelloController extends ConstantsMess {

    private  Client client;
    private LogDialog dialogLog;
    private RegDialog dialogReg;
    private ChangeNickDialog dialogNickChange;
    private ObservableList<String> observableList;

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
        if (client.connectedCheck()){
            client.sendMessage(END);
        }
        if (client.connectedCheck()){
            client.sendMessage(END);
        }
        System.exit(0);
    }

    public void menuLogout(ActionEvent actionEvent) {
        client.sendMessage(LOGOUT);
        loginForm.setDisable(false);
        chatForm.setDisable(true);
        chatSendForm.setVisible(false);
    }

    @FXML
    private void buttonLogin(ActionEvent actionEvent) {
        dialogLog = new LogDialog();
        dialogLog.DialogForm("Login Form");
        Optional<String> s = dialogLog.showAndWait();
        if (!s.isEmpty()){
        String[] userdata = s.get().split(" ");
        client.sendMessage(AUTH+" "+userdata[0]+" "+userdata[1]);
       } else {
            textArea.setText("???????????????? ????????????\n");
       }
    }

    @FXML
    private void buttonReg(ActionEvent actionEvent) {
        dialogReg = new RegDialog();
        dialogReg.DialogForm("Registration");
        Optional<String> s = dialogReg.showAndWait();
        if (!s.isEmpty()){
            String[] userdata = s.get().split(" ");
            client.sendMessage(REG+" "+userdata[0]+" "+userdata[1]+" "+userdata[2]);
        } else {
            textArea.setText("???????????????? ????????????\n");
        }
    }

    public void nickChange(ActionEvent actionEvent) {
        dialogNickChange = new ChangeNickDialog();
        dialogNickChange.DialogForm("Nickname Change");
        Optional<String> s = dialogNickChange.showAndWait();
        if (!s.isEmpty()){
            client.sendMessage(CHN_NICK+" "+s.get());
        } else {
            textArea.setText("???????????????? ????????????\n");
        }
    }
}