package com.tranzzo.android.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

final class TelemetryUtils {
    
    @NonNull
    static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
    
    @NonNull
    static String getAndroidVersionString() {
        final String delimiter = " ";
        return "Android" + delimiter +
                Build.VERSION.RELEASE + delimiter +
                Build.VERSION.CODENAME + delimiter +
                Build.VERSION.SDK_INT;
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
    static String getScreen(@NonNull final Context context) {
        if (context.getResources() == null) {
            return "";
        }
        
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int density = context.getResources().getDisplayMetrics().densityDpi;
        
        return String.format(Locale.ENGLISH, "%dw_%dh_%ddpi", width, height, density);
    }
    
    
    @NonNull
    static String getTimeZoneString() {
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
