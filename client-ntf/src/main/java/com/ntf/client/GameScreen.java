package com.ntf.client;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class GameScreen {

    private Stage stage;
    private String playerName;
    private String opponentName;
    private boolean isPlayerTurn;
    private boolean isRed;
    private ClientConnection connection;
    private Label turnLabel;
    private Label playerInfoLabel;
    private Label opponentInfoLabel;
    private GridPane boardGrid;
    private GameBoard gameBoard;

    public GameScreen(Stage stage, String playerName, String opponentName, boolean isRed, ClientConnection connection) {
        this.stage = stage;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.isRed = isRed;
        this.isPlayerTurn = isRed;
        this.connection = connection;
        this.gameBoard = new GameBoard();
        setupUI();
        listenForOpponentMoves();
    }

    private void setupUI() {
        String playerColor = isRed ? "🔴" : "🟡";
        String opponentColor = isRed ? "🟡" : "🔴";

        playerInfoLabel = new Label(playerColor + " " + playerName);
        playerInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        opponentInfoLabel = new Label(opponentColor + " " + opponentName);
        opponentInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        turnLabel = new Label();
        updateTurnLabel();
        turnLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox topInfo = new VBox(5, playerInfoLabel, opponentInfoLabel, turnLabel);
        topInfo.setAlignment(Pos.CENTER);

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        updateBoardDisplay();

        GridPane buttonGrid = new GridPane();
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.setHgap(5);

        for (int col = 0; col < 7; col++) {
            int finalCol = col;
            Button colButton = new Button("" + (col + 1));
            colButton.setPrefWidth(50);
            colButton.setOnAction(e -> {
                if (isPlayerTurn) {
                    int placedRow = gameBoard.placeCoin(finalCol, isRed ? 'R' : 'Y');
                    if (placedRow != -1) {
                        updateBoardDisplay();
                        if (gameBoard.checkWin(isRed ? 'R' : 'Y')) {
                            turnLabel.setText("You Win!");
                        } else if (gameBoard.isFull()) {
                            turnLabel.setText("Draw!");
                        } else {
                            isPlayerTurn = false;
                            updateTurnLabel();
                            connection.getWriter().println("MOVE:" + finalCol);
                        }
                    }
                }
            });
            buttonGrid.add(colButton, col, 0);
            GridPane.setHalignment(colButton, HPos.CENTER);
        }

        Button quitButton = new Button("Quit >");
        quitButton.setStyle("-fx-text-fill: red;");
        quitButton.setOnAction(e -> {
            connection.getWriter().println("LEFT_LOBBY");
            LoginScreen login = new LoginScreen(stage);
            login.start(stage);
        });

        VBox mainBox = new VBox(10, topInfo, boardGrid, buttonGrid, quitButton);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(mainBox, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Connect 4 - Game");
        stage.show();
    }

    private void updateBoardDisplay() {
        boardGrid.getChildren().clear();
        char[][] matrix = gameBoard.getBoard();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                StackPane cell = new StackPane();
                cell.setMinSize(50, 50);
                cell.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
                char val = matrix[row][col];
                if (val == 'R') {
                    Image redCoin = new Image("images/NTF_RED.png", 40, 40, true, true);
                    cell.getChildren().add(new ImageView(redCoin));
                } else if (val == 'Y') {
                    Image yellowCoin = new Image("images/NTF_YELLOW.png", 40, 40, true, true);
                    cell.getChildren().add(new ImageView(yellowCoin));
                }
                boardGrid.add(cell, col, row);
            }
        }
    }

    private void updateTurnLabel() {
        String name = isPlayerTurn ? playerName : opponentName;
        String color = isPlayerTurn
                ? (isRed ? "#FF0000" : "#FFD700")
                : (isRed ? "#FFD700" : "#FF0000");
        turnLabel.setText(name + "'s Turn!");
        turnLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color);
    }

    private void listenForOpponentMoves() {
        new Thread(() -> {
            try {
                String line;
                while ((line = connection.getReader().readLine()) != null) {
                    if (line.startsWith("MOVE:")) {
                        int column = Integer.parseInt(line.split(":")[1]);
                        Platform.runLater(() -> {
                            int placedRow = gameBoard.placeCoin(column, isRed ? 'Y' : 'R');
                            if (placedRow != -1) {
                                updateBoardDisplay();
                                if (gameBoard.checkWin(isRed ? 'Y' : 'R')) {
                                    turnLabel.setText(opponentName + " Wins!");
                                } else if (gameBoard.isFull()) {
                                    turnLabel.setText("Draw!");
                                } else {
                                    isPlayerTurn = true;
                                    updateTurnLabel();
                                }
                            }
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
