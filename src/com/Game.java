package com;

import com.move.Move;
import com.piece.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.piece.PieceValue.*;

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
        Piece piece = getPieceAt(square);
        if (piece == null) {
            throw new IllegalArgumentException("Invalid Input. This square is empty.");
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
        putPossibleMovesToMap(allPossibleMoves, pieces);

        if (allPossibleMoves.isEmpty()) {
            throw new IllegalArgumentException("Invalid input, you have no possible moves.");
        }
        return allPossibleMoves;
    }

    public Map<Square, Set<Move>> getAllPossibleMoves(Player player) {
        List<Piece> pieces = player.getPieces();

        Map<Square, Set<Move>> allPossibleMoves = new HashMap<>();
        putPossibleMovesToMap(allPossibleMoves, pieces);
        return allPossibleMoves;
    }

    private void putPossibleMovesToMap(Map<Square, Set<Move>> map, List<Piece> pieces) {
        for (Piece piece : pieces) {
            // a King cannot directly check the opponent's King
            if (piece.getValue() != KING_VALUE) {
                Set<Move> possibleMoves = piece.findPossibleMoves(this);
                // only add it when there are possible moves for that piece
                if (!possibleMoves.isEmpty()) {
                    map.put(piece.getSquare(), possibleMoves);
                }
            }
        }
    }

    public void makeAMove(String condition, Move move) {
        Square from = move.getFrom();
        Square to = move.getTo();
        Piece myPiece = getPieceAt(from);

        Set<Move> possibleMoves = myPiece.findPossibleMoves(this);
        // check if it's a valid move
        if (isValidMove(possibleMoves, to)) {
            if (!condition.equals("promotion")) {
                // check if it is a non-normal move,
                // if true, set "move" to be that non-normal move
                for (Move m : possibleMoves) {
                    if (m.getTo().equals(move.getTo())) {
                        System.out.println(m);
                        move = m;
                    }
                }
            } else {
                // check if that piece is a pawn
                if (myPiece.getValue() != PieceValue.PAWN_VALUE) {
                    throw new IllegalArgumentException("Invalid input. Only a pawn can be promoted.");
                }
            }
            // perform a move
            move.makeAMove(this, myPiece);

            List<Piece> opponentPieces = blackPlayer.getPieces();
            if (colorToMove.equals(Color.BLACK)) {
                opponentPieces = whitePlayer.getPieces();
            }
            for (Piece piece : opponentPieces) {
                if (piece.getValue() == PAWN_VALUE) {
                    Pawn pawn = (Pawn) piece;
                    pawn.setCanBeCapturedByEnPassant(false);
                    System.out.println(pawn);
                }
            }

            if (isCheckMate()) {
                System.out.println("Check mate.");
            }
            // check if anyone wins after each move
            checkWin();
            // switch the player after each move
            switchPlayer();
        }
    }

    private boolean isValidMove(Set<Move> possibleMoves, Square to) {
        // throw an exception if there's no possible move for that square
        if (possibleMoves.isEmpty()) {
            throw new IllegalArgumentException("Invalid Input. There's no possible move for this piece.");
        }
        // throw an exception if the new square is not a valid move of myPiece
        if (possibleMoves.stream().noneMatch(m -> m.getTo().equals(to))) {
            throw new IllegalArgumentException("Invalid Input. This square is not a valid move.");
        }
        return true;
    }

    private void switchPlayer() {
        if (colorToMove.equals(Color.WHITE)) {
            setColorToMove(Color.BLACK);
        } else {
            setColorToMove(Color.WHITE);
        }
    }

    // check if all of opponent's possible moves contains opponent's King Position
    public boolean isNotInCheck(boolean isWhite, Square currSquare) {
        Player opponent;
        if (isWhite) {
            opponent = blackPlayer;
        } else {
            opponent = whitePlayer;
        }

        Map<Square, Set<Move>> opponentPossibleMoves = getAllPossibleMoves(opponent);
        // check if it contains King's position
        return !getFlattenPossibleDestinations(opponentPossibleMoves).contains(currSquare);
    }

    public boolean isCheckMate() {
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
        opponentPossibleKingTos.add(opponentKingPos);

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

    public boolean isRookAndKingAtInitialPosition() {
        List<Piece> myPieces = whitePlayer.getPieces();
        if (colorToMove.equals(Color.BLACK)) {
            myPieces = blackPlayer.getPieces();
        }

        for (Piece piece : myPieces) {
            if (isRook(piece)) {
                Rook rook = (Rook) piece;
                if (!rook.canCastling()) {
                    return false;
                }
            } else if (isKing(piece)) {
                King king = (King) piece;
                if (!king.canCastling()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isRook(Piece piece) {
        return piece.getValue() == ROOK_VALUE;
    }

    private boolean isKing(Piece piece) {
        return piece.getValue() == KING_VALUE;
    }
    
}
