package com.tranzzo.android.sdk;

import java.util.SortedMap;

public interface TranzzoApi {
    
    TrzResponse tokenize(SortedMap<String, ?> body, String apiToken);
    
}
