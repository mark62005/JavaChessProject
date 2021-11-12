package com;

import com.piece.Piece;
import com.piece.PieceValue;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private Color color;
    private List<Piece> pieces;
    private boolean isKingCaptured;
    private Square kingPos;

    public Player(Color color) {
        this.color = color;
        this.pieces = new ArrayList<>();
        this.isKingCaptured = false;
    }

    public Color getColor() {
        return color;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public boolean isKingCaptured() {
        return isKingCaptured;
    }

    public void setKingCaptured(boolean kindCaptured) {
        isKingCaptured = kindCaptured;
    }

    public void addAPiece(Piece piece) {
        pieces.add(piece);

        if (piece.getValue() == PieceValue.KING_VALUE) {
            kingPos = piece.getSquare();
        }
    }

    public Square getKingPos() {
        return this.kingPos;
    }

    public void setKingPos(Square kingPos) {
        this.kingPos = kingPos;
    }

}
