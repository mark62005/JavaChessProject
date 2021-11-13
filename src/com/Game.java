package com;

import com.move.Move;
import com.piece.*;

import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private static Game instance;
    private Piece[][] board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Color colorToMove;

    private Game() {
        this.whitePlayer = new Player(Color.WHITE);
        this.blackPlayer = new Player(Color.BLACK);

        // initialize the board
        this.board = new Piece[8][8];
        initializeBoard();

        this.colorToMove = Color.WHITE;
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPieceAt(Square square) {
        int rank = square.getRank();
        int file = square.getFile();

        if (!square.isWithinBorder()) {
            throw new IndexOutOfBoundsException("Invalid input, valid range is from a to h or 1 to 8.");
        }
        return board[rank][file];
    }
    
    public void setPieceAt(Square square, Piece piece) {
        int rank = square.getRank();
        int file = square.getFile();
        board[rank][file] = piece;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Color getColorToMove() {
        return colorToMove;
    }

    public void setColorToMove(Color colorToMove) {
        this.colorToMove = colorToMove;
    }

    public void initializeBoard() {
        initializePieces(Color.WHITE);
        initializePieces(Color.BLACK);
    }

    private void initializePieces(Color color) {
        int r1 = 0;
        int r2 = 1;
        boolean isWhite = true;
        
        if (color.equals(Color.BLACK)) {
            r1 = 7;
            r2 = 6;
            isWhite = false;
        }

        for (int i = 0; i < board.length; i++) {
            // 1st rank
            Square squareAtR1 = new Square(r1, i);
            switch (i) {
                case 0:
                case 7:
                    initializeAPiece(new Rook(isWhite), squareAtR1);
                    break;
                case 1:
                case 6:
                    initializeAPiece(new Knight(isWhite), squareAtR1);
                    break;
                case 2:
                case 5:
                    initializeAPiece(new Bishop(isWhite), squareAtR1);
                    break;
                case 3:
                    initializeAPiece(new Queen(isWhite), squareAtR1);
                    break;
                case 4:
                    initializeAPiece(new King(isWhite), squareAtR1);
                    break;
                default:
                    break;
            }
            // 2nd rank
            Square SquareAtR2 = new Square(r2, i);
            initializeAPiece(new Pawn(isWhite), SquareAtR2);
        }
    }

    private void initializeAPiece(Piece piece, Square square) {
        // set the square of that piece
        piece.setSquare(square);
        // if it's on the white side
        if (piece.isWhite()) {
            // add to whitePieces
            whitePlayer.addAPiece(piece);
        } else {
            // add to blackPieces
            blackPlayer.addAPiece(piece);
        }
        // set the square of the board to be that piece
        setPieceAt(square, piece);
    }

    public Set<Move> getPossibleMovesForPieceAt(Square square) {
        List<Piece> pieces;
        if (colorToMove.equals(Color.WHITE)) {
            pieces = whitePlayer.getPieces();
        } else {
            pieces = blackPlayer.getPieces();
        }

        Piece piece = getPieceAt(square);
        if (piece == null) {
            throw new IllegalArgumentException("Invalid Input. This square is empty.");
        }
        if (!pieces.contains(piece)) {
            throw new IllegalArgumentException(
                    "Invalid input, please choose a " + colorToMove.toString() + " piece."
            );
        }

        return piece.findPossibleMoves(this);
    }

    public Map<Square, Set<Move>> getAllPossibleMoves() {
        List<Piece> pieces;
        if (colorToMove.equals(Color.WHITE)) {
            pieces = whitePlayer.getPieces();
        } else {
            pieces = blackPlayer.getPieces();
        }

        Map<Square, Set<Move>> allPossibleMoves = new HashMap<>();
        for (Piece piece : pieces) {
            Set<Move> possibleMoves = piece.findPossibleMoves(this);
            // only add it when there are possible moves for that piece
            if (!possibleMoves.isEmpty()) {
                allPossibleMoves.put(piece.getSquare(), possibleMoves);
            }
        }

        if (allPossibleMoves.isEmpty()) {
            throw new IllegalArgumentException("Invalid input, you have no possible moves.");
        }
        return allPossibleMoves;
    }

    public void makeAMove(Move move) {
        Square from = move.getFrom();
        Square to = move.getTo();
        Piece myPiece = getPieceAt(from);

        Set<Move> possibleMoves = myPiece.findPossibleMoves(this);
        // throw an exception if there's no possible move for that square
        if (possibleMoves.isEmpty()) {
            throw new IllegalArgumentException("Invalid Input. There's no possible move for this piece.");
        }
        // throw an exception if the new square is not a valid move of myPiece
        if (possibleMoves.stream().noneMatch(m -> m.getTo().equals(to))) {
            throw new IllegalArgumentException("Invalid Input. This square is not a valid move.");
        }

        // TODO: work on conditions
        // check if it is a non-normal move,
        // if true, set "move" to be that non-normal move
        for (Move m : possibleMoves) {
            if (m.getTo().equals(move.getTo())) {
                System.out.println(m);
                move = m;
            }
        }
        // perform a move
        move.makeAMove(this, myPiece);

        // check if anyone wins after each move
        checkWin();
        // switch the player after each move
        switchPlayer();
    }

    private boolean isValidMove(Set<Move> possibleMoves, Square to) {


        return true;
    }

    private boolean canPromote(Piece pawn, int rank) {
        // if it is a white piece
        if (pawn.isWhite()) {
            return rank == 7;
        } else {
            // if it is a black piece
            return rank == 0;
        }
    }

    private void switchPlayer() {
        if (colorToMove.equals(Color.WHITE)) {
            setColorToMove(Color.BLACK);
        } else {
            setColorToMove(Color.WHITE);
        }
    }

    // check if all of my possible moves contains opponent's King Square
    public boolean isInCheck() {
        Player opponent = blackPlayer;
        if (colorToMove.equals(Color.BLACK)) {
            opponent = whitePlayer;
        }
        final Square opponentKingPos = opponent.getKingPos();

        Map<Square, Set<Move>> myPossibleMoves = getAllPossibleMoves();
        // flatten the "all my possible moves" map, then
        // check if it contains all possible King moves of opponent
        return getFlattenPossibleDestinations(myPossibleMoves).contains(opponentKingPos);
    }

    public boolean isCheckMate() {
        if (!isInCheck()) {
            return false;
        }

        Square opponentKingPos = blackPlayer.getKingPos();
        if (colorToMove.equals(Color.BLACK)) {
            opponentKingPos = whitePlayer.getKingPos();
        }
        // find possible move destinations for opponent's King
        Set<Move> opponentPossibleKingMoves = getPossibleMovesForPieceAt(opponentKingPos);
        Set<Square> opponentPossibleKingTos =
                opponentPossibleKingMoves.stream()
                        .map(Move::getTo)
                        .collect(Collectors.toSet());

        // find all my possible move destinations
        Map<Square, Set<Move>> myPossibleMoves = getAllPossibleMoves();

        // flatten the "all my possible moves" map, then
        // check if it contains all possible King moves of opponent
        return getFlattenPossibleDestinations(myPossibleMoves).containsAll(opponentPossibleKingTos);
    }

    // flatten the "all my possible moves" map, and return a set of possible destinations
    private Set<Square> getFlattenPossibleDestinations(Map<Square, Set<Move>> possibleMovesMap) {
        Set<Square> flattenedMyPossibleTos = new HashSet<>();
        for (Set<Move> moves : possibleMovesMap.values()) {
            flattenedMyPossibleTos.addAll(
                    moves.stream()
                            .map(Move::getTo)
                            .collect(Collectors.toSet())
            );
        }
        return flattenedMyPossibleTos;
    }

    // TODO: use Termination
    // if opponent's King is captured, return my side
    public Color checkWin() {
        if (whitePlayer.isKingCaptured()) {
            return Color.BLACK;
        }

        if (blackPlayer.isKingCaptured()) {
            return Color.WHITE;
        }

        // TODO: work on draw
        return null;
    }
    
}
