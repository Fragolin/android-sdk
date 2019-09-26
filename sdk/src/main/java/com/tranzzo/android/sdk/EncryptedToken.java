package com.tranzzo.android.sdk;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.tranzzo.android.sdk.util.Either;

import org.json.JSONObject;

public class EncryptedToken {
    
    @NonNull
    private final String data;
    
    @VisibleForTesting
    EncryptedToken(@NonNull String data) {
        this.data = data;
    }
    
    static Either<TrzError, EncryptedToken> fromJson(String successJson) {
        return Either.wrap(() -> {
            JSONObject json = new JSONObject(successJson);
            return new EncryptedToken(json.getString("data"));
        });
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        EncryptedToken that = (EncryptedToken) o;
    
        return data.equals(that.data);
    }
    
    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    @Override
    @NonNull
    public String toString() {
        return "EncryptedToken{data='" + data + "'}";
    }
    
}
