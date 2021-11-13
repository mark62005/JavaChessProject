package com.piece;

import com.Color;
import com.Game;
import com.Square;
import com.move.AttackMove;
import com.move.Move;

import java.util.HashSet;
import java.util.Set;

public class Rook extends Piece {

    private boolean canCastling;

    public Rook(boolean isWhite) {
        super(PieceValue.ROOK_VALUE, isWhite);
        setSymbol(isWhite);
        this.canCastling = true;
    }

    @Override
    public void setSymbol(boolean isWhite) {
        if (isWhite) {
            this.symbol = PieceSymbol.WHITE_ROOK_SYMBOL;
        } else {
            this.symbol = PieceSymbol.BLACK_ROOK_SYMBOL;
        }
    }

    public boolean CanCastling() {
        return canCastling;
    }

    public void setCanCastling(boolean canCastling) {
        this.canCastling = canCastling;
    }

    @Override
    public void move() {
        System.out.println("Horizontally or vertically");
    }

    @Override
    public Set<Move> findPossibleMoves(Game game) {
        Set<Move> possibleMoves = new HashSet<>();

        int currRank = this.square.getRank();
        int currFile = this.square.getFile();
        // https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Chessboard480.svg/416px-Chessboard480.svg.png
        Square[] possibleMoveCandidates = {
                // up
                new Square(currRank + 1, currFile),
                // right
                new Square(currRank, currFile + 1),
                // down
                new Square(currRank - 1, currFile),
                // left
                new Square(currRank, currFile - 1),
        };

        // TODO: work on castling
        for (Square candidate : possibleMoveCandidates) {

            currCandidate: {
                // while the candidate to check is within board border
                while (candidate.isWithinBorder()) {

                    // if that candidate is not an ally, add that candidate
                    if (isNotAlly(game, candidate)) {
                        // if that candidate is an enemy, add an attack move,
                        // then move to next possibleMoveCandidate
                        if (isEnemy(game, candidate)) {
                            addAttackMove(possibleMoves, this.square, candidate, game);
                            break currCandidate;
                        } else {
                            // if that candidate is not an enemy, add a normal move,
                            addNormalMove(possibleMoves, this.square, candidate);
                            // then search 1 further square with the same direction
                            int candidateRank = candidate.getRank();
                            int candidateFile = candidate.getFile();

                            // up
                            if (candidateRank > currRank && candidateFile == currFile) {
                                candidate.addRank(1);
                                // right
                            } else if (candidateRank == currRank && candidateFile > currFile) {
                                candidate.addFile(1);
                                // down
                            } else if (candidateRank < currRank && candidateFile == currFile) {
                                candidate.addRank(-1);
                                // left
                            } else if (candidateRank == currRank && candidateFile < currFile) {
                                candidate.addFile(-1);
                            }
                        }
                    } else {
                        // if that candidate is an ally, move to next possibleMoveCandidate
                        break currCandidate;
                    }

                }
            }

        }
        return possibleMoves;
    }

    @Override
    public String toString() {
        return "Rook{" +
                "isWhite=" + isWhite +
                ", square=" + square +
                ", symbol=" + symbol +
                '}';
    }
}
