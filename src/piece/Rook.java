package piece;

public class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(PieceValue.ROOK_VALUE, isWhite);
    }

    @Override
    public void move() {
        System.out.println("Horizontally or vertically");
    }

    @Override
    public String toString() {
        return "Rook{value='" + getValue() + "'}";
    }
}
