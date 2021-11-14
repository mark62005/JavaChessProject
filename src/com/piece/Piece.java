package com.piece;

import com.Game;
import com.move.*;
import com.Square;

import java.util.Objects;
import java.util.Set;

public abstract class Piece {
    private final int value;
    protected boolean isWhite;
    protected Square square;
    protected char symbol;

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

    protected abstract void setSymbol(boolean isWhite);

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
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

    protected void addPromotionMove(Set<Move> moves, Square from, Square to) {
        Queen queen = new Queen(isWhite);
        queen.setSquare(square);
        moves.add(new PawnPromotion(from, to, queen));
    }

    protected void addCastlingMove(Set<Move> moves, Square from, Square to, CastleSide side) {
        Square newTo = new Square(to);
        moves.add(new Castling(from, newTo, side));
    }

    protected void addEnPassantMove(Set<Move> moves, Square from, Square to, Pawn enemy) {
        moves.add(new EnPassant(from, to, enemy));
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
