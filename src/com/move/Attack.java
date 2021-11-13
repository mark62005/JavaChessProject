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

    default Player getOpponent(Game game) {
        if (game.getColorToMove().equals(Color.WHITE)) {
            return game.getBlackPlayer();
        }
        return game.getWhitePlayer();
    }

    default void captureEnemy(Game game, Square to) {
        Piece enemy = getEnemy(game, to);
        Player opponent = getOpponent(game);
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
        System.out.println("attack");
    }

}
