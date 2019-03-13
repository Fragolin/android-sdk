package com.tranzzo.android.sdk;

import com.tranzzo.android.sdk.annotation.InternalApi;
import com.tranzzo.android.sdk.util.Either;

import java.util.SortedMap;

public interface TranzzoApi {
    
    @InternalApi
    Either<TrzError, String> tokenize(SortedMap<String, ?> body, String apiToken);
    
}
