package com.tranzzo.android.sdk;

import androidx.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class TrzError {
    
    public final String id;
    public final String message;
    
    public TrzError(@NonNull String id, @NonNull String message) {
        this.id = id;
        this.message = message;
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
            return new TrzError(json.getString("id"), json.getString("error_message"));
        } catch (JSONException e) {
            e.printStackTrace();
            return TrzError.mkInternal(Tranzzo.OOPS_MESSAGE_SERVER + "Failed to parse server response: " + jsonString);
        }
    }
    
    public static TrzError mkInternal(String message) {
        return new TrzError(UUID.randomUUID().toString(), message);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TrzError trzError = (TrzError) o;
        
        if (!id.equals(trzError.id)) return false;
        return message.equals(trzError.message);
    }
    
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TrzError{");
        sb.append("id='").append(id).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
