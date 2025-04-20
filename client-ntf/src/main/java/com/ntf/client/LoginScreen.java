package com.ntf.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginScreen {

    private TextField usernameField;
    private Button connectButton;
    private Button aiButton;

    public void show(Stage stage) {
        Label title = new Label("NTF - Nail The Four");
        title.setFont(Font.font("Arial", 28));
        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Arial", 22));
        loginLabel.setStyle("-fx-text-fill: #b33939;");

        VBox titleBox = new VBox(title, loginLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setSpacing(5);

        Label usernameLabel = new Label("Choose username");
        usernameLabel.setFont(Font.font("Arial", 16));
        usernameField = new TextField();
        usernameField.setPrefWidth(300);

        VBox centerBox = new VBox(usernameLabel, usernameField);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);

        connectButton = new Button("Connect to server >");
        aiButton = new Button("Play vs AI >");

        VBox buttonBox = new VBox(aiButton, connectButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        VBox rightContent = new VBox(titleBox, centerBox, buttonBox);
        rightContent.setAlignment(Pos.CENTER);
        rightContent.setSpacing(20);
        rightContent.setPadding(new Insets(20));

        Image image = new Image(getClass().getResource("/images/Homescreen1.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        HBox mainLayout = new HBox(imageView, rightContent);
        mainLayout.setAlignment(Pos.CENTER_LEFT);
        mainLayout.setSpacing(40);
        mainLayout.setPadding(new Insets(40));

        Scene scene = new Scene(mainLayout, 700, 400);
        stage.setTitle("NTF - Login");
        stage.setScene(scene);
        stage.show();
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
