package com.tranzzo.android.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class CardTokenTest {
    
    private String token = "IAMTOKEN";
    
    private String successJson = String.format("{\"token\": \"%s\"}", token);
    
    private CardToken cardToken = new CardToken(token);;
    
    @Test
    public void testParsingFromValidJSON() {
        assertEquals(cardToken, CardToken.fromJson(successJson).valueOrNull());
    }
}