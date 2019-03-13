package com.tranzzo.android.sdk;

import android.annotation.SuppressLint;
import androidx.annotation.VisibleForTesting;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.*;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class CardToken {
    
    @VisibleForTesting
    static final DateFormat DATE_TIME_PARSER;
    
    private final String token;
    private final Date expiresAt;
    private final String mask;
    
    static {
        DATE_TIME_PARSER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DATE_TIME_PARSER.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    @VisibleForTesting
    CardToken(String token, Date expiresAt, String mask) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.mask = mask;
    }
    
    static CardToken fromJson(String successJson) throws JSONException, ParseException {
        JSONObject json = new JSONObject(successJson);
        return new CardToken(
                json.getString("token"),
                DATE_TIME_PARSER.parse(json.getString("expires_at")),
                json.getString("card_mask")
        );
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
        sb.append("value='").append(token).append('\'');
        sb.append(", expiresAt=").append(expiresAt);
        sb.append(", mask='").append(mask).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
