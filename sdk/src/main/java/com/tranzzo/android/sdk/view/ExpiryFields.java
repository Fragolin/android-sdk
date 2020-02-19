package com.tranzzo.android.sdk.view;

import androidx.annotation.*;

public class ExpiryFields {
    
    @IntRange(from = 1, to = 12)
    final int expMonth;
    
    
    @IntRange(/*from = ${CURRENT_YEAR}*/ to = 99)
    final int expYear;
    
    public ExpiryFields(int expMonth, int expYear) {
        this.expMonth = expMonth;
        this.expYear = expYear;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ExpiryFields that = (ExpiryFields) o;

        if (expMonth != that.expMonth) return false;
        return expYear == that.expYear;
    }
    
    @Override
    public int hashCode() {
        int result = expMonth;
        result = 31 * result + expYear;
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpiryFields{");
        sb.append("expMonth=").append(expMonth);
        sb.append(", expYear=").append(expYear);
        sb.append('}');
        return sb.toString();
    }

    public int getExpMonth() {
        return expMonth;
    }

    public int getExpYear() {
        return expYear;
    }
}
