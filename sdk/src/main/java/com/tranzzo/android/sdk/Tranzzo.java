package com.tranzzo.android.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.tranzzo.android.sdk.view.Card;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tranzzo.android.sdk.TelemetryUtils.*;


/**
 * Entry point for Tranzzo SDK API.
 */
@SuppressLint("SimpleDateFormat")
public class Tranzzo {
    
    
    private static final DateFormat DATE_TIME_PARSER;
    
    static {
        DATE_TIME_PARSER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DATE_TIME_PARSER.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    private final String apiToken;
    private final TranzzoApi api;
    
    /**
     * Factory method for {@link Tranzzo}.
     * TODO: Add link to CDN docs, section: Android
     *
     * @param apiToken API Token generated by Tranzzo.
     */
    public static Tranzzo init(String apiToken) {
        return new Tranzzo(apiToken, new HttpTranzzoApi(BuildConfig.TRANZZO_ENDPOINT));
    }
    
    /**
     * Private unstable constructor.
     *
     * @see #init(String)
     */
    private Tranzzo(String apiToken, TranzzoApi api) {
        this.apiToken = apiToken;
        this.api = api;
    }
    
    public TokenResult tokenize(final Card card, final Context context) {
        try {
            SortedMap<String, ?> data = new TreeMap<String, Object>(card.toMap()) {{
                put("platform", "android");
                put("sdk_version", String.valueOf(BuildConfig.VERSION_CODE));
                put("os_version", getAndroidVersionString());
                put("os_build_version", Build.VERSION.RELEASE);
                put("os_build_number", String.valueOf(Build.VERSION.SDK_INT));
                put("device_id", getHashedId(context));
                put("device_manufacturer", Build.MANUFACTURER);
                put("device_brand", Build.BRAND);
                put("device_model", Build.MODEL);
                put("device_tags", Build.TAGS);
                put("device_screen_res", getScreen(context));
                put("device_locale", Locale.getDefault().toString());
                put("device_time_zone", getTimeZoneString());
                put("app_name", getApplicationName(context));
                put("app_package", context.getPackageName());
            }};
            
            Log.d("TRZ_API", "Request: " + data);
            
            TrzResponse response = api.tokenize(data, apiToken);
            
            if (response.success) {
                Log.d("TRZ_API", "Response [success]: " + response);
                JSONObject json = new JSONObject(response.body);
                
                return TokenResult.success(new CardToken(
                        json.getString("token"),
                        DATE_TIME_PARSER.parse(json.getString("expires_at")),
                        json.getString("card_mask")
                ));
            } else {
                Log.e("TRZ_API", "Response [failure]: " + response.body);
                return TokenResult.failure(TrzError.fromJson(new JSONObject(response.body)));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return TokenResult.failure(new TrzError("Internal", e.toString()));
        }
    }
    
    
}
