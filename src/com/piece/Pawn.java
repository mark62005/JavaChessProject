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

        for (int i = 0; i < possibleMoveCandidates.length; i++) {

            Square candidate = possibleMoveCandidates[i];
            if (candidate.isWithinBorder() && isNotAlly(game, candidate)) {
                    if (i == 0) {

                        if (
                                candidate.getRank() == getLastRank(game) &&
                                game.getPieceAt(candidate) == null
                        ) {
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
                                isOnInitialSquare(game) &&
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

    private void checkAttackMoveConditions(Game game, Set<Move> possibleMoves, Square candidate) {
        Pawn enemy = getEnemyPawnBeside(game, candidate);
        // check if that candidate matches the conditions to perform En Passant
        if (
                canEnPassant &&
                isOnFifthRank(game) &&
                enemy != null &&
                enemy.canBeCapturedByEnPassant()
        ) {
            addEnPassantMove(possibleMoves, square, candidate, enemy);
        } else if (isEnemy(game, candidate)) {
            // else,
            if (candidate.getRank() == getLastRank(game)) {
                // promotion & attack move
                addPromotionMove(possibleMoves, square, candidate, game);
            } else {
                // normal attack move
                addAttackMove(possibleMoves, square, candidate, game);
            }
        }
    }

    public boolean isOnFifthRank(Game game) {
        return square.getRank() == getFifthRank(game);
    }

    public Pawn getEnemyPawnBeside(Game game, Square candidate) {
        Square enemySquare = new Square(getFifthRank(game), candidate.getFile());
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

    private boolean isOnInitialSquare(Game game) {
        return square.getRank() == getInitialRank(game);
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
