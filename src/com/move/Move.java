package com.move;

import com.*;
import com.piece.*;

import java.util.Arrays;
import java.util.Objects;

import static com.InputValidators.SQUARE_REGEX;
import static com.piece.PieceValue.*;

public class Move {
    protected final Square from;
    protected final Square to;

    public Move(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    public void makeAMove(Game game, Piece myPiece) {
        // update myPiece
        updateMyPiece(myPiece);
        // update the board
        updateBoard(game, myPiece);

        if (myPiece.getValue() == KING_VALUE) {
            updateKingPos(game);
        }
    }

    protected void updateMyPiece(Piece myPiece) {
        // update the square of myPiece
        myPiece.setSquare(to);
        // check for special conditions for future moves (e.g. Castling, En Passant and Promotion)
        checkSpecialConditions(myPiece);

        myPiece.setPrevMove(this);
    }

    protected void checkSpecialConditions(Piece piece) {
        int value = piece.getValue();

        if (value == KING_VALUE) {
            King king = (King) piece;
            king.setCanCastling(false);
        } else if (value == ROOK_VALUE) {
            Rook rook = (Rook) piece;
            rook.setCanCastling(false);
        } else if (value == PAWN_VALUE) {
            Pawn pawn = (Pawn) piece;
            // TODO: work on promotion condition
            pawn.setFirstMove(false);
            if (canEnPassant(pawn)) {
                pawn.setCanEnPassant(true);
            }
        }
    }

    // TODO: work on enemy condition
    private boolean canEnPassant(Pawn pawn) {
        if (pawn.isWhite()) {
            return pawn.getSquare().getRank() == 5;
        } else {
            return pawn.getSquare().getRank() == 4;
        }
    }

    protected void updateBoard(Game game, Piece myPiece) {
        // update "to" square
        game.setPieceAt(to, myPiece);
        // update current square
        game.setPieceAt(from, null);
    }

    private void updateKingPos(Game game) {
        Player player = game.getWhitePlayer();
        if (game.getColorToMove().equals(Color.BLACK)) {
            player = game.getBlackPlayer();
        }

        player.setKingPos(to);
    }

    public static Move parseUCI(String userInput) {
        if (!InputValidators.isUciPattern(userInput)) {
            throw new IllegalArgumentException("Invalid input. Please follow the format of UCI input.");
        }

        // TODO: work on promotion UCI
        String uci = userInput.substring(0, 4);
        Square from = Square.parse(uci.substring(0, 2));
        Square to = Square.parse(uci.substring(2, 4));

        if (!from.isWithinBorder() || !to.isWithinBorder()) {
            // TODO: work on error message
            throw new IllegalArgumentException("Invalid input. Target squares are outside the board.");
        }
        return new Move(from, to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "Move{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
