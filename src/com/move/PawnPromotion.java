package com.move;

import com.Square;
import com.piece.Piece;
import com.piece.PieceValue;

// TODO
public class PawnPromotion extends Move {
    private Piece promoteTo;

    public PawnPromotion(Square from, Square to, Piece promoteTo) {
        super(from, to);
        setPromoteTo(promoteTo);
    }

    public Piece getPromoteTo() {
        return promoteTo;
    }

    public void setPromoteTo(Piece promoteTo) {
        if (promoteTo.getValue() == PieceValue.KING_VALUE ||
                promoteTo.getValue() == PieceValue.PAWN_VALUE) {
            throw new IllegalArgumentException("Invalid input. A pawn cannot be promoted to a King or a Pawn.");
        }
        this.promoteTo = promoteTo;
    }
}
