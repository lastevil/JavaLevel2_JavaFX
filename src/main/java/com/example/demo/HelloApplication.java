package com.example.demo;

import com.example.demo.constant.ConstantsMess;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    private static HelloController controller;
    private  Client client;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 240);
        stage.setTitle("Test chat");
        stage.setScene(scene);
        stage.show();
        controller = fxmlLoader.getController();
        stage.setOnCloseRequest(event -> {
            controller.exitButtonAction();
        });
        setCombobox();
        }


    public static void main(String[] args) {
        launch();
    }
    public static void setCombobox(){
        controller.comboBox.setValue("/all");
    }
}