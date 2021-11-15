package com;

public enum Color {

    WHITE {
        @Override
        public String toString() {
            return "white";
        }
    },
    BLACK {
        @Override
        public String toString() {
            return "black";
        }
    }

}
