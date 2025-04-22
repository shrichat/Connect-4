package com.ntf.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LobbyScreen {

    private Stage stage;
    private Scene lobbyScene;
    private String username;
    private ClientConnection connection;

    public LobbyScreen(Stage stage, String username, ClientConnection connection) {
        this.stage = stage;
        this.username = username;
        this.connection = connection;
        createLobbyScene();
    }

    private void createLobbyScene() {
        Label backLabel = new Label("< Back");
        backLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        backLabel.setOnMouseClicked(e -> {
            LoginScreen login = new LoginScreen(stage);
            login.start(stage);
        });

        VBox lobbyBox = new VBox(20);
        lobbyBox.setAlignment(Pos.CENTER);

        Button lobby1Button = new Button("Lobby 1\n1/2 Players");
        Button lobby2Button = new Button("Lobby 2\n0/2 Players");
        Button lobby3Button = new Button("Lobby 3\n2/2 Players");

        lobby1Button.setPrefWidth(200);
        lobby2Button.setPrefWidth(200);
        lobby3Button.setPrefWidth(200);

        lobby1Button.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        lobby2Button.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        lobby3Button.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");

        lobby1Button.setOnAction(e -> {
            connection.getWriter().println("JOIN LOBBY 1");
            System.out.println("Joined Lobby 1");
            GameScreen gameScreen = new GameScreen(stage, username, "Opponent");
        });

        lobby2Button.setOnAction(e -> {
            connection.getWriter().println("JOIN LOBBY 2");
            System.out.println("Joined Lobby 2");
        });

        lobby3Button.setOnAction(e -> {
            connection.getWriter().println("JOIN LOBBY 3");
            System.out.println("Joined Lobby 3");
        });

        lobbyBox.getChildren().addAll(lobby1Button, lobby2Button, lobby3Button);

        BorderPane root = new BorderPane();
        root.setTop(backLabel);
        BorderPane.setAlignment(backLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(backLabel, new Insets(10));
        root.setCenter(lobbyBox);

        lobbyScene = new Scene(root, 800, 600);
    }

    public void start(Stage stage) {
        stage.setTitle("Lobby - NTF");
        stage.setScene(lobbyScene);
        stage.show();
    }
}
