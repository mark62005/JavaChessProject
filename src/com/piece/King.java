package com.piece;

import com.Color;
import com.Game;
import com.Square;
import com.move.AttackMove;
import com.move.Move;

import java.util.HashSet;
import java.util.Set;

public class King extends Piece {
    private boolean canCastling;

    public King(boolean isWhite) {
        super(PieceValue.KING_VALUE, isWhite);
        setSymbol(isWhite);
        this.canCastling = true;
    }

    @Override
    public void setSymbol(boolean isWhite) {
        if (isWhite) {
            this.symbol = PieceSymbol.WHITE_KING_SYMBOL;
        } else {
            this.symbol = PieceSymbol.BLACK_KING_SYMBOL;
        }
    }

    public boolean isCanCastling() {
        return canCastling;
    }

    public void setCanCastling(boolean canCastling) {
        this.canCastling = canCastling;
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
        // https://en.wikipedia.org/wiki/King_(chess)
        Square[] possibleMoveCandidates = {
                // up
                new Square(currRank + 1, currFile),
                // upper right
                new Square(currRank + 1, currFile + 1),
                // right
                new Square(currRank, currFile + 1),
                // lower right
                new Square(currRank - 1, currFile + 1),
                // down
                new Square(currRank - 1, currFile),
                // lower left
                new Square(currRank - 1, currFile - 1),
                // left
                new Square(currRank, currFile - 1),
                // upper left
                new Square(currRank + 1, currFile - 1),
        };

        for (Square candidate : possibleMoveCandidates) {
            if (candidate.isWithinBorder() && isNotAlly(game, candidate)) {
                // if the candidate square is an enemy, add an attack move
                if (isEnemy(game, candidate)) {
                    addAttackMove(possibleMoves, this.square, candidate, game);
                } else if (true) {
                    // TODO: work on castling
                } else {
                    // else, add a normal move
                    addNormalMove(possibleMoves, this.square, candidate);
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public String toString() {
        return "King{" +
                "isWhite=" + isWhite +
                ", square=" + square +
                ", symbol=" + symbol +
                '}';
    }
}
