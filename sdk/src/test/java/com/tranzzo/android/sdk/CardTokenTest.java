package com.tranzzo.android.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.text.ParseException;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class CardTokenTest {
    
    private String token = "IAMTOKEN";
    private String exp = "2022-12-31T00:00:00";
    private String cardMask = "424242******4242";
    
    private String successJson = "{\n" +
            "  \"card_mask\": \"" + cardMask + "\",\n" +
            "  \"expires_at\": \"" + exp + "\",\n" +
            "  \"token\": \"" + token + "\"\n" +
            "}";
    
    private CardToken cardToken;
    
    {
        try {
            cardToken = new CardToken(token, CardToken.DATE_TIME_PARSER.parse(exp), cardMask);
        } catch (ParseException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    @Test
    public void testParsingFromValidJSON() throws Exception{
        CardToken actual = CardToken.fromJson(successJson);
        assertEquals(cardToken, actual);
    }
}