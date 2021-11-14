package com.move;

import com.*;
import com.piece.Piece;

import java.util.Arrays;
import java.util.Set;

public class Castling extends Move {

    private CastleSide castleSide;

    public Castling(Square from, Square to, CastleSide castleSide) {
        super(from, to);
        this.castleSide = castleSide;
    }

    public static Castling parseCastlingUCI(String userInput) {
        CastleSide castleSide = CastleSide.KING_SIDE;
        if (!Arrays.asList(castleSide.getUcis()).contains(userInput)) {
            castleSide = CastleSide.QUEEN_SIDE;
        }

        Square from = Square.parse(userInput.substring(0, 2));
        Square to = Square.parse(userInput.substring(2, 4));
        return new Castling(from, to, castleSide);
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
        checkSpecialConditions(rook);

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
