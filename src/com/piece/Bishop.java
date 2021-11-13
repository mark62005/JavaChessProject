package com.piece;

import com.Color;
import com.Game;
import com.Square;
import com.move.AttackMove;
import com.move.Move;

import java.util.HashSet;
import java.util.Set;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(PieceValue.BISHOP_VALUE, isWhite);
        setSymbol(isWhite);
    }

    @Override
    public void setSymbol(boolean isWhite) {
        if (isWhite) {
            this.symbol = PieceSymbol.WHITE_BISHOP_SYMBOL;
        } else {
            this.symbol = PieceSymbol.BLACK_BISHOP_SYMBOL;
        }
    }

    @Override
    public void move() {
        System.out.println("Diagonally");
    }

    @Override
    public Set<Move> findPossibleMoves(Game game) {
        Set<Move> possibleMoves = new HashSet<>();

        int currRank = this.square.getRank();
        int currFile = this.square.getFile();
        // https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Chess_oot45.svg/52px-Chess_oot45.svg.png
        Square[] possibleMoveCandidates = {
                // upper left
                new Square(currRank + 1, currFile - 1),
                // upper right
                new Square(currRank + 1, currFile + 1),
                // lower left
                new Square(currRank - 1, currFile - 1),
                // lower right
                new Square(currRank - 1, currFile + 1),
        };

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

                            // upper left
                            if (candidateRank > currRank && candidateFile < currFile) {
                                candidate.add(1, -1);
                                // upper right
                            } else if (candidateRank > currRank && candidateFile > currFile) {
                                candidate.add(1, 1);
                                // lower right
                            } else if (candidateRank < currRank && candidateFile > currFile) {
                                candidate.add(-1, 1);
                                // lower left
                            } else if (candidateRank < currRank && candidateFile < currFile) {
                                candidate.add(-1, -1);
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
        return "Bishop{" +
                "isWhite=" + isWhite +
                ", square=" + square +
                ", symbol=" + symbol +
                '}';
    }
}
