package com.tranzzo.android.sdk.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tranzzo.android.sdk.TokenResult;
import com.tranzzo.android.sdk.Tranzzo;
import com.tranzzo.android.sdk.view.*;

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
    
    @BindView(R.id.etCvc)
    CvcEditText etCvc;
    
    private TranzzoInputListener cardInputListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        
        btnTokenize.setOnClickListener(v -> {
            if (cardInputListener.isFormValid()) {
                new TokenizeTask().execute(collectCard());
            } else {
                displayError("Something is invalid");
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
            imgBrand.setImageResource(brand.img);
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
                displayError("INVALID");
            }
        });
        
    }
    
    private Card collectCard() {
        return
                new Card(
                        etCardNumber.getCardNumber(),
                        etExpiration.getValidDateFields(),
                        etCvc.getCvc()
                );
    }
    
    private void displayError(String text) {
        tvResult.setTextColor(getResources().getColor(R.color.colorRed));
        tvResult.setText(text);
    }
    
    private void displayResult(String text) {
        tvResult.setTextColor(getResources().getColor(R.color.colorGreen));
        tvResult.setText(text);
    }
    
    private class TokenizeTask extends AsyncTask<Card, Void, TokenResult> {
        
        @Override
        protected TokenResult doInBackground(Card... cards) {
            Tranzzo trz = Tranzzo.init("m03z1jKTSO6zUYQN5C8xYZnIclK0plIQ/3YMgTZbV6g7kxle6ZnCaHVNv3A11UCK");
            return trz.tokenize(cards[0], getApplicationContext());
        }
        
        @Override
        protected void onPostExecute(TokenResult cardToken) {
            if (cardToken.isSuccessful()) {
                displayResult(cardToken.token.toString());
                
                Log.i("TOKEN", ">>> " + cardToken.token.toString());
            } else {
                displayError(cardToken.error.toString());
            }
        }
    }
    
}
