package com.move;

import com.Color;
import com.Game;
import com.InputValidators;
import com.Square;
import com.piece.*;

import java.util.List;

// TODO
public class PawnPromotion extends Move implements Attack {
    private Piece promoteTo;
    private Piece enemy;

    public PawnPromotion(Square from, Square to, Piece promoteTo) {
        super(from, to);
        setPromoteTo(promoteTo);
    }

    public PawnPromotion(Move move, Piece promoteTo) {
        super(move.getFrom(), move.getTo());
        setPromoteTo(promoteTo);
    }

    public void setPromoteTo(Piece promoteTo) {
        if (promoteTo.getValue() == PieceValue.KING_VALUE ||
                promoteTo.getValue() == PieceValue.PAWN_VALUE) {
            throw new IllegalArgumentException("Invalid input. A pawn cannot be promoted to a King or a Pawn.");
        }
        this.promoteTo = promoteTo;
    }

    @Override
    public void makeAMove(Game game, Piece myPiece) {
        if (isValidPromotion(myPiece)) {
            promote(game, myPiece);
            // if the move destination is an enemy, capture it
            if (myPiece.isEnemy(game, to)) {
                captureEnemy(game, to);
            }
            updateBoard(game, promoteTo);
            System.out.println("The pawn has been promoted successfully.");
        }
    }

    private boolean isValidPromotion(Piece myPiece) {
        if (myPiece.getValue() != PieceValue.PAWN_VALUE) {
            throw new IllegalArgumentException("Invalid input. Only a pawn can be promoted.");
        }

        if (
                (from.getRank() == 6 && to.getRank() == 7) ||
                (from.getRank() == 1 && to.getRank() == 0)
        ) {
            return true;
        } else {
            throw new IllegalArgumentException(
                    "A pawn can only perform promotion when it is on the 7th Rank (white) or the 2nd Rank (black)."
            );
        }
    }

    private void promote(Game game, Piece currPiece) {
        promoteTo.setSquare(to);
        updatePieces(game, currPiece);
    }

    private void updatePieces(Game game, Piece currPiece) {
        List<Piece> pieces = game.getWhitePlayer().getPieces();
        if (game.getColorToMove().equals(Color.BLACK)) {
            pieces = game.getBlackPlayer().getPieces();
        }

        // remove that pawn from piece list
        pieces.remove(currPiece);
        // add the promoted piece to piece list
        pieces.add(promoteTo);
    }

    @Override
    protected void updateBoard(Game game, Piece myPiece) {
        super.updateBoard(game, myPiece);
    }

    public static PawnPromotion parsePromotionUCI(Game game, String userInput) {
        if (
                userInput.charAt(4) != 'k' &&
                userInput.charAt(4) != 'b' &&
                userInput.charAt(4) != 'r' &&
                userInput.charAt(4) != 'q'
        ) {
            throw new IllegalArgumentException("Invalid input. A pawn cannot be promoted to a King or a Pawn.");
        }

        Move move = Move.parseUCI(userInput.substring(0, 4));
        Piece currPiece = game.getPieceAt(move.getFrom());
        Piece newPiece;
        switch (userInput.charAt(4)) {
            case 'k':
                newPiece = new Knight(currPiece.isWhite());
                break;
            case 'b':
                newPiece = new Bishop(currPiece.isWhite());
                break;
            case 'r':
                newPiece = new Rook(currPiece.isWhite());
                break;
            case 'q':
                newPiece = new Queen(currPiece.isWhite());
                break;
            default:
                throw new IllegalArgumentException("Invalid input. A pawn cannot be promoted to a King or a Pawn.");
        }

        return new PawnPromotion(move, newPiece);
    }

    @Override
    public String toString() {
        return "PawnPromotion{" +
                "from=" + from +
                ", to=" + to +
                ", promoteTo=" + promoteTo +
                '}';
    }
}
