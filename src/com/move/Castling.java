package com.move;

import com.*;
import com.piece.Piece;

public class Castling extends Move {

    private final CastleSide castleSide;

    public Castling(Square from, Square to, CastleSide castleSide) {
        super(from, to);
        this.castleSide = castleSide;
    }

    @Override
    public void makeAMove(Game game, Piece myPiece) {
        System.out.println("Castling.......");
        super.makeAMove(game, myPiece);
        updateRook(game);
    }

    private void updateRook(Game game) {
        int rank = from.getRank();
        Square currRookSquare = getCurrRookSquare();
        Piece rook = game.getPieceAt(currRookSquare);

        Square finalRookSquare = new Square(rank, castleSide.getFinalRookFile());
        // update the square of myPiece
        rook.setSquare(finalRookSquare);
        // check for special conditions for future moves (e.g. Castling, En Passant and Promotion)
        checkSpecialConditions(game, rook);

        // update board
        game.setPieceAt(finalRookSquare, rook);
        game.setPieceAt(currRookSquare, null);
    }

    private Square getCurrRookSquare() {
        int rank = from.getRank();
        Square square = new Square(rank, 0);
        if (castleSide.equals(CastleSide.KING_SIDE)) {
            square = new Square(rank, 7);
        }
        return square;
    }

    @Override
    public String toString() {
        return "Castling{" +
                "castleSide=" + castleSide +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
