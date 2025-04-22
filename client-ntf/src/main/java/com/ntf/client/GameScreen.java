package com.ntf.client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Random;

public class GameScreen {

    private Stage stage;
    private String playerName;
    private String opponentName;
    private boolean isPlayerTurn;
    private boolean isRed;
    private Label turnLabel;
    private Label playerInfoLabel;
    private Label opponentInfoLabel;
    private GridPane boardGrid;
    private GameBoard gameBoard;

    public GameScreen(Stage stage, String playerName, String opponentName, boolean isRed) {
        this.stage = stage;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.isRed = isRed;
        this.isPlayerTurn = isRed;
        this.gameBoard = new GameBoard();
        setupUI();
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
        turnLabel.setStyle("-fx-font-size: 24px;");

        VBox topInfo = new VBox(5, playerInfoLabel, opponentInfoLabel, turnLabel);
        topInfo.setAlignment(Pos.CENTER);

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        updateBoardDisplay();

        HBox columnButtons = new HBox(10);
        columnButtons.setAlignment(Pos.CENTER);
        for (int col = 0; col < 7; col++) {
            int finalCol = col;
            Button colButton = new Button("" + (col + 1));
            colButton.setOnAction(e -> {
                if (isPlayerTurn) {
                	int placedRow = gameBoard.placeCoin(finalCol, isRed ? 'R' : 'Y');
                	if (placedRow != -1) {
                	    isPlayerTurn = false;
                	    updateBoardDisplay();
                	    updateTurnLabel();
                	}

                }
            });
            columnButtons.getChildren().add(colButton);
        }

        Button quitButton = new Button("Quit >");
        quitButton.setStyle("-fx-text-fill: red;");
        quitButton.setOnAction(e -> {
            LoginScreen login = new LoginScreen(stage);
            login.start(stage);
        });

        VBox mainBox = new VBox(10, topInfo, boardGrid, columnButtons, quitButton);
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
                Label cell = new Label();
                cell.setMinSize(50, 50);
                cell.setStyle("-fx-border-color: blue; -fx-border-width: 2px; -fx-alignment: center;");
                char val = matrix[row][col];
                if (val == 'R') cell.setText("\uD83D\uDD34");
                else if (val == 'Y') cell.setText("\uD83D\uDFE1");
                boardGrid.add(cell, col, row);
            }
        }
    }

    private void updateTurnLabel() {
        String name = isPlayerTurn ? playerName : opponentName;
        String color = (isPlayerTurn == isRed) ? "#FFD700" : "#FF0000";
        turnLabel.setText(name + "'s Turn!");
        turnLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color);
    }
}
