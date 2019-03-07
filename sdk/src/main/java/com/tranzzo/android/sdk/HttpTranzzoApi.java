package com.tranzzo.android.sdk;

import android.util.Log;
import com.tranzzo.android.sdk.util.HmacSigner;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.SortedMap;

public class HttpTranzzoApi implements TranzzoApi {
    
    private final String baseUrl;
    
    public HttpTranzzoApi(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    @Override
    public TrzResponse tokenize(SortedMap<String, ?> body, String apiToken) {
        
        HttpURLConnection conn = null;
        
        String sign = HmacSigner.sign(body, apiToken);
        JSONObject requestBody = new JSONObject(body);
        
        try {
            URL url = new URL(baseUrl + "/api/v1/sdk/tokenize");
            conn = (HttpURLConnection) url.openConnection();
            
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Widget-Id", apiToken);
            conn.setRequestProperty("X-Sign", sign);
            
            final OutputStream out = conn.getOutputStream();
            out.write(requestBody.toString().getBytes("UTF-8"));
            out.close();
            
            int code = conn.getResponseCode();
            boolean successful = code >= 200 && code < 400;
            
            final InputStream in = successful ? conn.getInputStream() : conn.getErrorStream();
            //\A is the beginning of
            // the stream boundary
            final Scanner scanner = new Scanner(in, "UTF-8").useDelimiter("\\A");
            final String responseBody = scanner.hasNext() ? scanner.next() : null;
            in.close();
            
            return new TrzResponse(successful, responseBody);
        } catch (Exception e) {
            Log.e("HttpTranzzoApi", e.toString(), e);
            return new TrzResponse(false, e.toString());
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
