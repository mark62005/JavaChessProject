package com.move;

public enum CastleSide {
    KING_SIDE (5, 6),
    QUEEN_SIDE (3, 2);

    private final int finalRookFile;
    private final int finalKingFile;

    CastleSide(int finalRookFile, int finalKingFile) {
        this.finalRookFile = finalRookFile;
        this.finalKingFile = finalKingFile;
    }

    public int getFinalKingFile() {
        return finalKingFile;
    }

    public int getFinalRookFile() {
        return finalRookFile;
    }

    @Override
    public String toString() {
        return "CastleSide{" +
                "finalRookFile=" + finalRookFile +
                ", finalKingFile=" + finalKingFile +
                '}';
    }
}
