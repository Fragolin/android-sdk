package com.tranzzo.android.sdk;

import com.tranzzo.android.sdk.util.HmacSigner;
import org.json.JSONObject;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    
    @Test
    public void testJsonFromMap() {
    
        SortedMap<String, String> data = new TreeMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }};
    
        JSONObject underTest = new JSONObject();
        
        String sign = HmacSigner.sign(data, "secret");
        
        assertEquals(
                "77de38e4b50e618a0ebb95db61e2f42697391659d82c064a5f81b9f48d85ccd5",
                sign
        );
        
    }
}