package com.ntf.client;

import java.util.*;

public class Ai {

    public int chooseMove(GameBoard board, boolean isRedAI) {
        List<Integer> validMoves = board.getValidColumns();

        for (int col : validMoves) {
            GameBoard test = new GameBoard(board); 
            test.dropPiece(col, isRedAI);
            if (test.isWinningMove(col, isRedAI)) {
                return col;
            }
        }

        for (int col : validMoves) {
            GameBoard test = new GameBoard(board); 
            test.dropPiece(col, !isRedAI);
            if (test.isWinningMove(col, !isRedAI)) {
                return col;
            }
        }

        Random rand = new Random();
        return validMoves.get(rand.nextInt(validMoves.size()));
    }
}
