package com.tranzzo.android.sdk;

import com.tranzzo.android.sdk.view.Card;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TranzzoTest {
    
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    
    @Mock
    private TranzzoApi api;
    
    @Mock
    private TelemetryProvider telemetryProvider;
    
    private Card validCard = new Card("4242424242424242", 12, 22, "000");
    
    private SortedMap<String, Object> requestParams = new TreeMap<>(validCard.toMap());
    
    private String token = "IAMTOKEN";
    private String exp = "2022-12-31T00:00:00";
    private String cardMask = "424242******4242";
    
    private String successJson = "{\n" +
            "  \"card_mask\": \"" + cardMask + "\",\n" +
            "  \"expires_at\": \"" + exp + "\",\n" +
            "  \"token\": \"" + token + "\"\n" +
            "}";
    
    private Tranzzo underTest;
    
    @Before
    public void setUp() {
        when(telemetryProvider.collect(RuntimeEnvironment.systemContext)).thenReturn(new HashMap<>());
        underTest = new Tranzzo("", api, telemetryProvider, new TestLogger());
    }
    
    @Test
    public void returnTokenForSuccessfulResponse() throws Exception {
        checkCase(
                true,
                successJson,
                (response, actual) -> {
                    assertTrue(actual.isSuccessful());
                    assertEquals(new CardToken(token, CardToken.DATE_TIME_PARSER.parse(exp), cardMask), actual.value);
                }
        
        );
    }
    
    @Test
    @SuppressWarnings("ConstantConditions")
    public void returnAnErrorForEmptyResponseJson() throws Exception {
        checkCase(
                false,
                "",
                (response, actual) -> {
                    assertFalse(actual.isSuccessful());
                    assertEquals(Tranzzo.OOPS_MESSAGE_INTERNAL, actual.error.message);
                }
        
        );
    }
    
    @Test
    @SuppressWarnings("ConstantConditions")
    public void returnAnErrorForInvalidResponseJson() throws Exception {
        checkCase(
                false,
                "{\"invalid\":\"json\"}",
                (response, actual) -> {
                    assertFalse(actual.isSuccessful());
                    assertEquals(Tranzzo.OOPS_MESSAGE_SERVER + "Failed to parse server response: {\"invalid\":\"json\"}", actual.error.message);
                }
        
        );
    }
    
    private void checkCase(
            final boolean requestSuccess,
            final String serverResponse,
            final ThrowableBiConsumer<String, Outcome<CardToken>> assertion
    ) throws Exception {
        when(api.tokenize(requestParams, "")).thenReturn(new TrzResponse(requestSuccess, serverResponse));
        Outcome<CardToken> actual = underTest.tokenize(validCard, RuntimeEnvironment.systemContext);
        assertion.accept(serverResponse, actual);
    }
    
    @FunctionalInterface
    interface ThrowableBiConsumer<T, U> {
        
        void accept(T t, U u) throws Exception;
        
    }
    
}