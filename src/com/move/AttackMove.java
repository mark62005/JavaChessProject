package com.move;

import com.Game;
import com.Square;
import com.piece.Piece;

public class AttackMove extends Move implements Attack {

    protected Piece enemy;

    public AttackMove(Square from, Square to, Piece enemy) {
        super(from, to);
        this.enemy = enemy;
    }

    @Override
    public void makeAMove(Game game, Piece myPiece) {
        super.makeAMove(game, myPiece);
    }

    @Override
    protected void updateBoard(Game game, Piece myPiece) {
        captureEnemy(game, to);
        super.updateBoard(game, myPiece);
    }

    @Override
    public String toString() {
        return "AttackMove{" +
                "from=" + from +
                ", to=" + to +
                ", enemy=" + enemy +
                '}';
    }
}
