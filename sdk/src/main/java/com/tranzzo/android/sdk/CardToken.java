package com.tranzzo.android.sdk;

import java.util.Date;

public class CardToken {
    
    private final String token;
    private final Date expiresAt;
    private final String mask;
    
    
    CardToken(String token, Date expiresAt, String mask) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.mask = mask;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        CardToken cardToken = (CardToken) o;
        
        if (!token.equals(cardToken.token)) return false;
        if (!expiresAt.equals(cardToken.expiresAt)) return false;
        return mask.equals(cardToken.mask);
    }
    
    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + expiresAt.hashCode();
        result = 31 * result + mask.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CardToken{");
        sb.append("token='").append(token).append('\'');
        sb.append(", expiresAt=").append(expiresAt);
        sb.append(", mask='").append(mask).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
