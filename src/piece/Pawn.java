package piece;

import java.util.Objects;

public class Pawn extends Piece {
    private boolean promoted;
    private Piece newPiece;

    public Pawn(boolean isWhite) {
        super(PieceValue.PAWN_VALUE, isWhite);
    }

    public void promote(Piece newPiece) {
        if (!promoted) {
            this.promoted = true;
            this.newPiece = newPiece;
        } else {
            System.out.println("It's already been promoted.");
        }
    }

    @Override
    public void move() {
        System.out.println("Forward 1");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pawn pawn = (Pawn) o;
        return (isWhite == pawn.isWhite && promoted == pawn.promoted
                && (newPiece.getValue() == pawn.newPiece.getValue()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(promoted, newPiece);
    }

    @Override
    public String toString() {
        return "Pawn{value='" + getValue() + "'}";
    }
}
