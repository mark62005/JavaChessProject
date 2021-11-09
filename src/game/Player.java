package game;

import piece.Piece;
import piece.PieceValue;
import position.Position;

import java.util.*;

public class Player {

    private Side side;
    private List<Piece> pieces = new ArrayList<>();
    private boolean isKingCaptured;
    private Position kingPos;

    public Player(Side side) {
        this.side = side;
        this.isKingCaptured = false;
    }

    public Side getSide() {
        return side;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public boolean isKingCaptured() {
        return isKingCaptured;
    }

    public void setKingCaptured(boolean kingCaptured) {
        isKingCaptured = kingCaptured;
    }

    public Position getKingPos() {
        return this.kingPos;
    }

    public void setKingPos(Position kingPos) {
        if (this.kingPos == null) {
            for (Piece piece : pieces) {
                if (piece.getValue() == 1000) {
                    this.kingPos = piece.getPosition();
                }
            }
        } else {
            this.kingPos = kingPos;
        }
    }

    public void addAPiece(Piece piece) {
        pieces.add(piece);

        if (piece.getValue() == PieceValue.KING_VALUE) {
            kingPos = piece.getPosition();
        }
    }

}
