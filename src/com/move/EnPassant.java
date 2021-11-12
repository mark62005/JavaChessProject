package com.move;

import com.Game;
import com.Square;
import com.piece.Pawn;
import com.piece.Piece;

// TODO
public class EnPassant extends AttackMove {
    public EnPassant(Square from, Square to, Piece enemy) {
        super(from, to, enemy);
    }

    @Override
    public void makeAMove(Game game, Piece myPiece) {
        super.makeAMove(game, myPiece);
    }

    @Override
    protected void updateMyPiece(Piece myPiece) {
        Pawn pawn = (Pawn) myPiece;

        pawn.setSquare(to);
        pawn.setCanEnPassant(false);
        pawn.setPrevMove(this);
    }

    @Override
    protected void updateBoard(Game game, Piece myPiece) {
        super.updateBoard(game, myPiece);
        game.setPieceAt(enemy.getSquare(), null);
    }
}
