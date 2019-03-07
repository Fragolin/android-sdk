package com.tranzzo.android.sdk.view;

import android.text.TextUtils;
import androidx.annotation.*;
import com.tranzzo.android.sdk.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;

/**
 * A model object representing a CardX in the Android SDK.
 */
public class CardX {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            AMERICAN_EXPRESS,
            DISCOVER,
            JCB,
            DINERS_CLUB,
            VISA,
            MASTERCARD,
            UNIONPAY,
            UNKNOWN
    })
    public @interface Brand { }
    public static final String AMERICAN_EXPRESS = "American Express";
    public static final String DISCOVER = "Discover";
    public static final String JCB = "JCB";
    public static final String DINERS_CLUB = "Diners Club";
    public static final String VISA = "Visa";
    public static final String MASTERCARD = "MasterCard";
    public static final String UNIONPAY = "UnionPay";
    public static final String UNKNOWN = "Unknown";

    public static final int CVC_LENGTH_AMERICAN_EXPRESS = 4;
    public static final int CVC_LENGTH_COMMON = 3;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            FUNDING_CREDIT,
            FUNDING_DEBIT,
            FUNDING_PREPAID,
            FUNDING_UNKNOWN
    })
    public @interface FundingType { }
    public static final String FUNDING_CREDIT = "credit";
    public static final String FUNDING_DEBIT = "debit";
    public static final String FUNDING_PREPAID = "prepaid";
    public static final String FUNDING_UNKNOWN = "unknown";

    public static final Map<String , Integer> BRAND_RESOURCE_MAP =
            new HashMap<String , Integer>() {{
                put(CardX.AMERICAN_EXPRESS, R.drawable.ic_amex);
                put(CardX.DINERS_CLUB, R.drawable.ic_diners);
                put(CardX.DISCOVER, R.drawable.ic_discover);
                put(CardX.JCB, R.drawable.ic_jcb);
                put(CardX.MASTERCARD, R.drawable.ic_mastercard);
                put(CardX.VISA, R.drawable.ic_visa);
                put(CardX.UNIONPAY, R.drawable.ic_unionpay);
                put(CardX.UNKNOWN, R.drawable.ic_unknown);
            }};

    // Based on http://en.wikipedia.org/wiki/Bank_card_number#Issuer_identification_number_.28IIN.29
    public static final String[] PREFIXES_AMERICAN_EXPRESS = {"34", "37"};
    public static final String[] PREFIXES_DISCOVER = {"60", "64", "65"};
    public static final String[] PREFIXES_JCB = {"35"};
    public static final String[] PREFIXES_DINERS_CLUB = {"300", "301", "302", "303", "304",
            "305", "309", "36", "38", "39"};
    public static final String[] PREFIXES_VISA = {"4"};
    public static final String[] PREFIXES_MASTERCARD = {
        "2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229",
        "223", "224", "225", "226", "227", "228", "229",
        "23", "24", "25", "26",
        "270", "271", "2720",
        "50", "51", "52", "53", "54", "55", "67"
    };
    public static final String[] PREFIXES_UNIONPAY = {"62"};

    public static final int MAX_LENGTH_STANDARD = 16;
    public static final int MAX_LENGTH_AMERICAN_EXPRESS = 15;
    public static final int MAX_LENGTH_DINERS_CLUB = 14;

    static final String VALUE_CARD = "card";

    private static final String FIELD_OBJECT = "object";
    private static final String FIELD_ADDRESS_CITY = "address_city";
    private static final String FIELD_ADDRESS_COUNTRY = "address_country";
    private static final String FIELD_ADDRESS_LINE1 = "address_line1";
    private static final String FIELD_ADDRESS_LINE1_CHECK = "address_line1_check";
    private static final String FIELD_ADDRESS_LINE2 = "address_line2";
    private static final String FIELD_ADDRESS_STATE = "address_state";
    private static final String FIELD_ADDRESS_ZIP = "address_zip";
    private static final String FIELD_ADDRESS_ZIP_CHECK = "address_zip_check";
    private static final String FIELD_BRAND = "brand";
    private static final String FIELD_COUNTRY = "country";
    private static final String FIELD_CURRENCY = "currency";
    private static final String FIELD_CUSTOMER = "customer";
    private static final String FIELD_CVC_CHECK = "cvc_check";
    private static final String FIELD_EXP_MONTH = "exp_month";
    private static final String FIELD_EXP_YEAR = "exp_year";
    private static final String FIELD_FINGERPRINT = "fingerprint";
    private static final String FIELD_FUNDING = "funding";
    private static final String FIELD_METADATA = "metadata";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_LAST4 = "last4";
    private static final String FIELD_ID = "id";
    private static final String FIELD_TOKENIZATION_METHOD = "tokenization_method";

    private String number;
    private String cvc;
    private Integer expMonth;
    private Integer expYear;
    private String name;
    private String addressLine1;
    private String addressLine1Check;
    private String addressLine2;
    private String addressCity;
    private String addressState;
    private String addressZip;
    private String addressZipCheck;
    private String addressCountry;
    @Size(4) private String last4;
    @Brand
    private String brand;
    @FundingType private String funding;
    private String fingerprint;
    private String country;
    private String currency;
    private String customerId;
    private String cvcCheck;
    private String id;
    @NonNull
    private List<String> loggingTokens = new ArrayList<>();
    @Nullable
    private String tokenizationMethod;
    @Nullable private Map<String, String> metadata;

    /**
     * Builder class for a {@link CardX} model.
     */
    public static class Builder {
        private final String number;
        private final String cvc;
        private final Integer expMonth;
        private final Integer expYear;
        private String name;
        private String addressLine1;
        private String addressLine1Check;
        private String addressLine2;
        private String addressCity;
        private String addressState;
        private String addressZip;
        private String addressZipCheck;
        private String addressCountry;
        private @Brand
        String brand;
        private @FundingType String funding;
        private @Size(4) String last4;
        private String fingerprint;
        private String country;
        private String currency;
        private String customer;
        private String cvcCheck;
        private String id;
        private String tokenizationMethod;
        private Map<String, String> metadata;

        /**
         * Constructor with most common {@link CardX} fields.
         *
         * @param number the credit card number
         * @param expMonth the expiry month, as an integer value between 1 and 12
         * @param expYear the expiry year
         * @param cvc the card CVC number
         */
        public Builder(
                String number,
                @IntRange(from = 1, to = 12) Integer expMonth,
                @IntRange(from = 0) Integer expYear,
                String cvc) {
            this.number = number;
            this.expMonth = expMonth;
            this.expYear = expYear;
            this.cvc = cvc;
        }

        @NonNull
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @NonNull
        public Builder addressLine1(String address) {
            this.addressLine1 = address;
            return this;
        }

        @NonNull
        public Builder addressLine1Check(String addressLine1Check) {
            this.addressLine1Check = addressLine1Check;
            return this;
        }

        @NonNull
        public Builder addressLine2(String address) {
            this.addressLine2 = address;
            return this;
        }

        @NonNull
        public Builder addressCity(String city) {
            this.addressCity = city;
            return this;
        }

        @NonNull
        public Builder addressState(String state) {
            this.addressState = state;
            return this;
        }

        @NonNull
        public Builder addressZip(String zip) {
            this.addressZip = zip;
            return this;
        }

        @NonNull
        public Builder addressZipCheck(String zipCheck) {
            this.addressZipCheck = zipCheck;
            return this;
        }

        @NonNull
        public Builder addressCountry(String country) {
            this.addressCountry = country;
            return this;
        }

        @NonNull
        public Builder brand(@Brand String brand) {
            this.brand = brand;
            return this;
        }

        @NonNull
        public Builder fingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
            return this;
        }

        @NonNull
        public Builder funding(@FundingType String funding) {
            this.funding = funding;
            return this;
        }

        @NonNull
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        @NonNull
        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        @NonNull
        public Builder customer(String customer) {
            this.customer = customer;
            return this;
        }

        @NonNull
        public Builder cvcCheck(String cvcCheck) {
            this.cvcCheck = cvcCheck;
            return this;
        }

        @NonNull
        public Builder last4(String last4) {
            this.last4 = last4;
            return this;
        }

        @NonNull
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        @NonNull
        public Builder tokenizationMethod(@Nullable String tokenizationMethod) {
            this.tokenizationMethod = tokenizationMethod;
            return this;
        }

        @NonNull
        public Builder metadata(@Nullable Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        /**
         * Generate a new {@link CardX} object based on the arguments held by this Builder.
         *
         * @return the newly created {@link CardX} object
         */
        public CardX build() {
            return new CardX(this);
        }
    }

    /**
     * Converts an unchecked String value to a {@link Brand} or {@code null}.
     *
     * @param possibleCardType a String that might match a {@link Brand} or be empty.
     * @return {@code null} if the input is blank, else the appropriate {@link Brand}.
     */
    @Nullable
    @Brand
    public static String asCardBrand(@Nullable String possibleCardType) {
        if (possibleCardType == null || TextUtils.isEmpty(possibleCardType.trim())) {
            return null;
        }

        if (CardX.AMERICAN_EXPRESS.equalsIgnoreCase(possibleCardType)) {
            return CardX.AMERICAN_EXPRESS;
        } else if (CardX.MASTERCARD.equalsIgnoreCase(possibleCardType)) {
            return CardX.MASTERCARD;
        } else if (CardX.DINERS_CLUB.equalsIgnoreCase(possibleCardType)) {
            return CardX.DINERS_CLUB;
        } else if (CardX.DISCOVER.equalsIgnoreCase(possibleCardType)) {
            return CardX.DISCOVER;
        } else if (CardX.JCB.equalsIgnoreCase(possibleCardType)) {
            return CardX.JCB;
        } else if (CardX.VISA.equalsIgnoreCase(possibleCardType)) {
            return CardX.VISA;
        } else if (CardX.UNIONPAY.equalsIgnoreCase(possibleCardType)) {
            return CardX.UNIONPAY;
        } else {
            return CardX.UNKNOWN;
        }
    }

    /**
     * Converts an unchecked String value to a {@link FundingType} or {@code null}.
     *
     * @param possibleFundingType a String that might match a {@link FundingType} or be empty
     * @return {@code null} if the input is blank, else the appropriate {@link FundingType}
     */
    @Nullable
    @FundingType
    public static String asFundingType(@Nullable String possibleFundingType) {
        if (possibleFundingType == null || TextUtils.isEmpty(possibleFundingType.trim())) {
            return null;
        }

        if (CardX.FUNDING_CREDIT.equalsIgnoreCase(possibleFundingType)) {
            return CardX.FUNDING_CREDIT;
        } else if (CardX.FUNDING_DEBIT.equalsIgnoreCase(possibleFundingType)) {
            return CardX.FUNDING_DEBIT;
        } else if (CardX.FUNDING_PREPAID.equalsIgnoreCase(possibleFundingType)) {
            return CardX.FUNDING_PREPAID;
        } else {
            return CardX.FUNDING_UNKNOWN;
        }
    }

    /**
     * Checks whether {@code this} represents a valid card.
     *
     * @return {@code true} if valid, {@code false} otherwise.
     */
    public boolean validateCard() {
        return validateCard(Calendar.getInstance());
    }

    /**
     * Checks whether or not the {@link #number} field is valid.
     *
     * @return {@code true} if valid, {@code false} otherwise.
     */
    public boolean validateNumber() {
        return CardUtils.isValidCardNumber(number);
    }

    /**
     * Checks whether or not the {@link #expMonth} and {@link #expYear} fields represent a valid
     * expiry date.
     *
     * @return {@code true} if valid, {@code false} otherwise
     */
    public boolean validateExpiryDate() {
        return validateExpiryDate(Calendar.getInstance());
    }

    /**
     * Checks whether or not the {@link #cvc} field is valid.
     *
     * @return {@code true} if valid, {@code false} otherwise
     */
    public boolean validateCVC() {
        if (TranzzoTextUtils.isBlank(cvc)) {
            return false;
        }
        String cvcValue = cvc.trim();
        String updatedType = getBrand();
        boolean validLength =
                (updatedType == null && cvcValue.length() >= 3 && cvcValue.length() <= 4)
                || (AMERICAN_EXPRESS.equals(updatedType) && cvcValue.length() == 4)
                || cvcValue.length() == 3;

        return ModelUtils.isWholePositiveNumber(cvcValue) && validLength;
    }

    /**
     * Checks whether or not the {@link #expMonth} field is valid.
     *
     * @return {@code true} if valid, {@code false} otherwise.
     */
    public boolean validateExpMonth() {
        return expMonth != null && expMonth >= 1 && expMonth <= 12;
    }

    /**
     * Checks whether or not the {@link #expYear} field is valid.
     *
     * @return {@code true} if valid, {@code false} otherwise.
     */
    boolean validateExpYear(Calendar now) {
        return expYear != null && !ModelUtils.hasYearPassed(expYear, now);
    }

    /**
     * @return the {@link #number} of this card
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the {@link List} of logging tokens associated with this {@link CardX} object
     */
    @NonNull
    public List<String> getLoggingTokens() {
        return loggingTokens;
    }

    /**
     * Add a logging token to this {@link CardX} object.
     *
     * @param loggingToken a token to be logged with this card
     * @return {@code this}, for chaining purposes
     */
    @NonNull
    public CardX addLoggingToken(@NonNull String loggingToken) {
        loggingTokens.add(loggingToken);
        return this;
    }

    /**
     * Setter for the card number. Note that mutating the number of this card object
     * invalidates the {@link #brand} and {@link #last4}.
     *
     * @param number the new {@link #number}
     */
    @Deprecated
    public void setNumber(String number) {
        this.number = number;
        this.brand = null;
        this.last4 = null;
    }

    /**
     * @return the {@link #cvc} for this card
     */
    public String getCVC() {
        return cvc;
    }

    /**
     * @param cvc the new {@link #cvc} code for this card
     */
    @Deprecated
    public void setCVC(String cvc) {
        this.cvc = cvc;
    }

    /**
     * @return the {@link #expMonth} for this card
     */
    @Nullable
    @IntRange(from = 1, to = 12)
    public Integer getExpMonth() {
        return expMonth;
    }

    /**
     * @param expMonth sets the {@link #expMonth} for this card
     */
    @Deprecated
    public void setExpMonth(@Nullable @IntRange(from = 1, to = 12) Integer expMonth) {
        this.expMonth = expMonth;
    }

    /**
     * @return the {@link #expYear} for this card
     */
    public Integer getExpYear() {
        return expYear;
    }

    /**
     * @param expYear sets the {@link #expYear} for this card
     */
    @Deprecated
    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    /**
     * @return the cardholder {@link #name} for this card
     */
    public String getName() {
        return name;
    }

    /**
     * @param name sets the cardholder {@link #name} for this card
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the {@link #addressLine1} of this card
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * @param addressLine1 sets the {@link #addressLine1} for this card
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     * @return the {@link #addressLine2} of this card
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * @param addressLine2 sets the {@link #addressLine2} for this card
     */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     * @return the {@link #addressCity} for this card
     */
    public String getAddressCity() {
        return addressCity;
    }

    /**
     * @param addressCity sets the {@link #addressCity} for this card
     */
    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    /**
     * @return the {@link #addressZip} of this card
     */
    public String getAddressZip() {
        return addressZip;
    }

    /**
     * @param addressZip sets the {@link #addressZip} for this card
     */
    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    /**
     * @return the {@link #addressState} of this card
     */
    public String getAddressState() {
        return addressState;
    }

    /**
     * @param addressState sets the {@link #addressState} for this card
     */
    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    /**
     * @return the {@link #addressCountry} of this card
     */
    public String getAddressCountry() {
        return addressCountry;
    }

    /**
     * @param addressCountry sets the {@link #addressCountry} for this card
     */
    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    /**
     * @return the {@link #currency} of this card. Only supported for Managed accounts.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency sets the {@link #currency} of this card. Only supported for Managed accounts.
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the {@link #metadata} of this card
     */
    @Nullable
    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    /**
     * @param metadata {@link #metadata} for this card
     */
    public void setMetadata(@Nullable Map<String, String> metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the {@link #last4} digits of this card. Sets the value based on the {@link #number}
     * if it has not already been set.
     */
    public String getLast4() {
        if (!TranzzoTextUtils.isBlank(last4)) {
            return last4;
        }

        if (number != null && number.length() > 4) {
            last4 = number.substring(number.length() - 4);
            return last4;
        }

        return null;
    }

    /**
     * Gets the {@link #brand} of this card, changed from the "type" field. Use {@link #getBrand()}
     * instead.
     *
     * @return the {@link #brand} of this card
     */
    @Deprecated
    @Brand
    public String getType() {
        return getBrand();
    }

    /**
     * Gets the {@link #brand} of this card. Updates the value if none has yet been set, or
     * if the {@link #number} has been changed.
     *
     * @return the {@link #brand} of this card
     */
    @Brand
    public String getBrand() {
        if (TranzzoTextUtils.isBlank(brand) && !TranzzoTextUtils.isBlank(number)) {
            brand = CardUtils.getPossibleCardType(number);
        }

        return brand;
    }

    /**
     * @return the {@link #fingerprint} of this card
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * @return the {@link #funding} type of this card
     */
    @Nullable
    @FundingType
    public String getFunding() {
        return funding;
    }

    /**
     * @return the {@link #country} of this card
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return If address_line1 was provided, results of the check:
     * pass, fail, unavailable, or unchecked.
     */
    @Nullable
    public String getAddressLine1Check() {
        return addressLine1Check;
    }

    /**
     * @return If address_zip was provided, results of the check:
     * pass, fail, unavailable, or unchecked.
     */
    @Nullable
    public String getAddressZipCheck() {
        return addressZipCheck;
    }

    /**
     * @return The ID of the customer that this card belongs to.
     */
    @Nullable
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @return If a CVC was provided, results of the check:
     * pass, fail, unavailable, or unchecked.
     */
    @Nullable
    public String getCvcCheck() {
        return cvcCheck;
    }

    @Nullable
    String getTokenizationMethod() {
        return this.tokenizationMethod;
    }

    boolean validateCard(@NonNull Calendar now) {
        if (cvc == null) {
            return validateNumber() && validateExpiryDate(now);
        } else {
            return validateNumber() && validateExpiryDate(now) && validateCVC();
        }
    }

    boolean validateExpiryDate(Calendar now) {
        if (!validateExpMonth()) {
            return false;
        }
        if (!validateExpYear(now)) {
            return false;
        }
        return !ModelUtils.hasMonthPassed(expYear, expMonth, now);
    }

    private CardX(Builder builder) {
        this.number = TranzzoTextUtils.nullIfBlank(normalizeCardNumber(builder.number));
        this.expMonth = builder.expMonth;
        this.expYear = builder.expYear;
        this.cvc = TranzzoTextUtils.nullIfBlank(builder.cvc);
        this.name = TranzzoTextUtils.nullIfBlank(builder.name);
        this.addressLine1 = TranzzoTextUtils.nullIfBlank(builder.addressLine1);
        this.addressLine1Check = TranzzoTextUtils.nullIfBlank(builder.addressLine1Check);
        this.addressLine2 = TranzzoTextUtils.nullIfBlank(builder.addressLine2);
        this.addressCity = TranzzoTextUtils.nullIfBlank(builder.addressCity);
        this.addressState = TranzzoTextUtils.nullIfBlank(builder.addressState);
        this.addressZip = TranzzoTextUtils.nullIfBlank(builder.addressZip);
        this.addressZipCheck = TranzzoTextUtils.nullIfBlank(builder.addressZipCheck);
        this.addressCountry = TranzzoTextUtils.nullIfBlank(builder.addressCountry);
        this.last4 = TranzzoTextUtils.nullIfBlank(builder.last4) == null
                ? getLast4()
                : builder.last4;
        this.brand = asCardBrand(builder.brand) == null
                ? getBrand()
                : builder.brand;
        this.fingerprint = TranzzoTextUtils.nullIfBlank(builder.fingerprint);
        this.funding = asFundingType(builder.funding);
        this.country = TranzzoTextUtils.nullIfBlank(builder.country);
        this.currency = TranzzoTextUtils.nullIfBlank(builder.currency);
        this.customerId = TranzzoTextUtils.nullIfBlank(builder.customer);
        this.cvcCheck = TranzzoTextUtils.nullIfBlank(builder.cvcCheck);
        this.id = TranzzoTextUtils.nullIfBlank(builder.id);
        this.tokenizationMethod = TranzzoTextUtils.nullIfBlank(builder.tokenizationMethod);
        this.metadata = builder.metadata;
    }

    @Nullable
    private String normalizeCardNumber(@Nullable String number) {
        if (number == null) {
            return null;
        }
        return number.trim().replaceAll("\\s+|-", "");
    }
}
