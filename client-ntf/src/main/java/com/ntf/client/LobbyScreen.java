package com.ntf.client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyScreen {

    private Stage stage;
    private Scene lobbyScene;
    private String username;
    private ClientConnection connection;
    private boolean isRed;

    private Button lobby1Button;
    private Button lobby2Button;
    private Button lobby3Button;

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

        lobby1Button = new Button("Lobby 1");
        lobby2Button = new Button("Lobby 2");
        lobby3Button = new Button("Lobby 3");

        lobby1Button.setPrefWidth(200);
        lobby2Button.setPrefWidth(200);
        lobby3Button.setPrefWidth(200);

        lobby1Button.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        lobby2Button.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        lobby3Button.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");

        lobby1Button.setOnAction(e -> joinLobby(1));
        lobby2Button.setOnAction(e -> joinLobby(2));
        lobby3Button.setOnAction(e -> joinLobby(3));

        lobbyBox.getChildren().addAll(lobby1Button, lobby2Button, lobby3Button);

        BorderPane root = new BorderPane();
        root.setTop(backLabel);
        BorderPane.setAlignment(backLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(backLabel, new Insets(10));
        root.setCenter(lobbyBox);

        lobbyScene = new Scene(root, 800, 600);
        requestLobbyStatus();
    }

    private void joinLobby(int lobbyId) {
        connection.getWriter().println("JOIN_LOBBY:" + lobbyId);
        System.out.println("Sent request to join Lobby " + lobbyId);

        new Thread(() -> {
            try {
                String opponent = null;
                while (true) {
                    String response = connection.getReader().readLine();
                    if (response == null) break;

                    if (response.startsWith("JOIN_SUCCESS")) {
                        System.out.println("Successfully joined Lobby " + lobbyId);
                    } else if (response.startsWith("JOIN_FAILED")) {
                        System.out.println("Join failed: " + response);
                    } else if (response.startsWith("COLOR_ASSIGN")) {
                        String color = response.split(":")[1];
                        isRed = color.equals("RED");
                    } else if (response.startsWith("LOBBY_READY")) {
                        opponent = response.split(":")[1];
                        String finalOpponent = opponent;
                        Platform.runLater(() -> {
                            GameScreen gameScreen = new GameScreen(stage, username, finalOpponent, isRed, connection, false);
                        });
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }


    private void requestLobbyStatus() {
        connection.getWriter().println("REQUEST_LOBBY_STATUS");

        new Thread(() -> {
            try {
                String response;
                while ((response = connection.getReader().readLine()) != null) {
                    if (response.startsWith("LOBBY_STATUS:")) {
                        String[] statuses = response.substring("LOBBY_STATUS:".length()).split(",");
                        Platform.runLater(() -> {
                            lobby1Button.setText("Lobby 1\n" + statuses[0] + " Players");
                            lobby2Button.setText("Lobby 2\n" + statuses[1] + " Players");
                            lobby3Button.setText("Lobby 3\n" + statuses[2] + " Players");
                        });
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void start(Stage stage) {
        stage.setTitle("Lobby - NTF");
        stage.setScene(lobbyScene);
        stage.show();
    }
}
