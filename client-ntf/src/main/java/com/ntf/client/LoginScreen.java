package com.ntf.client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen {

    private Stage stage;
    private TextField usernameField;
    private Button connectButton;
    private Button aiButton;
    private ClientConnection connection;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        Label title = new Label("NTF - Nail The Four");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label loginLabel = new Label("Login");
        loginLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: brown;");

        Label usernameLabel = new Label("Choose username");
        usernameLabel.setStyle("-fx-font-size: 16px;");

        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        connectButton = new Button("Connect to server >");
        aiButton = new Button("Play vs AI >");

        VBox textBox = new VBox(10, title, loginLabel, usernameLabel, usernameField, aiButton, connectButton);
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.setPadding(new Insets(20));

        ImageView imageView = new ImageView(new Image("images/Homescreen1.png"));
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        HBox layout = new HBox(20, imageView, textBox);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 800, 500);
        stage.setTitle("NTF - Login");
        stage.setScene(scene);
        stage.show();

        connectButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                new Thread(() -> {
                    connection = new ClientConnection();
                    boolean connected = connection.connect("tramway.proxy.rlwy.net", 13343, username);

                    if (connected) {
                        Platform.runLater(() -> {
                            LobbyScreen lobby = new LobbyScreen(stage, username, connection);
                            lobby.start(stage);
                        });
                    } else {
                        Platform.runLater(() -> {
                            System.out.println("Connection failed.");
                        });
                    }
                }).start();
            }
        });
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public Button getConnectButton() {
        return connectButton;
    }

    public Button getAiButton() {
        return aiButton;
    }
}
