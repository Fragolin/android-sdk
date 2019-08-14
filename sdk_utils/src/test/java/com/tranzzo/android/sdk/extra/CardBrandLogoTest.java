package com.tranzzo.android.sdk.extra;

import com.tranzzo.android.sdk.view.CardBrand;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardBrandLogoTest {
    
    @Test
    public void shouldReturnLogoForKnownBrand() {
        CardBrandLogo underTest = CardBrandLogo.fromBrand(CardBrand.AMERICAN_EXPRESS);
        CardBrandLogo expected = CardBrandLogo.AMERICAN_EXPRESS;
        
        assertEquals(expected, underTest);
    }
    
    @Test
    public void shouldReturnLogoForUnkownBrand() {
        CardBrandLogo underTest = CardBrandLogo.fromBrand(CardBrand.UNKNOWN);
        CardBrandLogo expected = CardBrandLogo.UNKNOWN;
        
        assertEquals(expected, underTest);
    }
}