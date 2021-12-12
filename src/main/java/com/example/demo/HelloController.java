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

public class HelloController {

    private  Client client;
    private LogDialog dialogLog;
    private RegDialog dialogReg;
    private ChangeNickDialog dialogNickChange;
    private ObservableList<String> observableList;
    private ConstantsMess con;
    private History history;

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
            if(comboBox.getValue().toString().equals(con.ALL.getAttribute())){
            client.sendMessage(con.ALL.getAttribute()+" "+mess);
            }
            else {
                client.sendMessage(con.TO.getAttribute()+" "+comboBox.getValue().toString()+" "+mess);
            }

            textField.clear();
            textField.focusedProperty();
        }
    }

    public void getMessage(String a){
        textArea.appendText(a+"\n");
        if (!a.endsWith("авторизован")){
            history.writeFile(a);
        }

    }
    public void getHistory(String a){
        textArea.appendText(a+"\n");
    }

    @FXML
    private void menuExit(ActionEvent actionEvent) {
        exitButtonAction();
    }

    public void exitButtonAction(){
        if (client.connectedCheck()){
            client.sendMessage(con.END.getAttribute());
        }
        if (client.connectedCheck()){
            client.sendMessage(con.END.getAttribute());
        }

        System.exit(0);
    }
    @FXML
    private void menuLogout(ActionEvent actionEvent) {

        client.sendMessage(con.LOGOUT.getAttribute());
        textArea.clear();
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
        client.sendMessage(con.AUTH.getAttribute()+" "+userdata[0]+" "+userdata[1]);
       } else {
            textArea.setText("неверные данные\n");
       }
    }
    @FXML
    private void buttonReg(ActionEvent actionEvent) {
        dialogReg = new RegDialog();
        dialogReg.DialogForm("Registration");
        Optional<String> s = dialogReg.showAndWait();
        if (!s.isEmpty()){
            String[] userdata = s.get().split(" ");
            client.sendMessage(con.REG.getAttribute()+" "+userdata[0]+" "+userdata[1]+" "+userdata[2]);
        } else {
            textArea.setText("неверные данные\n");
        }
    }
    @FXML
    private void nickChange(ActionEvent actionEvent) {
        dialogNickChange = new ChangeNickDialog();
        dialogNickChange.DialogForm("Nickname Change");
        Optional<String> s = dialogNickChange.showAndWait();
        if (!s.isEmpty()){
            client.sendMessage(con.CHN_NICK.getAttribute()+" "+s.get());
        } else {
            textArea.setText("Неверные данные\n");
        }
    }
}