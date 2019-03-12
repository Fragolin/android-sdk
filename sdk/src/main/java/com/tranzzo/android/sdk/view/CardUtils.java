package com.tranzzo.android.sdk.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Utility class for functions to do with cards.
 */
public class CardUtils {
    
    private static final int LENGTH_COMMON_CARD = 16;
    private static final int LENGTH_AMERICAN_EXPRESS = 15;
    private static final int LENGTH_DINERS_CLUB = 14;
    
    /**
     * Returns a {@link Card.Brand} corresponding to a partial card number,
     * or {@link Card#UNKNOWN} if the card brand can't be determined from the input value.
     *
     * @param cardNumber a credit card number or partial card number
     * @return the {@link Card.Brand} corresponding to that number,
     * or {@link Card#UNKNOWN} if it can't be determined
     */
    @NonNull
    public static CardBrand getPossibleCardType(@Nullable String cardNumber) {
        return CardBrand.fromNumber(cardNumber);
    }
    
    /**
     * Checks the input string to see whether or not it is a valid card number, possibly
     * with groupings separated by spaces or hyphens.
     *
     * @param cardNumber a String that may or may not represent a valid card number
     * @return {@code true} if and only if the input value is a valid card number
     */
    public static boolean isValidCardNumber(@Nullable String cardNumber) {
        String normalizedNumber = TranzzoTextUtils.removeSpacesAndHyphens(cardNumber);
        return isValidLuhnNumber(normalizedNumber) && isValidCardLength(normalizedNumber);
    }
    
    @Nullable
    public static String normalizeCardNumber(@Nullable String number) {
        if (number == null) {
            return null;
        }
        return number.trim().replaceAll("\\s+|-", "");
    }
    
    /**
     * Checks the input string to see whether or not it is a valid Luhn number.
     *
     * @param cardNumber a String that may or may not represent a valid Luhn number
     * @return {@code true} if and only if the input value is a valid Luhn number
     */
    static boolean isValidLuhnNumber(@Nullable String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        
        boolean isOdd = true;
        int sum = 0;
        
        for (int index = cardNumber.length() - 1; index >= 0; index--) {
            char c = cardNumber.charAt(index);
            if (!Character.isDigit(c)) {
                return false;
            }
            
            int digitInteger = Character.getNumericValue(c);
            isOdd = !isOdd;
            
            if (isOdd) {
                digitInteger *= 2;
            }
            
            if (digitInteger > 9) {
                digitInteger -= 9;
            }
            
            sum += digitInteger;
        }
        
        return sum % 10 == 0;
    }
    
    /**
     * Checks to see whether the input number is of the correct length, after determining its brand.
     * This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @return {@code true} if the card number is of known type and the correct length
     */
    static boolean isValidCardLength(@Nullable String cardNumber) {
        return cardNumber != null && isValidCardLength(cardNumber,
                CardBrand.fromNumber(cardNumber));
    }
    
    /**
     * Checks to see whether the input number is of the correct length, given the assumed brand of
     * the card. This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @param cardBrand  a {@link Card.Brand} used to get the correct size
     * @return {@code true} if the card number is the correct length for the assumed brand
     */
    static boolean isValidCardLength(
            @Nullable String cardNumber,
            @NonNull CardBrand cardBrand) {
        if (cardNumber == null || cardBrand == CardBrand.UNKNOWN) {
            return false;
        }
        
        int length = cardNumber.length();
        switch (cardBrand) {
            case AMERICAN_EXPRESS:
                return length == LENGTH_AMERICAN_EXPRESS;
            case DINERS_CLUB:
                return length == LENGTH_DINERS_CLUB;
            default:
                return length == LENGTH_COMMON_CARD;
        }
    }
}
