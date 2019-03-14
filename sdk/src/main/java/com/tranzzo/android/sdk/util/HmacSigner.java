package com.tranzzo.android.sdk.util;

import androidx.annotation.NonNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Map;
import java.util.SortedMap;

public class HmacSigner {
    
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    
    public static String sign(SortedMap<String, ?> underTest, String secret) {
        
        StringBuilder outcome = new StringBuilder();
        
        for (Map.Entry<String, ?> entry : underTest.entrySet()) {
            Object value = entry.getValue();
            
            if (value != null) {
                outcome.append(value.toString());
            }
        }
        
        return generateHashWithHmac256(outcome.toString(), secret);
    }
    
    private static String generateHashWithHmac256(String message, String key) {
        try {
            return bytesToHex(hmac(key.getBytes(), message.getBytes()));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private static byte[] hmac(byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(message);
    }
    
    
    @NonNull
    private static String bytesToHex(@NonNull byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            final int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
}
