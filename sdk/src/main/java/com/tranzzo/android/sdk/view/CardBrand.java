package com.tranzzo.android.sdk.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Represents well-known card brands and common properties for them.
 */
public enum CardBrand {
    AMERICAN_EXPRESS("American Express", 15, 4, "34", "37"),
    DISCOVER("Discover", 19, 3, "60", "64", "65"),
    JCB("JCB", 19, 3, "35"),
    DINERS_CLUB("Diners Club", 19, 3, "300", "301", "302", "303", "304", "305", "309", "36", "38", "39"),
    VISA("Visa", 16, 3, "4"),
    MASTERCARD("MasterCard", 16, 3,
            "2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229",
            "223", "224", "225", "226", "227", "228", "229",
            "23", "24", "25", "26",
            "270", "271", "2720",
            "50", "51", "52", "53", "54", "55", "67"
    ),
    UNION_PAY("UnionPay", 16, 3, "62"),
    UNKNOWN("Unknown", 16, 3);
    
    public final String name;
    public final int maxLength;
    public final int cvvLength;
    public final String[] prefixes;
    
    /**
     * @param name      brand name
     * @param maxLength maximum card length
     * @param cvvLength maximum cvv length
     * @param prefixes  prefixes, applicable for this brand
     */
    CardBrand(String name, int maxLength, int cvvLength, final String... prefixes) {
        this.name = name;
        this.maxLength = maxLength;
        this.cvvLength = cvvLength;
        this.prefixes = prefixes;
    }
    
    @NonNull
    public static CardBrand fromNumber(@Nullable String rawNumber) {
        if (TranzzoTextUtils.isBlank(rawNumber)) {
            return UNKNOWN;
        }
        
        String number = TranzzoTextUtils.removeSpacesAndHyphens(rawNumber);
        
        //@formatter:off
             if (TranzzoTextUtils.hasAnyPrefix(number, AMERICAN_EXPRESS.prefixes)) return AMERICAN_EXPRESS;
        else if (TranzzoTextUtils.hasAnyPrefix(number, DISCOVER.prefixes))         return DISCOVER;
        else if (TranzzoTextUtils.hasAnyPrefix(number, JCB.prefixes))              return JCB;
        else if (TranzzoTextUtils.hasAnyPrefix(number, DINERS_CLUB.prefixes))      return DINERS_CLUB;
        else if (TranzzoTextUtils.hasAnyPrefix(number, VISA.prefixes))             return VISA;
        else if (TranzzoTextUtils.hasAnyPrefix(number, MASTERCARD.prefixes))       return MASTERCARD;
        else if (TranzzoTextUtils.hasAnyPrefix(number, UNION_PAY.prefixes))        return UNION_PAY;
        else return UNKNOWN;
        //@formatter:on
    }
    
}
