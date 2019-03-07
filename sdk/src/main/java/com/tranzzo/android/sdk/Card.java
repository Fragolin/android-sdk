package com.tranzzo.android.sdk;

public class Card {
    
    public final String number;
    public final int expMonth;
    public final int expYear;
    public final String cvv;
    
    public Card(String number, int expMonth, int expYear, String cvv) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CardX{");
        sb.append("number='").append(number).append('\'');
        sb.append(", expMonth=").append(expMonth);
        sb.append(", expYear=").append(expYear);
        sb.append(", cvv='").append(cvv).append('\'');
        sb.append('}');
        return sb.toString();
    }
    
}