package com.tranzzo.android.sdk;

import android.util.Log;
import com.tranzzo.android.sdk.annotation.InternalApi;
import com.tranzzo.android.sdk.util.HmacSigner;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.SortedMap;

class HttpTranzzoApi implements TranzzoApi {
    
    private final String baseUrl;
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = new TranzzoSSLSocketFactory();
    
    public HttpTranzzoApi(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    @Override
    @InternalApi
    public TrzResponse tokenize(SortedMap<String, ?> jsonParams, String apiToken) {
        
        HttpURLConnection conn = null;
        
        String sign = HmacSigner.sign(jsonParams, apiToken);
        JSONObject requestBody = new JSONObject(jsonParams);
        
        try {
            URL url = new URL(baseUrl + "/api/v1/sdk/tokenize");
            conn = (HttpURLConnection) url.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(SSL_SOCKET_FACTORY);
            }
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Widget-Id", apiToken);
            conn.setRequestProperty("X-Sign", sign);
            conn.setInstanceFollowRedirects(false);
            conn.setUseCaches(false);
            conn.setConnectTimeout(30 * 1000);
            conn.setReadTimeout(80 * 1000);
    
            byte[] body = requestBody.toString().getBytes("UTF-8");
            conn.setFixedLengthStreamingMode(body.length);
            
            final OutputStream out = conn.getOutputStream();
            out.write(body);
            out.close();
            
            int code = conn.getResponseCode();
            boolean successful = code >= 200 && code < 400;
            
            final InputStream in = new BufferedInputStream(successful ? conn.getInputStream() : conn.getErrorStream());
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
