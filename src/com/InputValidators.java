package com;

import java.util.regex.Pattern;

public class InputValidators {

    public static final String VALID_INPUT_FORMAT_REGEX = "^((help)|(board)|(resign)|(moves))$";
    public static final String SQUARE_REGEX = "^[a-h][1-8]$";
    public static final String UCI_REGEX = "^([a-h][1-8]){2}q?$";
    private static final Pattern validInputFormat = Pattern.compile(VALID_INPUT_FORMAT_REGEX);
    private static final Pattern squarePattern = Pattern.compile(SQUARE_REGEX);
    private static final Pattern uciPattern = Pattern.compile(UCI_REGEX);

    public static boolean isValidInputFormat(String userInput) {
        return validInputFormat.matcher(userInput).matches() ||
                squarePattern.matcher(userInput).matches() ||
                uciPattern.matcher(userInput).matches();
    }

    public static boolean isSquarePattern(String userInput) {
        return squarePattern.matcher(userInput).matches();
    }

    public static boolean isUciPattern(String userInput) {
        return uciPattern.matcher(userInput).matches();
    }

}
