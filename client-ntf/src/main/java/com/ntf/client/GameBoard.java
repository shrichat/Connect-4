package com.ntf.client;

public class GameBoard {

    private char[][] board;
    private final int ROWS = 6;
    private final int COLS = 7;

    public GameBoard() {
        board = new char[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = ' ';
            }
        }
    }

    public int placeCoin(int column, char symbol) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][column] == ' ') {
                board[row][column] = symbol;
                return row;
            }
        }
        return -1;
    }

    public boolean checkWin(char symbol) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == symbol && board[row][col + 1] == symbol &&
                    board[row][col + 2] == symbol && board[row][col + 3] == symbol) {
                    return true;
                }
            }
        }

        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if (board[row][col] == symbol && board[row + 1][col] == symbol &&
                    board[row + 2][col] == symbol && board[row + 3][col] == symbol) {
                    return true;
                }
            }
        }

        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == symbol && board[row - 1][col + 1] == symbol &&
                    board[row - 2][col + 2] == symbol && board[row - 3][col + 3] == symbol) {
                    return true;
                }
            }
        }

        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == symbol && board[row + 1][col + 1] == symbol &&
                    board[row + 2][col + 2] == symbol && board[row + 3][col + 3] == symbol) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isFull() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == ' ') {
                return false;
            }
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }
}
