package com.ntf.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginScreen login = new LoginScreen();
        login.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
