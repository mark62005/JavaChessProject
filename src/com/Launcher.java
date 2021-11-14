package com;

import com.move.Move;
import com.move.PawnPromotion;
import com.piece.Piece;
import com.piece.Queen;

import java.util.*;
import java.util.stream.Collectors;

import static com.InputValidators.*;

public class Launcher {
    public static void main(String[] args) {
        // initialize the game
        Scanner scanner = new Scanner(System.in);
        Game game = Game.getInstance();

        // test
        game.setPieceAt(new Square(1, 3), null);
        game.setPieceAt(new Square(6, 3), null);

        // start the game
        printStartMessage();
        printBoard(game);

        // when the game is running
        while(game.getTermination() == null) {
            readMove(scanner, game);
        }

        // TODO: use Termination
        // when the game has ended
        System.out.println(getTerminationDescription(game));
        System.exit(0);
    }

    private static void printStartMessage() {
        System.out.println("Game start.");
    }

    private static void printBoard(Game game) {
        printBody(game);
        printFooter();
    }

    private static void printBody(Game game) {
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

    private static void printFooter() {
        for (int i = 97; i < 105; i++) {
            System.out.print((char) i + " ");
        }
        System.out.println();
    }

    private static void readMove(Scanner scanner, Game game) {
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
                resign(game);
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
                } else if (isUciPattern(userInput) || isPromotionPattern(userInput)) {
                    performMove(scanner, game, userInput);
                } else {
                    // TODO: write invalid input message
                    System.out.println("Invalid input format. Type 'help' for help.");
                    // start the function again
                    readMove(scanner, game);
                }
                break;
        }
    }

    // TODO: make sure a pawn is being promoted if it's moving from the 2nd last Rank to last Rank
    private static void performMove(Scanner scanner, Game game, String userInput) {
        try {
            // make a move other than promotion
            if (!isPromotionPattern(userInput)) {
                Move move = Move.parseUCI(userInput);

                if (isNotMyPiece(game, move)) {
                    System.out.println(
                            "Invalid input, please choose a " +
                                    game.getColorToMove().toString() + " piece."
                    );
                    readMove(scanner, game);
                } else {
                    System.out.println("ok");
                    game.makeAMove("move", move);
                }
            } else {
                // make a promotion move
                Move move = PawnPromotion.parsePromotionUCI(game, userInput);

                if (isNotMyPiece(game, move)) {
                    System.out.println(
                            "Invalid input, please choose a " +
                                    game.getColorToMove().toString() + " piece."
                    );
                    readMove(scanner, game);
                } else {
                    System.out.println("ok");
                    game.makeAMove("promotion", move);
                }
            }

            printBoard(game);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            readMove(scanner, game);
        }
    }

    private static boolean isNotMyPiece(Game game, Move move) {
        List<Piece> myPieces = game.getWhitePlayer().getPieces();

        if (game.getColorToMove().equals(Color.BLACK)) {
            myPieces = game.getBlackPlayer().getPieces();
        }

        Piece piece = game.getPieceAt(move.getFrom());
        return !myPieces.contains(piece);
    }

    private static boolean isNotMyPiece(Game game, Square square) {
        List<Piece> pieces = game.getWhitePlayer().getPieces();
        if (game.getColorToMove().equals(Color.BLACK)) {
            pieces = game.getBlackPlayer().getPieces();
        }
        return pieces.stream().noneMatch(p -> p.getSquare().equals(square));
    }

    private static String getUserInput(Scanner scanner, Game game) {
        printQuestion(game);
        String userInput = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

        // make sure it is a valid input format
        if (!isValidInputFormat(userInput)) {
            // TODO: write invalid input message
            System.out.println("Invalid input format. Type 'help' for help.");
            return getUserInput(scanner, game);
        }
        return userInput;
    }

    private static void printQuestion(Game game) {
        System.out.printf(
                "\n%s to move",
                capitalize(game.getColorToMove().toString())
        );
        System.out.print("\nEnter UCI (type 'help' for help): ");
    }

    // capitalize a string
    private static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static void printHelp() {
        System.out.println("* type 'help' for help");
        System.out.println("* type 'board' to see the board again");
        System.out.println("* type 'resign' to resign");
        System.out.println("* type 'moves' to lists all possible moves");
        System.out.println("* type a square (e.g. b1, e2) to list possible moves for that square");
        System.out.println("* type UCI (e.g. b1c3, e7e8q) to make a move");
    }

    private static void printMovesForSquare(Scanner scanner, Game game, String userInput) {
        try {
            Square square = Square.parse(userInput);

            if (isNotMyPiece(game, square)) {
                throw new IllegalArgumentException(
                        "Invalid input. Please choose a " + game.getColorToMove().toString() + " piece."
                );
            }

            Set<Move> possibleMoves = game.getPossibleMovesForPieceAt(square);
            if (possibleMoves.isEmpty()) {
                System.out.printf("No possible move for %s.\n", userInput);
            } else {
                Color colorToMove = game.getColorToMove();
                printPossibleMoveDestinations(possibleMoves, square, colorToMove);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            readMove(scanner, game);
        }
    }

    private static void printPossibleMoveDestinations(Set<Move> possibleMoves, Square from, Color colorToMove) {
        Set<Square> possibleMoveDestinations =
                possibleMoves.stream()
                            .map(Move::getTo)
                            // sort the moves by the rank of the destination square
                            .sorted(Comparator.comparingInt(Square::getRank))
                            .collect(Collectors.toCollection(LinkedHashSet::new));

        if (colorToMove.equals(Color.BLACK)) {
            possibleMoveDestinations =
                    possibleMoves.stream()
                                .map(Move::getTo)
                                // sort the moves by the rank of the destination square
                                .sorted(Comparator.comparingInt(Square::getRank).reversed())
                                .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        System.out.printf("Possible moves for %s:\n", from);
        System.out.println(possibleMoveDestinations);
    }

    private static void printAllPossibleMoves(Game game) {
        Player currPlayer = game.getWhitePlayer();
        if (game.getColorToMove().equals(Color.BLACK)) {
            currPlayer = game.getBlackPlayer();
        }

        Map<Square, Set<Move>> allPossibleMoves = game.getAllPossibleMoves(currPlayer);
        // TODO: sort the map keys
        for (Map.Entry<Square, Set<Move>> entry : allPossibleMoves.entrySet()) {
            Square square = entry.getKey();
            printPossibleMoveDestinations(entry.getValue(), square, game.getColorToMove());
        }
    }

    private static void resign(Game game) {
        game.resign();
        System.out.println(getTerminationDescription(game));
        System.exit(0);
    }

    private static String getTerminationDescription(Game game) {
        return game.getTermination().getScore();
    }
}
