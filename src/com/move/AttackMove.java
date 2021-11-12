package com.move;

import com.Color;
import com.Game;
import com.Player;
import com.Square;
import com.piece.Piece;
import com.piece.PieceValue;

public class AttackMove extends Move {

    protected Piece enemy;

    public AttackMove(Square from, Square to, Piece enemy) {
        super(from, to);
        this.enemy = enemy;
    }

    @Override
    public void makeAMove(Game game, Piece myPiece) {
        super.makeAMove(game, myPiece);

        captureEnemy(game);
    }

    protected Player getOpponent(Game game) {
        if (game.getColorToMove().equals(Color.WHITE)) {
            return game.getBlackPlayer();
        }
        return game.getWhitePlayer();
    }

    protected void captureEnemy(Game game) {
        Player opponent = getOpponent(game);
        for (Piece piece : opponent.getPieces()) {
            if (enemy.equals(piece)) {
                opponent.getPieces().remove(enemy);
                break;
            }
        }

        if (enemy.getValue() == PieceValue.KING_VALUE) {
            opponent.setKingCaptured(true);
        }
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
