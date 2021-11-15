package com.piece;

import com.Color;
import com.Game;

public class PawnRank {

    public static final int WHITE_PAWN_INITIAL_RANK = 1;
    public static final int WHITE_PAWN_JUMPED_RANK = 3;
    public static final int WHITE_PAWN_FIFTH_RANK = 4;
    public static final int WHITE_PAWN_LAST_RANK = 7;

    public static final int BLACK_PAWN_INITIAL_RANK = 6;
    public static final int BLACK_PAWN_JUMPED_RANK = 4;
    public static final int BLACK_PAWN_FIFTH_RANK = 3;
    public static final int BLACK_PAWN_LAST_RANK = 0;

    public static int getInitialRank(Game game) {
        if (game.getColorToMove().equals(Color.WHITE)) {
            return WHITE_PAWN_INITIAL_RANK;
        }
        return BLACK_PAWN_INITIAL_RANK;
    }

    public static int getJumpedRank(Game game) {
        if (game.getColorToMove().equals(Color.WHITE)) {
            return WHITE_PAWN_JUMPED_RANK;
        }
        return BLACK_PAWN_JUMPED_RANK;
    }

    public static int getFifthRank(Game game) {
        if (game.getColorToMove().equals(Color.WHITE)) {
            return WHITE_PAWN_FIFTH_RANK;
        }
        return BLACK_PAWN_FIFTH_RANK;
    }

    public static int getLastRank(Game game) {
        if (game.getColorToMove().equals(Color.WHITE)) {
            return WHITE_PAWN_LAST_RANK;
        }
        return BLACK_PAWN_LAST_RANK;
    }

}
