package com.tranzzo.android.sdk.util;

import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class HmacSignerTest {
    @Test
    public void testJsonFromMap() {
        
        SortedMap<String, String> underTest = new TreeMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }};
        
        String sign = HmacSigner.sign(underTest, "secret");
        
        assertEquals(
                "77de38e4b50e618a0ebb95db61e2f42697391659d82c064a5f81b9f48d85ccd5",
                sign
        );
        
    }
}