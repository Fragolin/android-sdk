package com.tranzzo.android.sdk;

import androidx.annotation.NonNull;
import com.tranzzo.android.sdk.annotation.InternalApi;
import com.tranzzo.android.sdk.util.Either;

import java.util.SortedMap;

public interface TranzzoApi {
    
    @InternalApi
    @NonNull
    Either<TrzError, String> tokenize(@NonNull final SortedMap<String, ?> body, @NonNull final String apiToken);
    
}
