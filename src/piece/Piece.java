package piece;

import position.Position;

public abstract class Piece {
    private int value;
    protected boolean isWhite;
    protected Position position;

    public Piece(int value, boolean isWhite) {
        this.value = value;
        this.isWhite = isWhite;
    }

    public Piece(int value, boolean isWhite, Position position) {
        this.value = value;
        this.isWhite = isWhite;
        this.position = position;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void move();

}
