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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TokenResult that = (TokenResult) o;
        
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return error != null ? error.equals(that.error) : that.error == null;
    }
    
    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TokenResult{");
        sb.append("token=").append(token);
        sb.append(", error=").append(error);
        sb.append('}');
        return sb.toString();
    }
}
