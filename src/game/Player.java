package game;

import piece.Piece;

import java.util.*;

public class Player {

    private String name;
    private Side side;
    private List<Piece> pieces = new ArrayList<>();

    public Player(String name, Side side) {
        this.name = name;
        this.side = side;
    }

    public String getName() {
        return name;
    }

    public Side getSide() {
        return side;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void addAPiece(Piece piece) {
        pieces.add(piece);
    }

}
