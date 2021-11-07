package game;

import piece.*;
import position.Position;

public class Game {

    private Piece[][] board;
    private Player whitePlayer;
    private Player blackPlayer;

    public Game() {
        this.whitePlayer = new Player("P1", Side.WHITE);
        this.blackPlayer = new Player("P2", Side.BLACK);
        // initialize the board
        this.board = new Piece[8][8];
        initializeBoard();
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
        int r1 = 7;
        int r2 = 6;
        boolean isWhite = true;

        if (side.equals(Side.BLACK)) {
            r1 = 0;
            r2 = 1;
            isWhite = false;
        }

        for (int i = 0; i < 8; i++) {
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

}
