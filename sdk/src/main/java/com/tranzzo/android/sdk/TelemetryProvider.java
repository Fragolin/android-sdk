package com.tranzzo.android.sdk;

import android.content.Context;
import androidx.annotation.NonNull;

import java.util.Map;

interface TelemetryProvider {
    
    @NonNull
    Map<String, String> collect(@NonNull Context context);
    
}
