package com;

import java.util.Objects;

public class Square {
    private int rank; // row
    private int file; // col

    public Square(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    public Square(Square square) {
        this.rank = square.getRank();
        this.file = square.getFile();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public Square add(int rankToAdd, int fileToAdd) {
        addRank(rankToAdd);
        addFile(fileToAdd);
        return this;
    }

    public void addRank(int rankToAdd) {
        setRank(this.rank + rankToAdd);
    }

    public void addFile(int fileToAdd) {
        setFile(this.file + fileToAdd);
    }

    public static Square parse(String squareStr) {
        int file = parseUnit(squareStr.charAt(0));
        int rank = parseUnit(squareStr.charAt(1));

        return new Square(rank, file);
    }

    private static int parseUnit(char ch) {
        switch (ch) {
            case 'a' : { return 0; }
            case '1' : { return 0; }
            case 'b': { return 1; }
            case '2' : { return 1; }
            case 'c' : { return 2; }
            case '3' : { return 2; }
            case 'd' : { return 3; }
            case '4' : { return 3; }
            case 'e' : { return 4; }
            case '5' : { return 4; }
            case 'f' : { return 5; }
            case '6' : { return 5; }
            case '7' : { return 6; }
            case 'g' : { return 6; }
            case 'h': { return 7; }
            case '8' : { return 7; }
            default : throw new IndexOutOfBoundsException();
        }
    }

    public boolean isWithinBorder() {
        return rank < 8 && rank >= 0 && file < 8 && file >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return rank == square.rank && file == square.file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, file);
    }

    @Override
    public String toString() {
        String fileStr = "";
        switch (file) {
            case 0 : fileStr = "a";
                break;
            case 1 : fileStr = "b";
                break;
            case 2 : fileStr = "c";
                break;
            case 3 : fileStr = "d";
                break;
            case 4 : fileStr = "e";
                break;
            case 5 : fileStr = "f";
                break;
            case 6 : fileStr = "g";
                break;
            case 7 : fileStr = "h";
                break;
            default : {}
        }

        return fileStr + (rank + 1);
    }
}
