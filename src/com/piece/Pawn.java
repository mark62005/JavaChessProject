package com.piece;

import com.Game;
import com.Square;
import com.move.Move;

import java.util.HashSet;
import java.util.Set;

import static com.piece.PawnRank.*;

public class Pawn extends Piece {

    private boolean isFirstMove;
    private boolean canEnPassant;
    private boolean canBeCapturedByEnPassant;

    public Pawn(boolean isWhite) {
        super(PieceValue.PAWN_VALUE, isWhite);
        setSymbol(isWhite);
        this.isFirstMove = true;
        this.canEnPassant = false;
        this.canBeCapturedByEnPassant = false;
    }

    @Override
    protected void setSymbol(boolean isWhite) {
        if (isWhite) {
            this.symbol = PieceSymbol.WHITE_PAWN_SYMBOL;
        } else {
            this.symbol = PieceSymbol.BLACK_PAWN_SYMBOL;
        }
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    public void setCanEnPassant(boolean canEnPassant) {
        this.canEnPassant = canEnPassant;
    }

    public boolean canBeCapturedByEnPassant() {
        return canBeCapturedByEnPassant;
    }

    public void setCanBeCapturedByEnPassant(boolean canBeCapturedByEnPassant) {
        this.canBeCapturedByEnPassant = canBeCapturedByEnPassant;
    }

    @Override
    public void move() {
        System.out.println("Forward 1");
    }

    @Override
    public Set<Move> findPossibleMoves(Game game) {
        Set<Move> possibleMoves = new HashSet<>();

        int currRank = square.getRank();
        int currFile = square.getFile();
        // if the piece is white, moves upward
        int offset = 1;
        // if the piece is black, moves downward
        if (!isWhite) {
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

        // TODO: work on en passant
        for (int i = 0; i < possibleMoveCandidates.length; i++) {

            Square candidate = possibleMoveCandidates[i];
            if (candidate.isWithinBorder() && isNotAlly(game, candidate)) {
                    if (i == 0) {

                        int lastRank = getLastRank();
                        if (candidate.getRank() == lastRank) {
                            // add promotion move
                            addPromotionMove(possibleMoves, square, candidate);
                        } else {
                            if (!isEnemy(game, candidate)) {
                                // normal move
                                addNormalMove(possibleMoves, square, candidate);
                            }
                        }

                    } else if (i == 1) {
                        Square frontSquare = possibleMoveCandidates[0];
                        // pawn jump
                        if (
                                isFirstMove &&
                                isOnInitialSquare() &&
                                isNotBlocked(game, frontSquare) &&
                                !isEnemy(game, candidate)
                        ) {
                            addNormalMove(possibleMoves, square, candidate);
                        }
                    } else {
                        checkAttackMoveConditions(game, possibleMoves, candidate);
                    }
            }

        }
        return possibleMoves;
    }

    public int getLastRank() {
        if (isWhite) {
            return WHITE_PAWN_LAST_RANK;
        }
        return BLACK_PAWN_LAST_RANK;
    }

    public int getJumpedRank() {
        if (isWhite) {
            return WHITE_PAWN_JUMPED_RANK;
        }
        return BLACK_PAWN_JUMPED_RANK;
    }

    private void checkAttackMoveConditions(Game game, Set<Move> possibleMoves, Square candidate) {
        Pawn enemy = getEnemyPawnBeside(game, candidate);
        // check if that candidate matches the conditions to perform En Passant
        if (
                canEnPassant &&
                isOnFifthRank() &&
                enemy != null &&
                enemy.canBeCapturedByEnPassant()
        ) {
            addEnPassantMove(possibleMoves, square, candidate, enemy);
        } else if (isEnemy(game, candidate)) {
            // else,
            // check if it matches the condition to perform a normal attack move
            addAttackMove(possibleMoves, square, candidate, game);
        }
    }

    private int getFifthRank() {
        if (isWhite) {
            return WHITE_PAWN_FIFTH_RANK;
        }
        return BLACK_PAWN_FIFTH_RANK;
    }

    public boolean isOnFifthRank() {
        return square.getRank() == getFifthRank();
    }

    public Pawn getEnemyPawnBeside(Game game, Square candidate) {
        Square enemySquare = new Square(getFifthRank(), candidate.getFile());
        if (enemySquare.isWithinBorder()) {
            Piece enemy = game.getPieceAt(enemySquare);
            if (isEnemy(game, enemySquare) && enemy.getValue() == PieceValue.PAWN_VALUE) {
                return (Pawn) enemy;
            }
        }
        return null;
    }

    private boolean isNotBlocked(Game game, Square frontSquare) {
        return game.getPieceAt(frontSquare) == null;
    }

    private boolean isOnInitialSquare() {
        return square.getRank() == getInitialRank();
    }

    public int getInitialRank() {
        if (isWhite) {
            return WHITE_PAWN_INITIAL_RANK;
        }
        return BLACK_PAWN_INITIAL_RANK;
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "isWhite=" + isWhite +
                ", square=" + square +
                ", symbol=" + symbol +
                ", canBeCapturedByEnPassant=" + canBeCapturedByEnPassant +
                '}';
    }
}
