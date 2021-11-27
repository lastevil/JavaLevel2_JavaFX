package com.example.demo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChangeNickDialog extends Dialog<String> {
        private TextField textField;

        public void DialogForm(String title) {
            Thread.currentThread().isDaemon();
            setTitle(title);
            setHeaderText("Please enter your new nickname.");
            textField = new TextField();
            textField.setPromptText("Nickname");
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
            getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            VBox vBox = new VBox();
            HBox hBox1 = new HBox();

            vBox.getChildren().add(hBox1);
            hBox1.getChildren().add(textField);
            hBox1.setPadding(new Insets(10));
            HBox.setHgrow(textField, Priority.ALWAYS);
            getDialogPane().setContent(vBox);
            Platform.runLater(() -> textField.requestFocus());

            setResultConverter(dialogButton->{
                if(dialogButton.getButtonData() == okButton.getButtonData()){
                    return textField.getText();
                }
                if (dialogButton.getButtonData() == cancelButton.getButtonData()){
                    return null;
                }
                return null;
            });

    }

}
