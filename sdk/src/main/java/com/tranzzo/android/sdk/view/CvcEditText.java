package com.tranzzo.android.sdk.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.tranzzo.android.sdk.Either;
import com.tranzzo.android.sdk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * An {@link EditText} that handles CVC input.
 */
public class CvcEditText extends TranzzoEditText {
    
    private static final Pattern cvcPattern = Pattern.compile("[0-9]{3,4}");
    
    private List<CvcInputListener> mCvcInputListeners = new ArrayList<>();
    private boolean mIsCvcValid;
    
    public CvcEditText(Context context) {
        super(context);
        setupView();
        listenForTextChanges();
    }
    
    public CvcEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
        listenForTextChanges();
    }
    
    public CvcEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
        listenForTextChanges();
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        String accLabel = getResources().getString(
                R.string.acc_label_expiry_date_node,
                getText()
        );
        info.setText(accLabel);
    }
    
    /**
     * Gets whether or not the date currently entered is valid and not yet
     * passed.
     *
     * @return {@code true} if the text entered represents a valid expiry date that has not
     * yet passed, and {@code false} if not.
     */
    public boolean isCvcValid() {
        return mIsCvcValid;
    }
    
    @NonNull
    public Either<String, String> getCvc(){
        if (!mIsCvcValid){
            return Either.failure("Invalid CVC value");
        } else {
            return Either.success(getText().toString());
        }
    }
    
    public void addCvcInputListener(CvcInputListener mCvcInputListener) {
        this.mCvcInputListeners.add(mCvcInputListener);
    }
    
    private void setupView(){
        setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }
    
    private void listenForTextChanges() {
        addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cvc = s.toString();
                mIsCvcValid = cvcPattern.matcher(cvc).matches();
                if (mIsCvcValid){
                    for (CvcInputListener listener : mCvcInputListeners) {
                        listener.onCvcInputComplete();
                    }
                }
            }
        });
    }
    
    /**
     * This listener is triggered on CVC input completion.
     *
     * @note event is triggered only for valid CVC.
     * @see #addCvcInputListener(CvcInputListener)
     */
    public interface CvcInputListener {
        void onCvcInputComplete();
    }
}
