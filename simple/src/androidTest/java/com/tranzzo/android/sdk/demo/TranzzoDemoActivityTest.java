package com.tranzzo.android.sdk.demo;

import android.view.View;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.tranzzo.android.sdk.view.CardUtils;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static com.tranzzo.android.sdk.demo.EspressoTestsMatchers.withDrawable;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TranzzoDemoActivityTest {
    
    @Rule
    public ActivityTestRule<TranzzoDemoActivity> activityRule
            = new ActivityTestRule<>(TranzzoDemoActivity.class);
    
    String visaRaw = "4242 4242 4242 4242";
    String visa = CardUtils.normalizeCardNumber(visaRaw);
    
    String masterCardRaw = "5555 5555 5555 4444";
    String masterCard = CardUtils.normalizeCardNumber(masterCardRaw);
    
    String unknownRaw = "1111 1111 1111 1111";
    String unknown = CardUtils.normalizeCardNumber(unknownRaw);
    
    @Test
    public void propertyFormatCardNumber_visa() {
        onView(withId(R.id.etCardNumber)).perform(typeText(visa));
        onView(withId(R.id.etCardNumber))
                .check(matches(withText(visaRaw)));
    }
    
    @Test
    public void pasteAndFormatCardNumber_masterCard() {
        onView(withId(R.id.etCardNumber)).perform(typeText(masterCard));
        onView(withId(R.id.etCardNumber))
                .check(matches(withText(masterCardRaw)));
    }
    
    @Test
    public void pasteAndFormatCardNumber_unknown() {
        onView(withId(R.id.etCardNumber)).perform(typeText(unknown));
        onView(withId(R.id.etCardNumber))
                .check(matches(withText(unknownRaw)));
    }
    
    @Test
    public void changeCardBrandLogo_visa() {
        onView(withId(R.id.etCardNumber)).perform(typeText(visa));
        onView(withId(R.id.imgBrand))
                .check(matches(withDrawable(R.drawable.ic_visa)));
    }
    
    @Test
    public void changeCardBrandLogo_unknown() {
        onView(withId(R.id.etCardNumber)).perform(typeText(unknown));
        onView(withId(R.id.imgBrand))
                .check(matches(withDrawable(R.drawable.ic_unknown)));
    }
    
    @Test
    public void changeCardBrandLogo_masterCard() {
        onView(withId(R.id.etCardNumber)).perform(typeText(masterCard));
        onView(withId(R.id.imgBrand))
                .check(matches(withDrawable(R.drawable.ic_mastercard)));
    }
    
    @Test
    public void properlyTokenizeValidCard() {
        onView(withId(R.id.btnFillDefault)).perform(click());
        onView(withId(R.id.btnTokenize)).perform(click());
    
        onView(withId(R.id.tvResult))
                .check(matches(withText(startsWith("CardToken"))));
    }
    
    @Test
    public void rejectToTokenizeInvalidCard_number() {
        onView(withId(R.id.btnFillWrong)).perform(click());
    
        onView(withId(R.id.btnTokenize)).check(matches(isDisabled()));
    }
    
    @Test
    public void rejectToTokenizeInvalidCard_expiryYear() {
        onView(withId(R.id.btnFillWrong)).perform(click());
    
        onView(withId(R.id.etExpiration))
                .perform(clearText())
                .perform(typeText("11/10"));
    
        onView(withId(R.id.btnTokenize)).check(matches(isDisabled()));
    }
    
    @Test
    public void rejectToTokenizeInvalidCard_expiryMonth() {
        onView(withId(R.id.btnFillWrong)).perform(click());
    
        onView(withId(R.id.etExpiration))
                .perform(clearText())
                .perform(typeText("13/10"));
        
        onView(withId(R.id.btnTokenize)).check(matches(isDisabled()));
    }
    
    private Matcher<View> isDisabled(){
        return not(isEnabled());
    }
    
    
}
