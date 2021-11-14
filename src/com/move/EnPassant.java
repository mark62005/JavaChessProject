package com.move;

import com.Game;
import com.Square;
import com.piece.Piece;

public class EnPassant extends AttackMove {
    public EnPassant(Square from, Square to, Piece enemy) {
        super(from, to, enemy);
    }

    @Override
    public void makeAMove(Game game, Piece myPiece) {
        super.makeAMove(game, myPiece);
    }

    @Override
    public void captureEnemy(Game game, Square to) {
        super.captureEnemy(game, enemy.getSquare());
    }

    @Override
    protected void updateBoard(Game game, Piece myPiece) {
        super.updateBoard(game, myPiece);
        game.setPieceAt(enemy.getSquare(), null);
    }

    @Override
    public String toString() {
        return "EnPassant{" +
                " from=" + from +
                ", to=" + to +
                ", enemy=" + enemy +
                '}';
    }
}
