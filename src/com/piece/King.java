package com.piece;

import com.Game;
import com.Square;
import com.move.CastleSide;
import com.move.Move;

import java.util.Arrays;
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

    public boolean canCastling() {
        return canCastling;
    }

    public void setCanCastling(boolean canCastling) {
        this.canCastling = canCastling;
    }

    @Override
    public Set<Move> findPossibleMoves(Game game) {
        Set<Move> possibleMoves = new HashSet<>();

        int currRank = square.getRank();
        int currFile = square.getFile();
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
                new Square(currRank + 1, currFile - 1)
        };

        for (Square candidate : possibleMoveCandidates) {
            if (candidate.isWithinBorder() && isNotAlly(game, candidate)) {
                // if the candidate square is an enemy, add an attack move
                if (isEnemy(game, candidate)) {
                    addAttackMove(possibleMoves, square, candidate, game);
                } else {
                    // else, add a normal move
                    addNormalMove(possibleMoves, square, candidate);
                }
            }
        }
        addCastlingMoves(game, possibleMoves);
        return possibleMoves;
    }

    private void addCastlingMoves(Game game, Set<Move> possibleMoves) {
        int rank = 0;
        if (!isWhite) {
            rank = 7;
        }
        addCastlingMove(game, possibleMoves, CastleSide.KING_SIDE, rank);
        addCastlingMove(game, possibleMoves, CastleSide.QUEEN_SIDE, rank);
    }

    private void addCastlingMove(Game game, Set<Move> possibleMoves, CastleSide side, int rank) {
        Square finalRookSquare = new Square(rank, side.getFinalRookFile());
        Square finalKingSquare = new Square(rank, side.getFinalKingFile());

        if (
                game.isRookAndKingAtInitialPosition() &&
                isNotInCheck(game) &&
                isClear(game, side, rank) &&
                isNotCastlingThroughCheck(game, finalRookSquare) &&
                isNotCastlingToCheck(game, finalKingSquare)
        ) {
            addCastlingMove(possibleMoves, square, finalKingSquare, side);
        }
    }

    private boolean isNotInCheck(Game game) {
        return game.isNotInCheck(square);
    }

    // check if the squares between King and Rook are empty
    private boolean isClear(Game game, CastleSide side, int rank) {
        Square[] castleSideCandidates = {
                new Square(rank, side.getFinalKingFile()),
                new Square(rank, side.getFinalRookFile()),
        };
        if (side.equals(CastleSide.QUEEN_SIDE)) {
            castleSideCandidates = new Square[]{
                    new Square(rank, side.getFinalKingFile()),
                    new Square(rank, side.getFinalRookFile()),
                    new Square(rank, 1),
            };
        }

        return Arrays.stream(castleSideCandidates)
                .allMatch(s -> game.getPieceAt(s) == null);
    }

    private boolean isNotCastlingThroughCheck(Game game, Square finalRookSquare) {
        return game.isNotInCheck(finalRookSquare);
    }

    private boolean isNotCastlingToCheck(Game game, Square finalKingSquare) {
        return game.isNotInCheck(finalKingSquare);
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
