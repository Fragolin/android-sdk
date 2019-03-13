package com.tranzzo.android.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.*;
import com.tranzzo.android.sdk.annotation.BetaApi;
import com.tranzzo.android.sdk.annotation.InternalApi;
import com.tranzzo.android.sdk.util.Either;
import com.tranzzo.android.sdk.view.Card;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Entry point for Tranzzo SDK API.
 *
 * @see #tokenize(Card, Context)
 */
@SuppressLint("SimpleDateFormat")
public class Tranzzo {
    
    static final String OOPS_MESSAGE_INTERNAL = "An error occurred within Tranzzo SDK. Send us exception log and we will try to do out best!";
    static final String OOPS_MESSAGE_SERVER = "An error occurred within Tranzzo SDK. Send us this message and we will try to do out best: ";
    
    private final String apiToken;
    private final TranzzoApi api;
    private final TelemetryProvider telemetry;
    private final Log log;
    
    /**
     * Factory method for {@link Tranzzo}.
     * TODO: Add link to CDN docs, section: Android
     *
     * @param apiToken API Token generated by Tranzzo.
     */
    @NonNull
    public static Tranzzo init(String apiToken) {
        return new Tranzzo(
                apiToken,
                new HttpTranzzoApi(BuildConfig.TRANZZO_ENDPOINT),
                AndroidTelemetryProvider.INSTANCE,
                AndroidLogAdapter.INSTANCE
        );
    }
    
    /**
     * Private unstable constructor.
     *
     * @see #init(String)
     */
    @InternalApi
    @VisibleForTesting
    Tranzzo(String apiToken, TranzzoApi api, TelemetryProvider telemetry, Log log) {
        this.apiToken = apiToken;
        this.api = api;
        this.telemetry = telemetry;
        this.log = log;
    }
    
    /**
     * Performs PCI DSS compliant card tokenization on Tranzzo server.
     * Immediately returns an error for invalid card.
     *
     * @param card    card to tokenize
     * @param context application context
     * @return either successful {@link CardToken} result or {@link TrzError} inside {@link Either}
     * @see Card#isValid()
     * @see Either#isSuccessful()
     */
    @BetaApi
    @NonNull
    public Either<TrzError, CardToken> tokenize(@NonNull final Card card, @NonNull final Context context) {
        
        Either<TrzError, CardToken> result = card.isValid() ? doTokenize(card, context) : invalidCard();
        
        return result.peekLeft(e -> {
            log.error(OOPS_MESSAGE_INTERNAL);
            log.error(e.toString());
        });
        
    }
    
    @NonNull
    private Either<TrzError, CardToken> doTokenize(@NonNull final Card card, @NonNull final Context context) {
        
        SortedMap<String, Object> data = new TreeMap<>(card.toMap());
        data.putAll(telemetry.collect(context));
        
        log.debug("Request: " + data);
        
        return api
                .tokenize(data, apiToken)
                .peek(body -> log.debug("Response [success]: " + body))
                .flatMap(CardToken::fromJson)
                .peekLeft(e -> log.error("Response [failure]: " + e));
        
        
    }
    
    private <T> Either<TrzError, T> invalidCard() {
        return Either.failure(TrzError.mkInternal("Attempt to tokenize invalid card."));
    }
    
}
