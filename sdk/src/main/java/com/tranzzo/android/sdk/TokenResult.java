package com.tranzzo.android.sdk;

/**
 * Represents tokenization result.
 */
public class TokenResult {
    
    public final CardToken token;
    public final TrzError error;
    
    static TokenResult failure(TrzError error) {
        return new TokenResult(null, error);
    }
    
    static TokenResult success(CardToken token) {
        return new TokenResult(token, null);
    }
    
    private TokenResult(CardToken token, TrzError error) {
        this.token = token;
        this.error = error;
    }
    
    public boolean isSuccessful() {
        return null == error;
    }
    
}
