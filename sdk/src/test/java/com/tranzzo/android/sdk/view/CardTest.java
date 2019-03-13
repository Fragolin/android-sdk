package com.tranzzo.android.sdk.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;
import java.util.TreeMap;

import static com.tranzzo.android.sdk.view.Card.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class CardTest {
    
    private String validNumberRaw = "4242 4242 4242 4242";
    private String validNumberFormatted = "4242424242424242";
    
    private final int expMonth = 12;
    private final int expYear = 50;
    private final String cvc = "000";
    
    private Card validCard = new Card(validNumberRaw, expMonth, expYear, cvc);
    
    @Test
    public void cardNumberShouldBeNormalized() {
        assertEquals(
                validCard.number,
                validNumberFormatted
        );
    }
    
    @Test
    public void cvcShouldBeNormalized() {
        assertEquals(
                new Card(validNumberRaw, expMonth, expYear, "  000  ").cvc,
                cvc
        );
    }
    
    @Test
    public void shouldBeConstructedProperlyFromExpiry() {
        Card underTest = new Card(validNumberRaw, new ExpiryFields(expMonth, expYear), cvc);
        assertEquals(
                underTest.expMonth,
                expMonth
        );
        assertEquals(
                underTest.expYear,
                expYear
        );
    }
    
    @Test
    public void shouldBeProperlyValidated() {
        assertTrue(validCard.isValid());
    }
    
    @Test
    public void validateNumber() {
        assertTrue(validCard.validateNumber());
    }
    
    @Test
    public void validateExpiryDate() {
        assertTrue(validCard.validateExpiryDate());
    }
    
    @Test
    public void validateCvc() {
        assertTrue(validCard.validateCvc());
    }
    
    @Test
    public void validateExpMonth() {
        assertTrue(validCard.validateExpMonth());
    }
    
    @Test
    public void validateExpYear() {
        assertTrue(validCard.validateExpYear(Calendar.getInstance()));
    }
    
    @Test
    public void validateExpiry() {
        assertTrue(validCard.validateExpiryDate());
    }
    
    @Test
    public void toMap() {
        assertEquals(
                new TreeMap<String, Object>() {{
                    put(F_CARD_NUMBER, validNumberFormatted);
                    put(F_CARD_EXP_MONTH, expMonth);
                    put(F_CARD_EXP_YEAR, expYear);
                    put(F_CARD_CVC, cvc);
                }},
                validCard.toMap()
        );
    }
}