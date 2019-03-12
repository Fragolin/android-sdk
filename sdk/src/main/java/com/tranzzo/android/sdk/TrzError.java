package com.tranzzo.android.sdk;

import org.json.JSONException;
import org.json.JSONObject;

public class TrzError {
    public final String id;
    public final String message;
    
    public TrzError(String id, String message) {
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
     * @param json JSON to parse
     * @return either parsed exception or generic one.
     */
    public static TrzError fromJson(JSONObject json) {
        try {
            return new TrzError(json.getString("id"), json.getString("error_message"));
        } catch (JSONException e) {
            e.printStackTrace();
            return new TrzError("Unknown", "Failed to parse server response: " + json.toString());
        }
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
