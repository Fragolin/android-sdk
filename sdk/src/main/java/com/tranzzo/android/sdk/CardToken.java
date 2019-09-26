package com.tranzzo.android.sdk;

import androidx.annotation.VisibleForTesting;

import com.tranzzo.android.sdk.util.Either;

import org.json.JSONObject;

public class CardToken {
    
    private final String token;
    
    @VisibleForTesting
    CardToken(String token) {
        this.token = token;
    }
    
    static Either<TrzError, CardToken> fromJson(String successJson) {
        return Either.wrap(() -> {
            JSONObject json = new JSONObject(successJson);
            return new CardToken(json.getString("token"));
        });
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        CardToken cardToken = (CardToken) o;
        
        return token.equals(cardToken.token);
    }
    
    @Override
    public int hashCode() {
        return token.hashCode();
    }
    
    @Override
    public String toString() {
        return "CardToken{token='" + token + "'}";
    }
}
