package com.tranzzo.android.sdk.view;

import androidx.annotation.*;

import java.util.*;

/**
 * A model object representing a Card in the Android SDK.
 */
public class Card {
    
    private static final Calendar UTC;
    
    static {
        UTC = Calendar.getInstance();
        UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    static final String F_CARD_NUMBER = "card_number";
    static final String F_CARD_EXP_MONTH = "card_exp_month";
    static final String F_CARD_EXP_YEAR = "card_exp_year";
    static final String F_CARD_CVC = "card_cvv";
    
    @VisibleForTesting
    final String number;
    
    @VisibleForTesting
    final int expMonth;
    
    @VisibleForTesting
    final int expYear;
    
    @VisibleForTesting
    final String cvc;
    
    @NonNull
    @VisibleForTesting
    private final CardBrand brand;
    
    public Card(
            @Size(max = 19) String number,
            @IntRange(from = 1, to = 12) int expMonth,
            @IntRange(to = 99) int expYear,
            @Size(min = 3, max = 4) String cvc) {
        this.number = TranzzoTextUtils.nullIfBlank(CardUtils.normalizeCardNumber(number));
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvc = cvc.trim();
        this.brand = CardBrand.fromNumber(number);
    }
    
    public Card(
            String number,
            ExpiryFields expiry,
            String cvc
    ) {
        this(number, expiry.expMonth, expiry.expYear, cvc);
    }
    
    /**
     * Checks whether {@code this} represents a valid card.
     *
     * @return {@code true} if valid, {@code false} otherwise.
     */
    public boolean isValid() {
        return isValid(UTC);
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
    public boolean validateCvc() {
        if (TranzzoTextUtils.isBlank(cvc)) {
            return false;
        }
        String cvcValue = cvc.trim();
        return ModelUtils.isWholePositiveNumber(cvcValue) && cvcValue.length() == brand.cvvLength;
    }
    
    public boolean validateExpMonth() {
        return ModelUtils.isValidMonth(expMonth);
    }
    
    public boolean validateExpYear(Calendar now) {
        return !ModelUtils.hasYearPassed(expYear, now);
    }
    
    boolean isValid(@NonNull Calendar now) {
        if (cvc == null) {
            return validateNumber() && validateExpiryDate(now);
        } else {
            return validateNumber() && validateExpiryDate(now) && validateCvc();
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
    
    public Map<String, Object> toMap() {
        return new TreeMap<String, Object>() {{
            put(F_CARD_NUMBER, number);
            put(F_CARD_EXP_MONTH, expMonth);
            put(F_CARD_EXP_YEAR, expYear);
            put(F_CARD_CVC, cvc);
        }};
    }
    
}
