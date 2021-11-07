package piece;

public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(PieceValue.QUEEN_VALUE, isWhite);
    }

    @Override
    public void move() {
        System.out.println("Like bishop and rook");
    }

    @Override
    public String toString() {
        return "Queen{value='" + getValue() + "'}";
    }
}
