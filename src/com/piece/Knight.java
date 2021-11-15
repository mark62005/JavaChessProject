package com.piece;

import com.Game;
import com.Square;
import com.move.Move;

import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(PieceValue.KNIGHT_VALUE, isWhite);
        setSymbol(isWhite);
    }

    @Override
    public void setSymbol(boolean isWhite) {
        if (isWhite) {
            this.symbol = PieceSymbol.WHITE_KNIGHT_SYMBOL;
        } else {
            this.symbol = PieceSymbol.BLACK_KNIGHT_SYMBOL;
        }
    }

    @Override
    public Set<Move> findPossibleMoves(Game game) {
        Set<Move> possibleMoves = new HashSet<>();

        int currRank = this.square.getRank();
        int currFile = this.square.getFile();
        // https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Chess_oot45.svg/52px-Chess_oot45.svg.png
        Square[] possibleMoveCandidates = {
                new Square(currRank + 2, currFile - 1),
                new Square(currRank + 2, currFile + 1),
                new Square(currRank + 1, currFile - 2),
                new Square(currRank + 1, currFile + 2),
                new Square(currRank - 1, currFile - 2),
                new Square(currRank - 1, currFile + 2),
                new Square(currRank - 2, currFile - 1),
                new Square(currRank - 2, currFile + 1),
        };

        for (Square candidate : possibleMoveCandidates) {
            if (candidate.isWithinBorder() && isNotAlly(game, candidate)) {
                // if the candidate square is an enemy, add an attack move
                if (isEnemy(game, candidate)) {
                    addAttackMove(possibleMoves, this.square, candidate, game);
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
        return "Knight{" +
                "isWhite=" + isWhite +
                ", square=" + square +
                ", symbol=" + symbol +
                '}';
    }
}
