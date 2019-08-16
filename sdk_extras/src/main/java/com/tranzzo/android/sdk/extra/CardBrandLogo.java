package com.tranzzo.android.sdk.extra;


import androidx.annotation.DrawableRes;
import com.tranzzo.android.sdk.view.CardBrand;

/**
 * Represents link between {@link CardBrand} and according logo Android resource.
 */
public enum CardBrandLogo {
    AMERICAN_EXPRESS(CardBrand.AMERICAN_EXPRESS, R.drawable.ic_amex),
    DISCOVER(CardBrand.DISCOVER, R.drawable.ic_discover),
    JCB(CardBrand.JCB, R.drawable.ic_jcb),
    DINERS_CLUB(CardBrand.DINERS_CLUB, R.drawable.ic_diners),
    VISA(CardBrand.VISA, R.drawable.ic_visa),
    MASTERCARD(CardBrand.MASTERCARD, R.drawable.ic_mastercard),
    UNION_PAY(CardBrand.UNION_PAY, R.drawable.ic_unionpay),
    UNKNOWN(CardBrand.UNKNOWN, R.drawable.ic_unknown);
    
    public final CardBrand brand;
    public final int img;
    
    /**
     * @param brand brand which owns this logo
     * @param img   drawable resource id for brand logo
     */
    CardBrandLogo(CardBrand brand, @DrawableRes int img) {
        this.brand = brand;
        this.img = img;
    }
    
    public static CardBrandLogo fromBrand(CardBrand brand) {
        for (CardBrandLogo logo : CardBrandLogo.values()) {
            if (logo.brand.equals(brand))
                return logo;
        }
        return CardBrandLogo.UNKNOWN;
    }
    
}
