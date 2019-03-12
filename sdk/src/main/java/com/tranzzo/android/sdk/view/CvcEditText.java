package com.tranzzo.android.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.tranzzo.android.sdk.R;
import com.tranzzo.android.sdk.util.TextWatcherAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link EditText} that handles putting numbers around a central divider character.
 */
public class CvcEditText extends TranzzoEditText {
    
    private List<CvcInputListener> mCvcInputListeners = new ArrayList<>();
    private boolean mIsCvcValid;
    
    public CvcEditText(Context context) {
        super(context);
        listenForTextChanges();
    }
    
    public CvcEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        listenForTextChanges();
    }
    
    public CvcEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    
    public String getCvc(){
        return getText().toString();
    }
    
    public void setCvcInputListener(CvcInputListener mCvcInputListener) {
        this.mCvcInputListeners.add(mCvcInputListener);
    }
    
    private void listenForTextChanges() {
        addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cvc = s.toString();
                mIsCvcValid = cvc.matches("[0-9]{3,4}");
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
     * @see #setCvcInputListener(CvcInputListener)
     */
    public interface CvcInputListener {
        void onCvcInputComplete();
    }
}
