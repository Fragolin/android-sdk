package com.tranzzo.android.sdk;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class TrzError {
    
    public final String id;
    public final String message;
    public final Throwable cause;
    
    public TrzError(@NonNull String id, @NonNull String message) {
        this(id, message, null);
    }
    
    public TrzError(@NonNull String id, @NonNull String message, Throwable cause) {
        this.id = id;
        this.message = message;
        this.cause = cause;
    }
    
    /**
     * JSON example:
     * <pre>
     * {@code
     * {
     *     "id": "A90qBCUUvEBgJ90w",
     *     "error_message": "BAD_REQUEST"
     * }
     * }
     * </pre>
     *
     * @param jsonString JSON to parse
     * @return either parsed exception or generic one.
     */
    static TrzError fromJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            String errorMessage = json.getString("error_message");
            
            if (errorMessage != null){
                return new TrzError(json.getString("id"), errorMessage);
            } else {
                return TrzError.mkInternal(Tranzzo.oopsMessage("Failed to parse server response:", jsonString));
            }
        } catch (JSONException e) {
            return TrzError.mkInternal(Tranzzo.oopsMessage("Failed to parse server response:", jsonString), e);
        }
    }
    
    public static TrzError mkInternal(String message) {
        return mkInternal(message, null);
    }
    
    public static TrzError mkInternal(String message, Throwable cause) {
        return new TrzError(UUID.randomUUID().toString(), message, cause);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TrzError trzError = (TrzError) o;
        
        if (!id.equals(trzError.id)) return false;
        if (!message.equals(trzError.message)) return false;
        return cause != null ? cause.equals(trzError.cause) : trzError.cause == null;
    }
    
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + (cause != null ? cause.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TrzError{");
        sb.append("id='").append(id).append('\'');
        sb.append(", message='").append(message).append('\'');
        if (cause != null){
            sb.append(", cause='").append(cause).append('\'');
        }
        sb.append('}');
        return sb.toString();
    }
}
