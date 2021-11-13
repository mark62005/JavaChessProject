package com.piece;

import com.Game;
import com.move.AttackMove;
import com.move.Move;
import com.Square;

import java.util.Objects;
import java.util.Set;

public abstract class Piece {
    private final int value;
    protected boolean isWhite;
    protected Square square;
    protected char symbol;
    protected Move prevMove;    // don't delete, use for checking en passant

    public Piece(int value, boolean isWhite) {
        this.value = value;
        this.isWhite = isWhite;
    }

    public Piece(int value, boolean isWhite, Square square) {
        this.value = value;
        this.isWhite = isWhite;
        this.square = square;
    }

    public int getValue() {
        return value;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public char getSymbol() {
        return symbol;
    }

    public abstract void setSymbol(boolean isWhite);

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public Move getPrevMove() {
        return prevMove;
    }

    public void setPrevMove(Move prevMove) {
        this.prevMove = prevMove;
    }

    public abstract void move();

    public abstract Set<Move> findPossibleMoves(Game game);

    protected void addNormalMove(Set<Move> moves, Square from, Square to) {
        Square newTo = new Square(to);
        moves.add(new Move(from, newTo));
    }

    protected void addAttackMove(Set<Move> moves, Square from, Square to, Game game) {
        Piece enemy = game.getPieceAt(to);
        Square newTo = new Square(to);
        moves.add(new AttackMove(from, newTo, enemy));
    }

    protected final boolean isNotAlly (Game game, Square to) {
        Piece piece = game.getPieceAt(to);
        if (piece == null) {
            return true;
        }
        return this.isWhite != piece.isWhite();
    }

    public final boolean isEnemy(Game game, Square to) {
        Piece piece = game.getPieceAt(to);
        if (piece == null) {
            return false;
        }
        return this.isWhite != piece.isWhite();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return value == piece.value && isWhite == piece.isWhite && square.equals(piece.square);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isWhite, square);
    }
}
