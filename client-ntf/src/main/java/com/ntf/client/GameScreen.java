package com.ntf.client;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;

public class GameScreen {
    private boolean vsAI;
    private Ai ai;
    private Stage stage;
    private String playerName;
    private String opponentName;
    private boolean isPlayerTurn;
    private boolean isRed;
    private boolean gameEnded = false;
    private ClientConnection connection;
    private Label turnLabel;
    private Label playerInfoLabel;
    private Label opponentInfoLabel;
    private GridPane boardGrid;
    private GameBoard gameBoard;
    private TextArea chatArea;
    private TextField chatInput;
    private Button sendButton;
    private Button playAgainButton;
    private Button quitButton;
    private boolean listening = true;
    private int lobbyID;

    public GameScreen(Stage stage, String playerName, String opponentName, boolean isRed, ClientConnection connection, boolean vsAI, int lobbyID) {
        this.stage = stage;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.isRed = isRed;
        this.isPlayerTurn = isRed;
        this.connection = connection;
        this.vsAI = vsAI;
        this.lobbyID = lobbyID;
        this.ai = new Ai();
        this.gameBoard = new GameBoard();
        setupUI();
        if (!vsAI) {
            listenForOpponentMoves();
        }
    }

    private void setupUI() {
        playerInfoLabel = new Label(playerName);
        String playerColor;
        if (isRed) {
            playerColor = "#FF0000";
        } else {
            playerColor = "#FFD700";
        }
        playerInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + playerColor + ";");

        opponentInfoLabel = new Label(opponentName);
        String opponentColor;
        if (isRed) {
            opponentColor = "#FFD700";
        } else {
            opponentColor = "#FF0000";
        }
        opponentInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + opponentColor + ";");

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
            colButton.setOnAction(e -> handleMove(finalCol));
            buttonGrid.add(colButton, col, 0);
            GridPane.setHalignment(colButton, HPos.CENTER);
        }

        playAgainButton = new Button("Play Again >");
        playAgainButton.setStyle("-fx-text-fill: green;");
        playAgainButton.setPrefWidth(120);
        playAgainButton.setOnAction(e -> handlePlayAgain());

        quitButton = new Button("Quit >");
        quitButton.setStyle("-fx-text-fill: red;");
        quitButton.setPrefWidth(120);
        quitButton.setOnAction(e -> handleQuit());

        HBox bottomButtons = new HBox(20, playAgainButton, quitButton);
        bottomButtons.setAlignment(Pos.CENTER);

        VBox leftSide = new VBox(10, topInfo, boardGrid, buttonGrid, bottomButtons);
        leftSide.setAlignment(Pos.CENTER);

        HBox mainLayout;
        if (!vsAI) {
            chatArea = new TextArea();
            chatArea.setEditable(false);
            chatArea.setPrefHeight(400);
            chatArea.setPrefWidth(250);
            chatArea.setWrapText(true);

            chatInput = new TextField();
            chatInput.setPromptText("Message...");
            chatInput.setPrefWidth(180);
            chatInput.setOnAction(e -> sendChatMessage());

            sendButton = new Button("Send");
            sendButton.setPrefWidth(60);
            sendButton.setOnAction(e -> sendChatMessage());

            HBox chatInputBox = new HBox(5, chatInput, sendButton);
            chatInputBox.setAlignment(Pos.CENTER);

            VBox chatBox = new VBox(10, new Label("Chat"), chatArea, chatInputBox);
            chatBox.setPadding(new Insets(20));
            chatBox.setAlignment(Pos.TOP_CENTER);

            mainLayout = new HBox(50, leftSide, chatBox);
        } else {
            mainLayout = new HBox(50, leftSide);
        }

        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        Scene scene = new Scene(mainLayout, 1000, 700);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Connect 4 - Game");
        stage.show();
    }

    private void handleMove(int column) {
        if (isPlayerTurn && !gameEnded) {
            char moveColor;
            if (isRed) {
                moveColor = 'R';
            } else {
                moveColor = 'Y';
            }
            int placedRow = gameBoard.placeCoin(column, moveColor);
            if (placedRow != -1) {
                updateBoardDisplay();
                if (!vsAI && connection != null) {
                    connection.getWriter().println("MOVE:" + column);
                }
                if (gameBoard.checkWin(moveColor)) {
                    turnLabel.setText("You Win!");
                    gameEnded = true;
                } else if (gameBoard.isFull()) {
                    turnLabel.setText("Draw!");
                    gameEnded = true;
                } else {
                    isPlayerTurn = false;
                    updateTurnLabel();
                    if (vsAI && !gameEnded) handleAIMove();
                }
            }
        }
    }

    private void handleAIMove() {
        Platform.runLater(() -> {
            int aiMove = ai.chooseMove(gameBoard, !isRed);
            char aiColor;
            if (!isRed) {
                aiColor = 'R';
            } else {
                aiColor = 'Y';
            }
            gameBoard.placeCoin(aiMove, aiColor);
            updateBoardDisplay();
            if (gameBoard.checkWin(aiColor)) {
                turnLabel.setText(opponentName + " (AI) Wins!");
                gameEnded = true;
            } else if (gameBoard.isFull()) {
                turnLabel.setText("Draw!");
                gameEnded = true;
            } else {
                isPlayerTurn = true;
                updateTurnLabel();
            }
        });
    }

    private void sendChatMessage() {
        String message = chatInput.getText().trim();
        if (!message.isEmpty() && connection != null) {
            chatArea.appendText("Me: " + message + "\n");
            connection.getWriter().println("CHAT:" + message);
            chatInput.clear();
        }
    }

    private void handlePlayAgain() {
        if (vsAI) {
            resetGame();
        } else {
            connection.getWriter().println("PLAY_AGAIN_REQUEST");
            turnLabel.setText("Waiting for opponent to accept...");
        }
    }

    private void handleQuit() {
        if (!vsAI && connection != null) {
            connection.getWriter().println("LEFT_LOBBY");
        }
        LoginScreen login = new LoginScreen(stage);
        login.start(stage);
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
                    cell.getChildren().add(new ImageView(new Image("images/NTF_RED.png", 40, 40, true, true)));
                } else if (val == 'Y') {
                    cell.getChildren().add(new ImageView(new Image("images/NTF_YELLOW.png", 40, 40, true, true)));
                }
                boardGrid.add(cell, col, row);
            }
        }
    }

    private void updateTurnLabel() {
        String name;
        if (isPlayerTurn) {
            name = playerName;
        } else {
            name = opponentName;
        }

        String color;
        if (isPlayerTurn) {
            if (isRed) {
                color = "#FF0000";
            } else {
                color = "#FFD700";
            }
        } else {
            if (isRed) {
                color = "#FFD700";
            } else {
                color = "#FF0000";
            }
        }

        turnLabel.setText(name + "'s Turn!");
        turnLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color);
    }

    private void listenForOpponentMoves() {
        new Thread(() -> {
            try {
                String line;
                while (listening && (line = connection.getReader().readLine()) != null) {
                    String finalLine = line;
                    Platform.runLater(() -> {
                        if (finalLine.startsWith("MOVE:")) {
                            int column = Integer.parseInt(finalLine.split(":")[1]);
                            char opponentColor;
                            if (isRed) {
                                opponentColor = 'Y';
                            } else {
                                opponentColor = 'R';
                            }
                            int placedRow = gameBoard.placeCoin(column, opponentColor);
                            if (placedRow != -1) {
                                updateBoardDisplay();
                                if (gameBoard.checkWin(opponentColor)) {
                                    turnLabel.setText(opponentName + " Wins!");
                                    gameEnded = true;
                                } else if (gameBoard.isFull()) {
                                    turnLabel.setText("Draw!");
                                    gameEnded = true;
                                } else {
                                    isPlayerTurn = true;
                                    updateTurnLabel();
                                }
                            }
                        } else if (finalLine.startsWith("CHAT:")) {
                            if (chatArea != null) {
                                String chatMessage = finalLine.substring(5);
                                chatArea.appendText(opponentName + ": " + chatMessage + "\n");
                            }
                        } else if (finalLine.equals("PLAY_AGAIN_ACCEPTED")) {
                            resetGame();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void resetGame() {
        this.gameBoard = new GameBoard();
        this.gameEnded = false;
        this.isPlayerTurn = isRed;
        updateBoardDisplay();
        updateTurnLabel();
        startListening();
    }

    private void startListening() {
        listening = true;
        listenForOpponentMoves();
    }
}
