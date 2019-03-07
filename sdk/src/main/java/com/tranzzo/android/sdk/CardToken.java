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
    public String toString() {
        final StringBuilder sb = new StringBuilder("CardToken{");
        sb.append("token='").append(token).append('\'');
        sb.append(", expiresAt=").append(expiresAt);
        sb.append(", mask='").append(mask).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
