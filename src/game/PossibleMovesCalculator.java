package game;

import piece.Piece;
import position.Position;

import java.util.HashSet;
import java.util.Set;

public class PossibleMovesCalculator {

    public static Set<Position> findKnightMoves(Piece[][] board, Piece piece) {

        Set<Position> possibleMoves = new HashSet<>();

        int row = piece.getPosition().getRow();
        int col = piece.getPosition().getCol();
        // https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Chess_oot45.svg/52px-Chess_oot45.svg.png
        Position[] possibleMoveCandidates = {
                new Position(row + 2, col - 1),
                new Position(row + 2, col + 1),
                new Position(row + 1, col - 2),
                new Position(row + 1, col + 2),
                new Position(row - 1, col - 2),
                new Position(row - 1, col + 2),
                new Position(row - 2, col - 1),
                new Position(row - 2, col + 1),
        };

        for (Position position : possibleMoveCandidates) {
            if (position.isWithinBorder() &&
                    isNotAlly(board, position, piece.isWhite())
            ) {
                possibleMoves.add(position);
            }
        }

        return possibleMoves;
    }

    public static Set<Position> findBishopMoves(Piece[][] board, Piece piece) {

        int currRow = piece.getPosition().getRow();
        int currCol = piece.getPosition().getCol();
        Set<Position> possibleMoves = new HashSet<>();
        // https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Chess_oot45.svg/52px-Chess_oot45.svg.png
        Position[] possibleMoveCandidates = {
                new Position(currRow + 1, currCol - 1),
                new Position(currRow + 1, currCol + 1),
                new Position(currRow - 1, currCol - 1),
                new Position(currRow - 1, currCol + 1),
        };

        for (Position candidate : possibleMoveCandidates) {

            // while the candidate to check is within board border
            while (candidate.isWithinBorder()) {

                // if that candidate is not an ally, add that candidate
                if (isNotAlly(board, candidate, piece.isWhite())) {
                    possibleMoves.add(candidate);
                    // if that candidate is an enemy, move to next possibleMoveCandidate
                    if (isEnemy(board, candidate, piece.isWhite())) {
                        break;
                    } else {
                        // if that candidate is not an enemy, search 1 further position with the same direction

                        // upper left
                        if (candidate.getRow() > currRow && candidate.getCol() < currCol) {
                            candidate.setRow(candidate.getRow() + 1);
                            candidate.setCol(candidate.getCol() - 1);
                            // upper right
                        } else if (candidate.getRow() > currRow && candidate.getCol() > currCol) {
                            candidate.setRow(candidate.getRow() + 1);
                            candidate.setCol(candidate.getCol() + 1);
                            // lower right
                        } else if (candidate.getRow() < currRow && candidate.getCol() > currCol) {
                            candidate.setRow(candidate.getRow() - 1);
                            candidate.setCol(candidate.getCol() + 1);
                            // lower left
                        } else if (candidate.getRow() < currRow && candidate.getCol() < currCol) {
                            candidate.setRow(candidate.getRow() - 1);
                            candidate.setCol(candidate.getCol() - 1);
                        }
                    }
                } else {
                    // if that candidate is an ally, move to next possibleMoveCandidate
                    break;
                }

            }

        }

        return possibleMoves;
    }

    public static Set<Position> findRookMoves(Piece[][] board, Piece piece) {

        int currRow = piece.getPosition().getRow();
        int currCol = piece.getPosition().getCol();
        Set<Position> possibleMoves = new HashSet<>();

        // https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Chessboard480.svg/416px-Chessboard480.svg.png
        Position[] possibleMoveCandidates = {
                // up
                new Position(currRow + 1, currCol),
                // right
                new Position(currRow, currCol + 1),
                // down
                new Position(currRow - 1, currCol),
                // left
                new Position(currRow, currCol - 1),
        };

        // TODO: work on en passant
        for (Position position : possibleMoveCandidates) {

            // while the candidate to check is within board border
            while (position.isWithinBorder()) {

                // if that candidate is not an ally, add that candidate
                if (isNotAlly(board, position, piece.isWhite())) {
                    possibleMoves.add(position);
                    // if that candidate is an enemy, move to next possibleMoveCandidate
                    if (isEnemy(board, position, piece.isWhite())) {
                        break;
                    } else {
                        // if that candidate is not an enemy, search 1 further position with the same direction

                        // up
                        if (position.getRow() > currRow && position.getCol() == currCol) {
                            position.setRow(position.getRow() + 1);
                            // right
                        } else if (position.getRow() == currRow && position.getCol() > currCol) {
                            position.setCol(position.getCol() + 1);
                            // down
                        } else if (position.getRow() < currRow && position.getCol() == currCol) {
                            position.setRow(position.getRow() - 1);
                            // left
                        } else if (position.getRow() == currRow && position.getCol() < currCol) {
                            position.setCol(position.getCol() - 1);
                        }
                    }
                } else {
                    // if that candidate is an ally, move to next possibleMoveCandidate
                    break;
                }

            }

        }

        return possibleMoves;
    }

    public static Set<Position> findPawnMoves(Piece[][] board, Piece piece) {

        int currRow = piece.getPosition().getRow();
        int currCol = piece.getPosition().getCol();
        boolean isWhite = piece.isWhite();

        Set<Position> possibleMoves = new HashSet<>();

        // if the piece is white, moves upward
        int offset = 1;
        // if the piece is black, moves downward
        if (!isWhite) {
            offset = -1;
        }

        // https://en.wikipedia.org/wiki/Pawn_(chess)
        Position[] possibleMoveCandidates = {
                // forward 1
                new Position(currRow + offset, currCol),
                // forward 2
                new Position(currRow + (offset * 2), currCol),
                // left diagonal
                new Position(currRow + offset, currCol - 1),
                // right diagonal
                new Position(currRow + offset, currCol + 1),
        };

        // TODO: work on promotions
        for (int i = 0; i < possibleMoveCandidates.length; i++) {

            Position position = possibleMoveCandidates[i];

            if (position.isWithinBorder() && isNotAlly(board, position, isWhite)) {
                // normal move
                if (i == 0) {
                    possibleMoves.add(position);
                    // pawn jump
                } else if (i == 1 && piece.isFirstMove()) {
                    possibleMoves.add(position);
                } else {
                    // attack
                    if (isEnemy(board, position, isWhite)) {
                        possibleMoves.add(position);
                    }
                }
            }

        }
        return possibleMoves;
    }

    public static Set<Position> findQueenMoves(Piece[][] board, Piece piece) {
        // https://en.wikipedia.org/wiki/Queen_(chess)
        Set<Position> possibleMoves = new HashSet<>();

        possibleMoves.addAll(findBishopMoves(board, piece));
        possibleMoves.addAll(findRookMoves(board, piece));

        return possibleMoves;
    }

    public static Set<Position> findKingMoves(Piece[][] board, Piece piece) {

        int currRow = piece.getPosition().getRow();
        int currCol = piece.getPosition().getCol();
        Set<Position> possibleMoves = new HashSet<>();
        // https://en.wikipedia.org/wiki/King_(chess)
        Position[] possibleMoveCandidates = {
                // up
                new Position(currRow + 1, currCol),
                // upper right
                new Position(currRow + 1, currCol + 1),
                // right
                new Position(currRow, currCol + 1),
                // lower right
                new Position(currRow - 1, currCol + 1),
                // down
                new Position(currRow - 1, currCol),
                // lower left
                new Position(currRow - 1, currCol - 1),
                // left
                new Position(currRow, currCol - 1),
                // upper left
                new Position(currRow + 1, currCol - 1),
        };

        // TODO: work on en passant
        for (Position position : possibleMoveCandidates) {
            if (position.isWithinBorder() && isNotAlly(board, position, piece.isWhite())) {
                possibleMoves.add(position);
            }
        }

        return possibleMoves;
    }

    // check if that position is not an ally
    public static boolean isNotAlly(Piece[][] board, Position position, boolean isWhite) {
        int row = position.getRow();
        int col = position.getCol();

        return board[row][col].isWhite() != isWhite;
    }

    // check if that position is an enemy
    public static boolean isEnemy(Piece[][] board, Position position, boolean isWhite) {
        int row = position.getRow();
        int col = position.getCol();

        return board[row][col].isWhite() == isWhite;
    }

}
