package com.tranzzo.android.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.concurrent.TimeUnit;

final class AndroidTelemetryProvider implements TelemetryProvider {
    
    static final TelemetryProvider INSTANCE = new AndroidTelemetryProvider();
    
    private AndroidTelemetryProvider(){}
    
    @NonNull
    @Override
    public Map<String, String> collect(@NonNull final Context context) {
        return new TreeMap<String, String>() {{
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
    }
    
    @NonNull
    private String getApplicationName(@NonNull final Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
    
    @NonNull
    private String getAndroidVersionString() {
        final String delimiter = " ";
        return "Android" + delimiter +
                Build.VERSION.RELEASE + delimiter +
                Build.VERSION.CODENAME + delimiter +
                Build.VERSION.SDK_INT;
    }
    
    @NonNull
    @SuppressLint("HardwareIds")
    private String getHashedId(@NonNull final Context context) {
        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (id == null || id.isEmpty()) {
            return "";
        }
        
        return id;
    }
    
    
    @NonNull
    private String getScreen(@NonNull final Context context) {
        if (context.getResources() == null) {
            return "";
        }
        
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int density = context.getResources().getDisplayMetrics().densityDpi;
        
        return String.format(Locale.ENGLISH, "%dw_%dh_%ddpi", width, height, density);
    }
    
    
    @NonNull
    private String getTimeZoneString() {
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
    
}
