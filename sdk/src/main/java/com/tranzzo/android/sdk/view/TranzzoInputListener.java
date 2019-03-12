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
    
    private final SafeCallable<Boolean> isFormValidProgram;
    
    public TranzzoInputListener(
            @NonNull final CardNumberEditText cardInput,
            @NonNull final ExpiryDateEditText expiryInput,
            @NonNull final CvcEditText cvcInput,
            @NonNull final InputCompletedListener listener
    ) {
        cardInput.addCardNumberCompleteListener(this);
        cardInput.addCardBrandChangeListener(this);
        expiryInput.addExpiryDateEditListener(this);
        cvcInput.addCvcInputListener(this);
        
        this.isFormValidProgram = new SafeCallable<Boolean>() {
            @Override
            public Boolean call() {
                return cardInput.isCardNumberValid() && expiryInput.isDateValid() && cvcInput.isCvcValid();
            }
        };
        
        this.listener = listener;
    }
    
    public boolean isFormValid() {
        try {
            return isFormValidProgram.call();
        } catch (Exception e) {
            return false;
        }
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
    
    private interface SafeCallable<V> {
        V call();
    }
    
}
