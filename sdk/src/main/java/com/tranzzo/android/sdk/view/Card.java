package com.tranzzo.android.sdk.view;

import androidx.annotation.*;

import java.util.*;

/**
 * A model object representing a Card in the Android SDK.
 */
public class Card {
    
    public static final int CVC_LENGTH_AMERICAN_EXPRESS = 4;
    public static final int CVC_LENGTH_COMMON = 3;
    
    @Size(max = 19)
    private final String number;
    
    @Size(max = 4)
    private final String cvc;
    
    @IntRange(from = 1, to = 12)
    private final Integer expMonth;
    
    @IntRange(to = 99)
    private final Integer expYear;
    
    @NonNull
    private final CardBrand brand;
    
    /**
     * Checks whether {@code this} represents a valid card.
     *
     * @return {@code true} if valid, {@code false} otherwise.
     */
    public boolean isValid() {
        return isValid(Calendar.getInstance());
    }
    
    /**
     * Checks whether or not the {@link #number} field is valid.
     *
     * @return {@code true} if valid, {@code false} otherwise.
     */
    public boolean validateNumber() {
        return CardUtils.isValidCardNumber(number);
    }
    
    /**
     * Checks whether or not the {@link #expMonth} and {@link #expYear} fields represent a valid
     * expiry date.
     *
     * @return {@code true} if valid, {@code false} otherwise
     */
    public boolean validateExpiryDate() {
        return validateExpiryDate(Calendar.getInstance());
    }
    
    /**
     * Checks whether or not the {@link #cvc} field is valid.
     *
     * @return {@code true} if valid, {@code false} otherwise
     */
    public boolean validateCVC() {
        if (TranzzoTextUtils.isBlank(cvc)) {
            return false;
        }
        String cvcValue = cvc.trim();
        boolean validLength =
                (brand == null && cvcValue.length() >= 3 && cvcValue.length() <= 4)
                        || (brand == CardBrand.AMERICAN_EXPRESS && cvcValue.length() == 4)
                        || cvcValue.length() == 3;
        
        return ModelUtils.isWholePositiveNumber(cvcValue) && validLength;
    }
    
    public boolean validateExpMonth() {
        return expMonth != null && expMonth >= 1 && expMonth <= 12;
    }
    
    public boolean validateExpYear(Calendar now) {
        return expYear != null && !ModelUtils.hasYearPassed(expYear, now);
    }
    
    boolean isValid(@NonNull Calendar now) {
        if (cvc == null) {
            return validateNumber() && validateExpiryDate(now);
        } else {
            return validateNumber() && validateExpiryDate(now) && validateCVC();
        }
    }
    
    boolean validateExpiryDate(Calendar now) {
        if (!validateExpMonth()) {
            return false;
        }
        if (!validateExpYear(now)) {
            return false;
        }
        return !ModelUtils.hasMonthPassed(expYear, expMonth, now);
    }
    
    public Card(String number, Integer expMonth, Integer expYear, String cvc) {
        this.number = TranzzoTextUtils.nullIfBlank(CardUtils.normalizeCardNumber(number));
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvc = cvc;
        this.brand = CardBrand.fromNumber(number);
    }
    
    public Map<String, Object> toMap() {
        return new TreeMap<String, Object>() {{
            put("card_number", number);
            put("card_exp_month", expMonth);
            put("card_exp_year", expYear);
            put("card_cvv", cvc);
        }};
    }
    
}
