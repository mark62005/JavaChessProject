package game;

import piece.*;
import position.Position;

import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private Piece[][] board;
    private Player whitePlayer;
    private Player blackPlayer;
    private int turns = 0;

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
                case 0 : initializeAPiece(new Rook(isWhite), row1);
                    break;
                case 7 : initializeAPiece(new Rook(isWhite), row1);
                    break;
                case 1 : initializeAPiece(new Knight(isWhite), row1);
                    break;
                case 6 : initializeAPiece(new Knight(isWhite), row1);
                    break;
                case 2 : initializeAPiece(new Bishop(isWhite), row1);
                    break;
                case 5 : initializeAPiece(new Bishop(isWhite), row1);
                    break;
                case 3 : initializeAPiece(new King(isWhite), row1);
                    break;
                case 4 : initializeAPiece(new Queen(isWhite), row1);
                    break;
                default : {}
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

    public void scanBoardAndInput(){
        int row = 1;
        int col = 1;
        for (Piece[] r : board ) {
            for ( Piece c : r ) {
                if (c == null){
                    System.out.print(".  ");
                } else {
                    System.out.print("" + piecesFont(c.getValue(),c.isWhite()) + " ");
                }
            }
            System.out.print("  " + row);
            System.out.println();
            col = 1;
            row++;
        }
        for ( char ch = 'a'; ch != 'i'; ch++ ) {
            System.out.print(ch + "  ");
        }
        System.out.println();
        getUserInput();
    }

    public String piecesFont(int value, boolean isWhite){
        String display = null;
        if (isWhite){
            switch (value) {
                case 1 : display = PieceValue.PIECES_WHITE[0];
                    break;
                case 2 : display = PieceValue.PIECES_WHITE[2];
                    break;
                case 3 : display = PieceValue.PIECES_WHITE[3];
                    break;
                case 5 : display = PieceValue.PIECES_WHITE[1];
                    break;
                case 9 : display = PieceValue.PIECES_WHITE[5];
                    break;
                case 1000 : display = PieceValue.PIECES_WHITE[4];
                    break;
                default : {}
            }
        } else {
            switch (value) {
                case 1 : display = PieceValue.PIECES_BLACK[0];
                    break;
                case 2 : display = PieceValue.PIECES_BLACK[2];
                    break;
                case 5 : display = PieceValue.PIECES_BLACK[1];
                    break;
                case 3 : display = PieceValue.PIECES_BLACK[3];
                    break;
                case 9 : display = PieceValue.PIECES_BLACK[5];
                    break;
                case 1000 : display = PieceValue.PIECES_BLACK[4];
                    break;
                default : {}
            }
        }
        return display;
    }

    public void getUserInput(){
        System.out.println(whitePlayer.getPieces().toString());
        System.out.println();
        Scanner in = new Scanner(System.in);
        if (turns%2 == 0){
            while (true) {
                System.out.println("White to move");
                System.out.print("Enter UCI (type 'help' for help): ");
                String command = in.next();
                if(getCommand(command)){
                    break;
                }
            }
        } else if (turns%2 == 1){
            while (true) {
                System.out.println("Black to move");
                System.out.print("Enter UCI (type 'help' for help): ");
                String command = in.next();
                if(getCommand(command)){
                    break;
                }
            }
        }
    }

    public boolean getCommand(String command){
        switch (command) {
            case "help":
                System.out.println("* type 'help' for help");
                System.out.println("* type 'board' to see the board again");
                System.out.println("* type 'resign' to resign");
                System.out.println("* type 'moves' to list all possible moves");
                System.out.println("* type a square (e.g. b1, e2) to possible moves for the square");
                System.out.println("* type UCI (e.g. b1c3, e7e8q) to make a move");
                System.out.println();
                return false;
            case "board":
                System.out.println();
                scanBoardAndInput();
                return true;
            case "break":
                turns++;
                scanBoardAndInput();
                return true;
            case "resign":
                if (turns % 2 == 0) {
                    System.out.println("Game over = 0-1 - Black won by resignation");
                } else {
                    System.out.println("Game over = 1-0 - White won by resignation");
                }
                return true;
        }
        return false;
    }
}

