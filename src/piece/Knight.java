package piece;

public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(PieceValue.KNIGHT_VALUE, isWhite);
    }

    @Override
    public void move() {
        System.out.println("Like an L");
    }

    @Override
    public String toString() {
        return "Knight{value='" + getValue() + "'}";
    }
}
