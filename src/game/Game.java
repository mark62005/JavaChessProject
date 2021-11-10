package game;

import piece.*;
import position.Position;

import java.util.*;

public class Game {

    private PieceService pieceService;
    private Piece[][] board;
    private Player whitePlayer;
    private Player blackPlayer;

    public Game() {
        this.whitePlayer = new Player(Side.WHITE);
        this.blackPlayer = new Player(Side.BLACK);
        // initialize the board
        this.board = new Piece[8][8];
        initializeBoard();
        this.pieceService = PieceService.getInstance();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoardAtPosition(Piece piece, Position position) {
        int row = position.getRow();
        int col = position.getCol();
        this.board[row][col] = piece;
    }

    public void initializeBoard() {
        initializePieces(Side.WHITE);
        initializePieces(Side.BLACK);
    }

    private void initializePieces(Side side) {
        int r1 = 0;
        int r2 = 1;
        boolean isWhite = true;

        if (side.equals(Side.BLACK)) {
            r1 = 7;
            r2 = 6;
            isWhite = false;
        }

        for (int i = 0; i < board.length; i++) {
            // 1st row
            Position row1 = new Position(r1, i);
            switch (i) {
                case 0, 7 -> initializeAPiece(new Rook(isWhite), row1);
                case 1, 6 -> initializeAPiece(new Knight(isWhite), row1);
                case 2, 5 -> initializeAPiece(new Bishop(isWhite), row1);
                case 3 -> initializeAPiece(new King(isWhite), row1);
                case 4 -> initializeAPiece(new Queen(isWhite), row1);
                default -> {}
            }

            // 2nd row
            Position row2 = new Position(r2, i);
            initializeAPiece(new Pawn(isWhite), row2);
        }
    }

    private void initializeAPiece(Piece piece, Position position) {
        piece.setPosition(position);
        if (piece.isWhite()) {
            whitePlayer.addAPiece(piece);
        } else {
            blackPlayer.addAPiece(piece);
        }
        setBoardAtPosition(piece, position);
    }

    private Piece getPieceByPosition(Position position) {
        int row = position.getRow();
        int col = position.getCol();

        if (board[row][col] == null) {
            throw new IllegalArgumentException("Invalid Input. This position is empty.");
        }
        return board[row][col];
    }

    public Set<Position> getPossibleMovesOfAPiece(Position position) {

        Piece piece = getPieceByPosition(position);

        switch (piece.getValue()) {
            case PieceValue.PAWN_VALUE -> {
                return PossibleMovesCalculator.findPawnMoves(board, piece);
            }
            case PieceValue.KNIGHT_VALUE -> {
                return PossibleMovesCalculator.findKnightMoves(board, piece);
            }
            case PieceValue.BISHOP_VALUE -> {
                return PossibleMovesCalculator.findBishopMoves(board, piece);
            }
            case PieceValue.ROOK_VALUE -> {
                return PossibleMovesCalculator.findRookMoves(board, piece);
            }
            case PieceValue.QUEEN_VALUE -> {
                return PossibleMovesCalculator.findQueenMoves(board, piece);
            }
            case PieceValue.KING_VALUE -> {
                return PossibleMovesCalculator.findKingMoves(board, piece);
            }
            default -> throw new IllegalArgumentException("Invalid Input. This position is empty.");
        }

    }

    public Map<Position, Set<Position>> getAllPossibleMoves(boolean isWhite) {
        Map<Position, Set<Position>> allPossibleMoves = new HashMap<>();

        if (isWhite) {
            for (Piece piece : whitePlayer.getPieces()) {
                Position position = piece.getPosition();
                allPossibleMoves.put(position, getPossibleMovesOfAPiece(position));
            }
        } else {
            for (Piece piece : blackPlayer.getPieces()) {
                Position position = piece.getPosition();
                allPossibleMoves.put(position, getPossibleMovesOfAPiece(position));
            }
        }

        return allPossibleMoves;
    }

    public void makeAMove(Position currPos, Position newPos) {
        Piece myPiece = getPieceByPosition(currPos);
        Piece targetPiece = getPieceByPosition(newPos);
        Set<Position> possibleMoves = getPossibleMovesOfAPiece(currPos);

        // throw an exception if there's no possible move for that position
        if (possibleMoves == null) {
            throw new IllegalArgumentException("Invalid Input. There's no possible move for this piece.");
        }
        // throw an exception if the new position is not a valid move of myPiece
        if (!possibleMoves.contains(newPos)) {
            throw new IllegalArgumentException("Invalid Input. This position is not a valid move.");
        }

        // if a player moves a King, change the King's Position of that player
        if (myPiece.getValue() == 1000) {
            if (myPiece.isWhite()) {
                whitePlayer.setKingPos(newPos);
            } else {
                blackPlayer.setKingPos(newPos);
            }
        }
        pieceService.makeAMove(myPiece, newPos);

        // change the new position to be target piece
        setBoardAtPosition(myPiece, newPos);

        // if there's an enemy on the new position, capture it
        if (PossibleMovesCalculator.isEnemy(board, newPos, myPiece.isWhite())) {
            captureEnemy(targetPiece, myPiece.isWhite());
        }

        // change the current position to be null
        setBoardAtPosition(null, currPos);
    }

    // remove opponent's piece
    private void captureEnemy(Piece targetPiece, boolean isWhite) {
        List<Piece> pieces;
        if (isWhite) {
            pieces = blackPlayer.getPieces();
        } else {
            pieces = whitePlayer.getPieces();
        }

        for (Piece piece : pieces) {
            if (piece.equals(targetPiece)) {
                // if that enemy is King
                if (targetPiece.getClass().equals(King.class)) {
                    captureKing(targetPiece.isWhite());
                }
                // remove that piece from the piece list
                pieces.remove(piece);
            }
        }
    }

    private void captureKing(boolean isWhite) {
        if (isWhite) {
            whitePlayer.setKingCaptured(true);
        } else {
            blackPlayer.setKingCaptured(true);
        }
    }

    // check if all of my possible moves contains opponent's King Position
    public boolean isCheck(boolean isWhite) {
        Player opponent;
        if (isWhite) {
            opponent = blackPlayer;
        } else {
            opponent = whitePlayer;
        }

        Map<Position, Set<Position>> myPossibleMoves = getAllPossibleMoves(isWhite);
        for (Set<Position> positions : myPossibleMoves.values()) {
            for (Position position : positions) {
                if (position.equals(opponent.getKingPos())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(boolean isWhite) {

        if (!isCheck(isWhite)) {
            return false;
        }

        // find possible moves for opponent's King
        Player opponent;
        if (isWhite) {
            opponent = blackPlayer;
        } else {
            opponent = whitePlayer;
        }
        Set<Position> opponentPossibleKingMoves = getPossibleMovesOfAPiece(opponent.getKingPos());

        // find all my possible moves
        Map<Position, Set<Position>> myPossibleMoves = getAllPossibleMoves(isWhite);
        // flatten the "all my possible moves" map
        Set<Position> flattenedMyPossibleMoves = new HashSet<>();
        for (Set<Position> positions : myPossibleMoves.values()) {
            flattenedMyPossibleMoves.addAll(positions);
        }

        // check if the flattened possible moves map contains
        // all possible King moves of opponent
        return flattenedMyPossibleMoves.containsAll(opponentPossibleKingMoves);
    }

    // if opponent's King is captured, return my side
    // else, return null
    public Side checkWin() {
        if (whitePlayer.isKingCaptured()) {
            return Side.BLACK;
        }

        if (blackPlayer.isKingCaptured()) {
            return Side.WHITE;
        }
        return null;
    }

}
