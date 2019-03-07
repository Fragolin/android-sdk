package com.tranzzo.android.sdk;

import org.json.JSONException;
import org.json.JSONObject;

public class TrzResponse {
    public final boolean success;
    public final String body;
    
    public TrzResponse(boolean success, String body) {
        this.success = success;
        this.body = body;
    }
    public static class TrzError {
        public final String id;
        public final String message;
    
        public TrzError(String id, String message) {
            this.id = id;
            this.message = message;
        }
    
        public static TrzError fromJson(JSONObject json){
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
    
}
