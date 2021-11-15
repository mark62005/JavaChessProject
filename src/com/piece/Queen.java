package com.piece;

import com.Game;
import com.Square;
import com.move.Move;

import java.util.HashSet;
import java.util.Set;

public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(PieceValue.QUEEN_VALUE, isWhite);
        setSymbol(isWhite);
    }

    @Override
    public void setSymbol(boolean isWhite) {
        if (isWhite) {
            this.symbol = PieceSymbol.WHITE_QUEEN_SYMBOL;
        } else {
            this.symbol = PieceSymbol.BLACK_QUEEN_SYMBOL;
        }
    }

    @Override
    public Set<Move> findPossibleMoves(Game game) {
        Set<Move> possibleMoves = new HashSet<>();

        int currRank = square.getRank();
        int currFile = square.getFile();
        // https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Chessboard480.svg/416px-Chessboard480.svg.png
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

            currCandidate : {
                // while the candidate to check is within board border
                while (candidate.isWithinBorder()) {
                    // if that candidate is not an ally, add that candidate
                    if (isNotAlly(game, candidate)) {
                        // if that candidate is an enemy, add an attack move,
                        // then move to next possibleMoveCandidate
                        if (isEnemy(game, candidate)) {
                            addAttackMove(possibleMoves, square, candidate, game);
                            break currCandidate;
                        } else {
                            // if that candidate is not an enemy, add a normal move,
                            addNormalMove(possibleMoves, square, candidate);
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
                                // upper left
                            } else if (candidateRank > currRank && candidateFile < currFile) {
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
        return "Queen{" +
                "isWhite=" + isWhite +
                ", square=" + square +
                ", symbol=" + symbol +
                '}';
    }
}
