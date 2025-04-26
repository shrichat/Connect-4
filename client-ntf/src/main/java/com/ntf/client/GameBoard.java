package com.ntf.client;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

    private char[][] board;

    public GameBoard() {
        board = new char[6][7];
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                board[row][col] = ' ';
            }
        }
    }

    // 🧠 Copy constructor for AI simulations
    public GameBoard(GameBoard other) {
        board = new char[6][7];
        for (int row = 0; row < 6; row++) {
            System.arraycopy(other.board[row], 0, board[row], 0, 7);
        }
    }

    // 🎯 Place a coin for actual player
    public int placeCoin(int col, char color) {
        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == ' ') {
                board[row][col] = color;
                return row;
            }
        }
        return -1; // column full
    }

    // 🧠 Simplified AI version of placing a coin
    public boolean dropPiece(int col, boolean isRed) {
        char color = isRed ? 'R' : 'Y';
        return placeCoin(col, color) != -1;
    }

    // 🧠 List of columns where a move is legal
    public List<Integer> getValidColumns() {
        List<Integer> valid = new ArrayList<>();
        for (int col = 0; col < 7; col++) {
            if (board[0][col] == ' ') {
                valid.add(col);
            }
        }
        return valid;
    }

    // 🧠 Check if the current board state is a win
    public boolean isWinningMove(int col, boolean isRed) {
        return checkWin(isRed ? 'R' : 'Y');
    }

    // ✅ Check for win in any direction
    public boolean checkWin(char color) {
        // Horizontal
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] == color && board[row][col + 1] == color &&
                    board[row][col + 2] == color && board[row][col + 3] == color) {
                    return true;
                }
            }
        }

        // Vertical
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row <= 2; row++) {
                if (board[row][col] == color && board[row + 1][col] == color &&
                    board[row + 2][col] == color && board[row + 3][col] == color) {
                    return true;
                }
            }
        }

        // Diagonal (bottom-left to top-right)
        for (int row = 3; row < 6; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] == color && board[row - 1][col + 1] == color &&
                    board[row - 2][col + 2] == color && board[row - 3][col + 3] == color) {
                    return true;
                }
            }
        }

        // Diagonal (top-left to bottom-right)
        for (int row = 0; row <= 2; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] == color && board[row + 1][col + 1] == color &&
                    board[row + 2][col + 2] == color && board[row + 3][col + 3] == color) {
                    return true;
                }
            }
        }

        return false;
    }

    // ✅ Check if board is full (for draw)
    public boolean isFull() {
        for (int col = 0; col < 7; col++) {
            if (board[0][col] == ' ') {
                return false;
            }
        }
        return true;
    }

    // ✅ Accessor for visual display
    public char[][] getBoard() {
        return board;
    }
}
