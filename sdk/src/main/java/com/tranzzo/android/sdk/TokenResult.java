package com.tranzzo.android.sdk;

public class TokenResult {
    
    public final CardToken token;
    public final TrzResponse.TrzError error;
    
    private TokenResult(CardToken token, TrzResponse.TrzError error) {
        this.token = token;
        this.error = error;
    }
    
    public boolean isSuccessful() {
        return null == error;
    }
    
    public static TokenResult failure(TrzResponse.TrzError error) {
        return new TokenResult(null, error);
    }
    
    public static TokenResult success(CardToken token) {
        return new TokenResult(token, null);
    }
    
}
