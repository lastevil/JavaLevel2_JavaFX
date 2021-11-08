package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class HelloController {

    public FlowPane flowPane;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;

    @FXML
    private void ButtonClick(ActionEvent actionEvent) {
        String mess = textField.getText();
        if (!mess.isEmpty()){
            textArea.setStyle("-fx-alignment : right; -fx-font-size: 13px;");

            textArea.appendText(mess+"\n");
            textField.clear();
        }
    }
    @FXML
    private void menuExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}