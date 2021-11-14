package com.move;

import java.util.Arrays;

public enum CastleSide {
    KING_SIDE (5, 6, new String[]{ "e1g1", "e8g8" }),
    QUEEN_SIDE (3, 2, new String[]{ "e1c1", "e8c8" });

    private final int finalRookFile;
    private final int finalKingFile;
    private final String[] ucis;

    CastleSide(int finalRookFile, int finalKingFile, String[] ucis) {
        this.finalRookFile = finalRookFile;
        this.finalKingFile = finalKingFile;
        this.ucis = ucis;
    }

    public int getFinalKingFile() {
        return finalKingFile;
    }

    public int getFinalRookFile() {
        return finalRookFile;
    }

    public String[] getUcis() {
        return ucis;
    }

    @Override
    public String toString() {
        return "CastleSide{" +
                ", ucis=" + Arrays.toString(ucis) +
                '}';
    }
}
