package piece;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(PieceValue.BISHOP_VALUE, isWhite);
    }

    @Override
    public void move() {
        System.out.println("Diagonally");
    }

    @Override
    public String toString() {
        return "Bishop{value='" + getValue() + "'}";
    }
}
