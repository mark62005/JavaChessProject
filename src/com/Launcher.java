package com;

import com.move.Move;
import com.piece.Piece;

import java.util.*;
import java.util.stream.Collectors;

import static com.InputValidators.*;

public class Launcher {
    public static void main(String[] args) {
        // initialize the game
        Scanner scanner = new Scanner(System.in);
        Game game = Game.getInstance();

        game.setPieceAt(new Square(1, 3), null);
        game.setPieceAt(new Square(6, 3), null);

        // start the game
        printStartMessage();
        printBoard(game);

        // when the game is running
        while(!gameHasEnded(game)) {
            readMove(scanner, game);
        }

        // TODO: use Termination
        // when the game has ended
        Color winner = game.checkWin();
        if (winner.equals(Color.BLACK)) {
            System.out.println("Black won.");
        } else {
            System.out.println("White won.");
        }
    }

    public static void printStartMessage() {
        System.out.println("Game start.");
    }

    public static void printBoard(Game game) {
        printBody(game);
        printFooter();
    }

    public static void printBody(Game game) {
        Piece[][] board = game.getBoard();

        for (int i = board.length; i > 0; i--) {
            for (int j = 0; j < board.length; j++) {
                Piece piece = board[i - 1][j];
                if (piece == null) {
                    System.out.print("• ");
                } else {
                    System.out.print(piece.getSymbol() + " ");
                }
            }
            System.out.print(" " + i);
            System.out.println();
        }

        System.out.println();
    }

    public static void printFooter() {
        for (int i = 97; i < 105; i++) {
            System.out.print((char) i + " ");
        }
        System.out.println();
    }

    public static void readMove(Scanner scanner, Game game) {
        String userInput = getUserInput(scanner, game);
        switch (userInput) {
            // * type 'help' for help
            case "help":
                printHelp();
                break;
            // * type 'board' to see the board again
            case "board":
                printBoard(game);
                break;
            // * type 'resign' to resign
            case "resign":
                resign();
                break;
            // * type 'moves' to lists all possible moves
            case "moves":
                printAllPossibleMoves(game);
                break;
            // else
            default:
                // * type a square (e.g. b1, e2) to list possible moves for that square
                if (isSquarePattern(userInput)) {
                    printMovesForSquare(scanner, game, userInput);
                    readMove(scanner, game);
                    // * type UCI (e.g. b13, e7e8q) to make a move
                } else if (isUciPattern(userInput)) {
                    performMove(scanner, game, userInput);
                } else {
                    // TODO: write invalid input message
                    System.out.println("Invalid input format.");
                    // start the function again
                    readMove(scanner, game);
                }
                break;
        }
    }

    // TODO: think about more errors
    public static void performMove(Scanner scanner, Game game, String userInput) {
        try {
            Move move = Move.parseUCI(userInput);
            System.out.println("ok");
            game.makeAMove(move);
            printBoard(game);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            readMove(scanner, game);
        }
    }

    public static String getUserInput(Scanner scanner, Game game) {
        printQuestion(game);
        String userInput = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

        // make sure it is a valid input format
        if (!isValidInputFormat(userInput)) {
            // TODO: write invalid input message
            System.out.println("Invalid input format.");
            return getUserInput(scanner, game);
        }
        return userInput;
    }

    public static void printQuestion(Game game) {
        System.out.printf(
                "\n%s to move",
                capitalize(game.getColorToMove().toString())
        );
        System.out.print("\nEnter UCI (type 'help' for help): ");
    }

    // capitalize a string
    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void printHelp() {
        System.out.println("* type 'help' for help");
        System.out.println("* type 'board' to see the board again");
        System.out.println("* type 'resign' to resign");
        System.out.println("* type 'moves' to lists all possible moves");
        System.out.println("* type a square (e.g. b1, e2) to list possible moves for that square");
        System.out.println("* type UCI (e.g. b1c3, e7e8q) to make a move");
    }

    // TODO: think about more errors
    public static void printMovesForSquare(Scanner scanner, Game game, String userInput) {
        try {
            Square square = Square.parse(userInput);
            Set<Square> possibleMoveDestinations = getPossibleMoveDestinations(game, square);

            if (possibleMoveDestinations.isEmpty()) {
                System.out.printf("No possible move for %s.\n", userInput);
            } else {
                System.out.printf("Possible moves for %s:\n", userInput);
                System.out.println(possibleMoveDestinations);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            readMove(scanner, game);
        }
    }

    public static Set<Square> getPossibleMoveDestinations(Game game, Square square) {
        Set<Move> possibleMoves = game.getPossibleMovesForPieceAt(square);

        if (game.getColorToMove().equals(Color.WHITE)) {
            return possibleMoves.stream()
                    .map(Move::getTo)
                    // sort the moves by the rank of the destination square
                    .sorted(Comparator.comparingInt(Square::getRank))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return possibleMoves.stream()
                .map(Move::getTo)
                // sort the moves by the rank of the destination square
                .sorted(Comparator.comparingInt(Square::getRank).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static void printAllPossibleMoves(Game game) {
        Map<Square, Set<Move>> allPossibleMoves = game.getAllPossibleMoves();

        // TODO: print all possible moves
        System.out.println("All possible moves...");
    }

    // TODO: work on draw
    public static boolean gameHasEnded(Game game) {
        Color winner = game.checkWin();

        if (winner == null) {
            return false;
        }
        return winner.equals(Color.BLACK) || winner.equals(Color.WHITE);
    }

    // TODO: use Termination
    public static void resign() {
        System.out.println("Resigned.");
        System.exit(0);
    }
}
