package com;

public enum Termination {
    // TODO: work on termination message
    WHITE_WON_CHECKMATE ("score"),
    BLACK_WON_CHECKMATE ("score"),
    WHITE_WON_RESIGNATION ("score"),
    BLACK_WON_RESIGNATION ("score"),
    DRAW_STALEMATE ("stalemate"),
    DRAW_FIFTY_MOVE ("fifty move");

    private String score;

    Termination(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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
}
