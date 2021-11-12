package com.piece;

import com.Color;
import com.Game;
import com.Square;
import com.move.AttackMove;
import com.move.Move;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Pawn extends Piece {
    private boolean promoted;
    private Piece newPiece;
    private boolean isFirstMove;
    private boolean canEnPassant;

    public Pawn(boolean isWhite) {
        super(PieceValue.PAWN_VALUE, isWhite);
        setSymbol(isWhite);
        this.isFirstMove = true;
        this.canEnPassant = false;
    }

    @Override
    public void setSymbol(boolean isWhite) {
        if (isWhite) {
            this.symbol = PieceSymbol.WHITE_PAWN_SYMBOL;
        } else {
            this.symbol = PieceSymbol.BLACK_PAWN_SYMBOL;
        }
    }

    public boolean isPromoted() {
        return promoted;
    }

    // TODO: work on promotion
    public void promote(Piece newPiece) {
        if (!promoted) {
            this.promoted = true;
            this.newPiece = newPiece;
        } else {
            System.out.println("It's already been promoted.");
        }
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    public boolean canEnPassant() {
        return canEnPassant;
    }

    public void setCanEnPassant(boolean canEnPassant) {
        this.canEnPassant = canEnPassant;
    }

    @Override
    public void move() {
        System.out.println("One square");
    }

    @Override
    public Set<Move> findPossibleMoves(Game game) {
        Set<Move> possibleMoves = new HashSet<>();

        int currRank = this.square.getRank();
        int currFile = this.square.getFile();
        // if the piece is white, moves upward
        int offset = 1;
        // if the piece is black, moves downward
        if (!this.isWhite) {
            offset = -1;
        }
        // https://en.wikipedia.org/wiki/Pawn_(chess)
        Square[] possibleMoveCandidates = {
                // forward 1
                new Square(currRank + offset, currFile),
                // forward 2
                new Square(currRank + (offset * 2), currFile),
                // left diagonal
                new Square(currRank + offset, currFile - 1),
                // right diagonal
                new Square(currRank + offset, currFile + 1),
        };

        // TODO: work on promotions
        // TODO: work on en passant
        for (int i = 0; i < possibleMoveCandidates.length; i++) {

            Square candidate = possibleMoveCandidates[i];
            if (candidate.isWithinBorder() && isNotAlly(game, candidate)) {
                if (!isEnemy(game, candidate)) {
                    // normal move
                    if (i == 0) {
                        addNormalMove(possibleMoves, this.square, candidate);
                        // pawn jump
                    } else if (
                            i == 1 &&
                            game.getPieceAt(possibleMoveCandidates[0]) == null &&
                            isFirstMove
                    ) {
                        addNormalMove(possibleMoves, this.square, candidate);
                    }
                } else {
                    // attack
                    addAttackMove(possibleMoves, this.square, candidate, game);
                }
            }

        }
        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pawn pawn = (Pawn) o;
        return promoted == pawn.promoted && newPiece.equals(pawn.newPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promoted, newPiece);
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "isWhite=" + isWhite +
                ", square=" + square +
                ", symbol=" + symbol +
                '}';
    }
}
