package com.tranzzo.android.sdk.view;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TranzzoInputListener implements
        CardNumberEditText.CardBrandChangeListener,
        CardNumberEditText.CardNumberCompleteListener,
        ExpiryDateEditText.ExpiryDateEditListener,
        CvcEditText.CvcInputListener {
    
    private AtomicBoolean card = new AtomicBoolean();
    private AtomicBoolean expiry = new AtomicBoolean();
    private AtomicBoolean cvc = new AtomicBoolean();
    private AtomicReference<CardBrand> brand = new AtomicReference<>(CardBrand.UNKNOWN);
    
    private final InputCompletedListener listener;
    
    public TranzzoInputListener(
            @NonNull CardNumberEditText cardInput,
            @NonNull ExpiryDateEditText expiryInput,
            @NonNull CvcEditText cvcInput,
            @NonNull InputCompletedListener listener
    ) {
        cardInput.addCardNumberCompleteListener(this);
        cardInput.addCardBrandChangeListener(this);
        expiryInput.addExpiryDateEditListener(this);
        cvcInput.addCvcInputListener(this);
        this.listener = listener;
    }
    
    @Override
    public void onCardNumberComplete() {
        card.set(true);
        checkCompletion();
    }
    
    @Override
    public void onCardBrandChanged(CardBrand brand) {
        this.brand.set(brand);
        checkCompletion();
    }
    
    @Override
    public void onExpiryDateComplete() {
        expiry.set(true);
        checkCompletion();
    }
    
    @Override
    public void onCvcInputComplete() {
        cvc.set(true);
        checkCompletion();
    }
    
    private void checkCompletion() {
        if (card.get() && expiry.get() && cvc.get()) {
            listener.onInputCompleted();
        }
    }
    
    public interface InputCompletedListener {
        void onInputCompleted();
    }
    
}
