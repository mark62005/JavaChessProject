package piece;

import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        ArrayList<Piece> pieces = new ArrayList<>();
        pieces.add(new Pawn(true));
        pieces.add(new Bishop(true));
        pieces.add(new Rook(true));
        pieces.add(new King(true));
        pieces.add(new Queen(true));
        pieces.add(new Knight(true));

        for (Piece piece : pieces) {
            piece.move();
        }

        Pawn p1 = new Pawn(true);
        p1.promote(new Queen(true));
        Pawn p2 = new Pawn(true);
        Pawn p3 = new Pawn(false);
        Pawn p4 = new Pawn(false);
        p4.promote(new Queen(false));
        Pawn p5 = new Pawn(true);
        Knight k = new Knight(true);
        p5.promote(k);

        System.out.println(p1.equals(p2));
        System.out.println(p1.equals(p4));
        System.out.println(p1.equals(p5));
        System.out.println(p2.equals(p3));
        System.out.println(p4.equals(p5));
    }
}
