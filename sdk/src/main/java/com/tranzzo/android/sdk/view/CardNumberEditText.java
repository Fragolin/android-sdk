package com.tranzzo.android.sdk.view;

import android.content.Context;
import android.text.*;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import androidx.annotation.*;
import com.tranzzo.android.sdk.R;

import java.util.*;

/**
 * An {@link EditText} that handles spacing out the digits of a credit card.
 */
public class CardNumberEditText extends TranzzoEditText {
    
    private static final int MAX_LENGTH_COMMON = 19;
    // Note that AmEx and Diners Club have the same length
    // because Diners Club has one more space, but one less digit.
    private static final int MAX_LENGTH_AMEX_DINERS = 17;
    
    private static final Integer[] SPACES_ARRAY_COMMON = {4, 9, 14};
    private static final Set<Integer> SPACE_SET_COMMON =
            new HashSet<>(Arrays.asList(SPACES_ARRAY_COMMON));
    
    private static final Integer[] SPACES_ARRAY_AMEX = {4, 11};
    private static final Set<Integer> SPACE_SET_AMEX =
            new HashSet<>(Arrays.asList(SPACES_ARRAY_AMEX));
    
    private CardBrand mCardBrand = CardBrand.UNKNOWN;
    private List<CardBrandChangeListener> mCardBrandChangeListeners = new ArrayList<>();
    private List<CardNumberCompleteListener> mCardNumberCompleteListeners = new ArrayList<>();
    private int mLengthMax = 19;
    private boolean mIgnoreChanges = false;
    private boolean mIsCardNumberValid = false;
    
    public CardNumberEditText(Context context) {
        super(context);
        listenForTextChanges();
    }
    
    public CardNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        listenForTextChanges();
    }
    
    public CardNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        listenForTextChanges();
    }
    
    @NonNull
    public CardBrand getCardBrand() {
        return mCardBrand;
    }
    
    /**
     * Gets a usable form of the card number. If the text is "4242 4242 4242 4242", this
     * method will return "4242424242424242". If the card number is invalid, this returns
     * {@code null}.
     *
     * @return a space-free version of the card number, or {@code null} if the number is invalid
     */
    @Nullable
    public String getCardNumber() {
        return mIsCardNumberValid
                ? TranzzoTextUtils.removeSpacesAndHyphens(getText().toString())
                : null;
    }
    
    public int getLengthMax() {
        return mLengthMax;
    }
    
    
    @Override
    public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        String accLabel = getResources().getString(
                R.string.acc_label_card_number_node,
                getText());
        info.setText(accLabel);
    }
    
    /**
     * Check whether or not the card number is valid
     *
     * @return the value of {@link #mIsCardNumberValid}
     */
    public boolean isCardNumberValid() {
        return mIsCardNumberValid;
    }
    
    public void addCardNumberCompleteListener(@NonNull CardNumberCompleteListener listener) {
        mCardNumberCompleteListeners.add(listener);
    }
    
    public void addCardBrandChangeListener(@NonNull CardBrandChangeListener listener) {
        mCardBrandChangeListeners.add(listener);
        // Immediately display the brand if known, in case this method is invoked when
        // partial data already exists.
        listener.onCardBrandChanged(mCardBrand);
    }
    
    /**
     * Updates the selection index based on the current (pre-edit) index, and
     * the size change of the number being input.
     *
     * @param newLength          the post-edit length of the string
     * @param editActionStart    the position in the string at which the edit action starts
     * @param editActionAddition the number of new characters going into the string (zero for
     *                           delete)
     * @return an index within the string at which to put the cursor
     */
    @VisibleForTesting
    int updateSelectionIndex(
            int newLength,
            int editActionStart,
            int editActionAddition) {
        int newPosition, gapsJumped = 0;
        Set<Integer> gapSet = mCardBrand == CardBrand.AMERICAN_EXPRESS
                ? SPACE_SET_AMEX
                : SPACE_SET_COMMON;
        boolean skipBack = false;
        for (Integer gap : gapSet) {
            if (editActionStart <= gap && editActionStart + editActionAddition > gap) {
                gapsJumped++;
            }
            
            // editActionAddition can only be 0 if we are deleting,
            // so we need to check whether or not to skip backwards one space
            if (editActionAddition == 0 && editActionStart == gap + 1) {
                skipBack = true;
            }
        }
        
        newPosition = editActionStart + editActionAddition + gapsJumped;
        if (skipBack && newPosition > 0) {
            newPosition--;
        }
        
        return newPosition <= newLength ? newPosition : newLength;
    }
    
    private void listenForTextChanges() {
        addTextChangedListener(new TextWatcher() {
            int latestChangeStart;
            int latestInsertionSize;
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!mIgnoreChanges) {
                    latestChangeStart = start;
                    latestInsertionSize = after;
                }
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mIgnoreChanges) {
                    return;
                }
                
                if (start < 4) {
                    updateCardBrandFromNumber(s.toString());
                }
                
                if (start > 16) {
                    // no need to do formatting if we're past all of the spaces.
                    return;
                }
                
                String spacelessNumber = TranzzoTextUtils.removeSpacesAndHyphens(s.toString());
                if (spacelessNumber == null) {
                    return;
                }
                
                String[] cardParts = ViewUtils.separateCardNumberGroups(
                        spacelessNumber, mCardBrand);
                StringBuilder formattedNumberBuilder = new StringBuilder();
                for (int i = 0; i < cardParts.length; i++) {
                    if (cardParts[i] == null) {
                        break;
                    }
                    
                    if (i != 0) {
                        formattedNumberBuilder.append(' ');
                    }
                    formattedNumberBuilder.append(cardParts[i]);
                }
                
                String formattedNumber = formattedNumberBuilder.toString();
                int cursorPosition = updateSelectionIndex(
                        formattedNumber.length(),
                        latestChangeStart,
                        latestInsertionSize);
                
                mIgnoreChanges = true;
                setText(formattedNumber);
                setSelection(cursorPosition);
                mIgnoreChanges = false;
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == mLengthMax) {
                    boolean before = mIsCardNumberValid;
                    mIsCardNumberValid = CardUtils.isValidCardNumber(s.toString());
                    setShouldShowError(!mIsCardNumberValid);
                    if (!before && mIsCardNumberValid) {
                        for (CardNumberCompleteListener listener : mCardNumberCompleteListeners) {
                            listener.onCardNumberComplete();
                        }
                    }
                } else {
                    mIsCardNumberValid = getText() != null
                            && CardUtils.isValidCardNumber(getText().toString());
                    // Don't show errors if we aren't full-length.
                    setShouldShowError(false);
                }
            }
        });
    }
    
    private void updateCardBrand(@NonNull CardBrand brand) {
        if (mCardBrand.equals(brand)) {
            return;
        }
        
        mCardBrand = brand;
    
        for (CardBrandChangeListener listener : mCardBrandChangeListeners) {
            listener.onCardBrandChanged(mCardBrand);
        }
        
        int oldLength = mLengthMax;
        mLengthMax = getLengthForBrand(mCardBrand);
        if (oldLength == mLengthMax) {
            return;
        }
        
        updateLengthFilter();
    }
    
    private void updateLengthFilter() {
        setFilters(new InputFilter[] {new InputFilter.LengthFilter(mLengthMax)});
    }
    
    private void updateCardBrandFromNumber(String partialNumber) {
        updateCardBrand(CardBrand.fromNumber(partialNumber));
    }
    
    private static int getLengthForBrand(CardBrand cardBrand) {
        if (cardBrand == CardBrand.AMERICAN_EXPRESS || cardBrand == CardBrand.DINERS_CLUB) {
            return MAX_LENGTH_AMEX_DINERS;
        } else {
            return MAX_LENGTH_COMMON;
        }
    }
    
    /**
     * This listener is triggered on card number input completion.
     *
     * @note event is triggered only for valid cards.
     * @see #addCardNumberCompleteListener(CardNumberCompleteListener)
     */
    public interface CardNumberCompleteListener {
        void onCardNumberComplete();
    }
    
    /**
     * Listener for card brand changes, e.g. for displaying card logo.
     *
     * @see #addCardBrandChangeListener(CardBrandChangeListener)
     */
    public interface CardBrandChangeListener {
        void onCardBrandChanged(CardBrand brand);
    }
    
}
