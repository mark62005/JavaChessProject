package com.move;

import com.Color;
import com.Game;
import com.Player;
import com.Square;
import com.piece.Piece;
import com.piece.PieceValue;

public interface Attack {

    default Piece getEnemy(Game game, Square to) {
        return game.getPieceAt(to);
    }

    default void captureEnemy(Game game, Square to) {
        Piece enemy = getEnemy(game, to);
        Player opponent = game.getOpponent();
        for (Piece piece : opponent.getPieces()) {
            if (enemy.equals(piece)) {
                opponent.getPieces().remove(enemy);
                break;
            }
        }
        // capture King
        if (enemy.getValue() == PieceValue.KING_VALUE) {
            opponent.setKingCaptured(true);
        }
    }

}
