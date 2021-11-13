package com;

import java.util.Arrays;
import java.util.regex.Pattern;

public class InputValidators {

    public static final String VALID_INPUT_FORMAT_REGEX = "^((help)|(board)|(resign)|(moves))$";
    public static final String SQUARE_REGEX = "^[a-h][1-8]$";
    public static final String UCI_REGEX = "^([a-h][1-8]){2}$";
    public static final String PROMOTION_REGEX = "^([a-h][2,7])([a-h][1,8])[kbrq]$";
    public static final String[] CASTLING_UCIS = { "e1c1", "e1g1", "e8c8", "e8g8" };

    private static final Pattern validInputFormat = Pattern.compile(VALID_INPUT_FORMAT_REGEX);
    private static final Pattern squarePattern = Pattern.compile(SQUARE_REGEX);
    private static final Pattern uciPattern = Pattern.compile(UCI_REGEX);
    private static final Pattern promotionPattern = Pattern.compile(PROMOTION_REGEX);

    public static boolean isValidInputFormat(String userInput) {
        return validInputFormat.matcher(userInput).matches() ||
                squarePattern.matcher(userInput).matches() ||
                uciPattern.matcher(userInput).matches() ||
                promotionPattern.matcher(userInput).matches();
    }

    public static boolean isSquarePattern(String userInput) {
        return squarePattern.matcher(userInput).matches();
    }

    public static boolean isUciPattern(String userInput) {
        return uciPattern.matcher(userInput).matches();
    }

    public static boolean isPromotionPattern(String userInput) {
        return promotionPattern.matcher(userInput).matches();
    }

    public static boolean isCastlingUCI(String userInput) {
        return Arrays.asList(CASTLING_UCIS).contains(userInput);
    }

}
