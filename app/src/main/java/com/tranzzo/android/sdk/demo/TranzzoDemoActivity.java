package com.tranzzo.android.sdk.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.tranzzo.android.sdk.*;
import com.tranzzo.android.sdk.extra.CardBrandLogo;
import com.tranzzo.android.sdk.util.Either;
import com.tranzzo.android.sdk.view.*;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TranzzoDemoActivity extends AppCompatActivity {
    
    @BindView(R.id.tvResult)
    TextView tvResult;
    
    @BindView(R.id.tvBrand)
    TextView tvBrand;
    
    @BindView(R.id.btnTokenize)
    Button btnTokenize;
    
    @BindView(R.id.btnFillDefault)
    Button btnFillInDefault;
    
    @BindView(R.id.btnFillWrong)
    Button btnFillInWrong;
    
    @BindView(R.id.btnClearInputs)
    Button btnClear;
    
    @BindView(R.id.btnCheckFormValid)
    Button btnCheckFormValid;
    
    @BindView(R.id.imgBrand)
    ImageView imgBrand;
    
    @BindView(R.id.etCardNumber)
    CardNumberEditText etCardNumber;
    
    @BindView(R.id.etExpiration)
    ExpiryDateEditText etExpiration;
    
    @BindView(R.id.swEnv)
    SwitchCompat swEnv;
    
    @BindView(R.id.etCvc)
    CvcEditText etCvc;
    
    private TranzzoInputListener cardInputListener;
    private AtomicBoolean isStage = new AtomicBoolean(true);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        
        swEnv.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isStage.set(!isChecked);
            if (isChecked)
                buttonView.setText("Prod");
            else
                buttonView.setText("Stage");
        });
        
        btnTokenize.setOnClickListener(v -> {
            if (cardInputListener.isFormValid()) {
                collectCard().consume(
                        this::displayError,
                        c -> new TokenizeTask().execute(c)
                );
            } else {
                displayError(TrzError.mkInternal("Something is invalid"));
            }
        });
        btnTokenize.setEnabled(false);
        
        btnFillInDefault.setOnClickListener(v -> {
            etCardNumber.setText("4242424242424242");
            etExpiration.setText("12/22");
            etCvc.setText("123");
        });
        
        btnFillInWrong.setOnClickListener(v -> {
            etCardNumber.setText("1111111111111111");
            etExpiration.setText("12/22");
            etCvc.setText("123");
        });
        
        btnClear.setOnClickListener(v -> {
            etCardNumber.setText("");
            etExpiration.setText("");
            etCvc.setText("");
            
            Toast
                    .makeText(getApplicationContext(), "Card input completed", Toast.LENGTH_LONG)
                    .show();
            
            btnTokenize.setEnabled(false);
        });
        
        etCardNumber.setErrorColor(getResources().getColor(R.color.colorRed));
        etCardNumber.addCardBrandChangeListener(brand -> {
            tvBrand.setText(brand.toString());
            imgBrand.setImageResource(CardBrandLogo.fromBrand(brand).img);
        });
        etCardNumber.addCardNumberCompleteListener(() -> Toast
                .makeText(getApplicationContext(), "Card input completed", Toast.LENGTH_SHORT)
                .show());
        
        etExpiration.setErrorColor(getResources().getColor(R.color.colorRed));
        etExpiration.addExpiryDateEditListener(() -> Toast
                .makeText(getApplicationContext(), "Expiry completed", Toast.LENGTH_SHORT)
                .show());
        
        etCvc.addCvcInputListener(() -> Toast
                .makeText(getApplicationContext(), "CVC completed", Toast.LENGTH_SHORT)
                .show());
        
        cardInputListener = new TranzzoInputListener(
                etCardNumber,
                etExpiration,
                etCvc,
                () -> btnTokenize.setEnabled(true)
        );
        
        btnCheckFormValid.setOnClickListener(v -> {
            if (cardInputListener.isFormValid()) {
                displayResult("VALID");
            } else {
                displayError(TrzError.mkInternal("INVALID"));
            }
        });
        
    }
    
    private Either<TrzError, Card> collectCard() {
        return etCardNumber
                .getCardNumber()
                .flatMap(cardNumber ->
                        etExpiration
                                .getValidDateFields()
                                .flatMap(expiry ->
                                        etCvc
                                                .getCvc()
                                                .map(cvc -> new Card(cardNumber, expiry, cvc))
                                )
                )
                .mapLeft(TrzError::mkInternal);
    }
    
    private void displayError(TrzError error) {
        tvResult.setTextColor(getResources().getColor(R.color.colorRed));
        tvResult.setText(error.message);
    }
    
    private void displayResult(String text) {
        tvResult.setTextColor(getResources().getColor(R.color.colorGreen));
        tvResult.setText(text);
    }
    
    private class TokenizeTask extends AsyncTask<Card, Void, Either<TrzError, CardToken>> {
        
        @Override
        protected Either<TrzError, CardToken> doInBackground(Card... cards) {
            return initTranzzo().tokenize(cards[0], getApplicationContext());
        }
        
        private Tranzzo initTranzzo(){
            if (isStage.get()){
                return TranzzoTestBridge.init("m03z1jKTSO6zUYQN5C8xYZnIclK0plIQ/3YMgTZbV6g7kxle6ZnCaHVNv3A11UCK", BuildConfig.TRANZZO_STAGE);
            } else {
                return TranzzoTestBridge.init("Qlvd8Q31SkOBXWfXv9dgdpsYlRuTvMS/a12Dk55RG01d5ngjaxTDao8QOudvmBGu", BuildConfig.TRANZZO_PROD);
            }
        }
        
        @Override
        protected void onPostExecute(Either<TrzError, CardToken> result) {
            result.consume(
                    TranzzoDemoActivity.this::displayError,
                    token -> {
                        displayResult(token.toString());
                        Log.i("TOKEN", ">>> " + token.toString());
                    });
        }
    }
    
}
