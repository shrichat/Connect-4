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
    public GameBoard(GameBoard other) {
        board = new char[6][7];
        for (int row = 0; row < 6; row++) {
            System.arraycopy(other.board[row], 0, board[row], 0, 7);
        }
    }

    public int placeCoin(int col, char color) {
        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == ' ') {
                board[row][col] = color;
                return row;
            }
        }
        return -1;
    }

    public boolean dropPiece(int col, boolean isRed) {
        char color;
        if (isRed) {
            color = 'R';
        } else {
            color = 'Y';
        }
        return placeCoin(col, color) != -1;
    }

    public List<Integer> getValidColumns() {
        List<Integer> valid = new ArrayList<>();
        for (int col = 0; col < 7; col++) {
            if (board[0][col] == ' ') {
                valid.add(col);
            }
        }
        return valid;
    }


    public boolean isWinningMove(int col, boolean isRed) {
    	if (isRed) {
    	    return checkWin('R');
    	} else {
    	    return checkWin('Y');
    	}
    }

    public boolean checkWin(char color) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] == color && board[row][col + 1] == color &&
                    board[row][col + 2] == color && board[row][col + 3] == color) {
                    return true;
                }
            }
        }


        for (int col = 0; col < 7; col++) {
            for (int row = 0; row <= 2; row++) {
                if (board[row][col] == color && board[row + 1][col] == color &&
                    board[row + 2][col] == color && board[row + 3][col] == color) {
                    return true;
                }
            }
        }

        for (int row = 3; row < 6; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] == color && board[row - 1][col + 1] == color &&
                    board[row - 2][col + 2] == color && board[row - 3][col + 3] == color) {
                    return true;
                }
            }
        }

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

    public boolean isFull() {
        for (int col = 0; col < 7; col++) {
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
