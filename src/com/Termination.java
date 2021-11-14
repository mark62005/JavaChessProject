package com;

public enum Termination {
    // TODO: work on draw
    WHITE_WON ("Game over - 1-0 - White won"),
    BLACK_WON ("Game over - 1-0 - Black won"),
    WHITE_WON_CHECKMATE ("Game over - 1-0 - White won by checkmate"),
    BLACK_WON_CHECKMATE ("Game over - 1-0 - Black won by checkmate"),
    WHITE_WON_RESIGNATION ("Game over - 1-0 - White won by resignation"),
    BLACK_WON_RESIGNATION ("Game over - 1-0 - Black won by resignation"),
    DRAW_STALEMATE ("Game over - Draw by stalemate"),
    DRAW_FIFTY_MOVE ("Game over - Draw by fifty-move");

    private final String score;

    Termination(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public static Termination winByCheckmate(Color color) {
        if (color.equals(Color.WHITE)) {
            return WHITE_WON_CHECKMATE;
        }
        return BLACK_WON_CHECKMATE;
    }

    public static Termination winByResignation(Color color) {
        if (color.equals(Color.WHITE)) {
            return WHITE_WON_RESIGNATION;
        }
        return BLACK_WON_RESIGNATION;
    }

    public static Termination winByCapturingKing(Color color) {
        if (color.equals(Color.WHITE)) {
            return WHITE_WON_RESIGNATION;
        }
        return BLACK_WON_RESIGNATION;
    }
}
