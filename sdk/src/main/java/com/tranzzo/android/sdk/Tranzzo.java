package com.tranzzo.android.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.util.Log;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Tranzzo {
    
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_TIME_PARSER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    private final String apiToken;
    private final TranzzoApi api;
    
    private Tranzzo(String apiToken, TranzzoApi api) {
        
        this.apiToken = apiToken;
        this.api = api;
    }
    
    @NonNull
    private static String getAndroidVersionString() {
        StringBuilder builder = new StringBuilder();
        final String delimiter = " ";
        builder.append("Android").append(delimiter)
                .append(Build.VERSION.RELEASE).append(delimiter)
                .append(Build.VERSION.CODENAME).append(delimiter)
                .append(Build.VERSION.SDK_INT);
        return builder.toString();
    }
    
    @NonNull
    @SuppressLint("HardwareIds")
    static String getHashedId(@NonNull final Context context) {
        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (id == null || id.isEmpty()) {
            return "";
        }
        
        return id;
    }
    
    
    @NonNull
    private static String getScreen(@NonNull final Context context) {
        if (context.getResources() == null) {
            return "";
        }
        
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int density = context.getResources().getDisplayMetrics().densityDpi;
        
        return String.format(Locale.ENGLISH, "%dw_%dh_%ddpi", width, height, density);
    }
    
    
    @NonNull
    private static String getTimeZoneString() {
        int minutes =
                (int) TimeUnit.MINUTES.convert(TimeZone.getDefault().getRawOffset(),
                        TimeUnit.MILLISECONDS);
        if (minutes % 60 == 0) {
            int hours = minutes / 60;
            return String.valueOf(hours);
        }
        
        BigDecimal decimalValue = new BigDecimal(minutes);
        decimalValue = decimalValue.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal decHours = decimalValue.divide(
                new BigDecimal(60),
                new MathContext(2))
                .setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return decHours.toString();
    }
    
    
    public TokenResult tokenize(final Card card, final Context context) {
        try {
            SortedMap<String, ?> data = new TreeMap<String, Object>() {{
                put("card_number", card.number);
                put("card_exp_month", card.expMonth);
                put("card_exp_year", card.expYear);
                put("card_cvv", card.cvv);
                
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
            
            if (response.success){
                Log.d("TRZ_API", "Response [success]: " + response);
                JSONObject json = new JSONObject(response.body);
    
                return TokenResult.success(new CardToken(
                        json.getString("token"),
                        DATE_TIME_PARSER.parse(json.getString("expires_at")),
                        json.getString("card_mask")
                ));
            } else {
                Log.e("TRZ_API", "Response [failure]: " + response.body);
                return TokenResult.failure(TrzResponse.TrzError.fromJson(new JSONObject(response.body)));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return TokenResult.failure(new TrzResponse.TrzError("Internal", e.toString()));
        }
    }
    
    public static Tranzzo init(String apiToken) {
        return new Tranzzo(apiToken, new HttpTranzzoApi(BuildConfig.TRANZZO_ENDPOINT));
    }
    
    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
    
    
}
