package com.move;

import com.Game;
import com.Square;
import com.piece.Piece;

// TODO
public class Castling extends Move {

    private CastleSide castleSide;

    public Castling(Square from, Square to, CastleSide castleSide) {
        super(from, to);
        this.castleSide = castleSide;
    }

    @Override
    public void makeAMove(Game game, Piece myPiece) {
        super.makeAMove(game, myPiece);

    }

    @Override
    protected void updateBoard(Game game, Piece myPiece) {
        super.updateBoard(game, myPiece);
        //
    }
}
