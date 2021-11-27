package com.example.demo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class RegDialog extends Dialog<String> {
    private PasswordField passwordField;
    private TextField nameField;
    private TextField nicknameField;


    public void DialogForm(String title) {
        Thread.currentThread().isDaemon();
        setTitle(title);
        setHeaderText("Please enter your userdata.");
        nicknameField = new TextField();
        nicknameField.setPromptText("Nickname");
        nameField = new TextField();
        nameField.setPromptText("Login");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        ButtonType okButton = new ButtonType("OK",ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        VBox vBox = new VBox();
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        HBox hBox3 = new HBox();
        vBox.getChildren().add(hBox3);
        hBox3.getChildren().add(nicknameField);
        hBox3.setPadding(new Insets(10));
        vBox.getChildren().add(hBox1);
        hBox1.getChildren().add(nameField);
        hBox1.setPadding(new Insets(10));
        HBox.setHgrow(nameField, Priority.ALWAYS);
        vBox.getChildren().add(hBox2);
        hBox2.getChildren().add(passwordField);
        hBox2.setPadding(new Insets(10));
        getDialogPane().setContent(vBox);
        Platform.runLater(() -> nicknameField.requestFocus());

        setResultConverter(dialogButton->{
            if(dialogButton.getButtonData() == okButton.getButtonData()){
                return  nicknameField.getText()+" "+nameField.getText()+" "+passwordField.getText();
            }
            if (dialogButton.getButtonData() == cancelButton.getButtonData()){
                return null;
            }
            return null;
        });
    }
}
