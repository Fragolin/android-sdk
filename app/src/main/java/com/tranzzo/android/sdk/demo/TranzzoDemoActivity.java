package com.tranzzo.android.sdk.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.tranzzo.android.sdk.TokenResult;
import com.tranzzo.android.sdk.Tranzzo;
import com.tranzzo.android.sdk.view.*;

public class TranzzoDemoActivity extends AppCompatActivity {
    
    private TextView tvResult;
    private TextView tvBrand;
    private Button btnTokenize;
    private Button btnFillInDefault;
    private Button btnFillInWrong;
    private Button btnClear;
    private ImageView imgBrand;
    
    private CardNumberEditText etCardNumber;
    private ExpiryDateEditText etExpiration;
    private CvcEditText etCvv;
    
    private Card collectCard() {
        return
                new Card(
                        etCardNumber.getCardNumber(),
                        etExpiration.getValidDateFields()[0],
                        etExpiration.getValidDateFields()[1],
                        etCvv.getCvc()
                );
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    
        tvResult = findViewById(R.id.tvResult);
        tvBrand = findViewById(R.id.tvBrand);
        imgBrand = findViewById(R.id.imgBrand);
        
        btnTokenize = findViewById(R.id.btnTokenize);
        
        btnTokenize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                boolean valid = etCardNumber.isCardNumberValid() &&
                        etExpiration.isDateValid() &&
                        etCvv.isCvcValid();
                
                if (valid) {
                    new TokenizeTask().execute(collectCard());
                } else {
                    displayError("Something is invalid");
                }
            }
        });
        
        btnFillInDefault = findViewById(R.id.btnFillDefault);
        
        btnFillInDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCardNumber.setText("4242424242424242");
                etExpiration.setText("12/22");
                etCvv.setText("123");
            }
        });
        
        btnFillInWrong = findViewById(R.id.btnFillWrong);
        
        btnFillInWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCardNumber.setText("1111111111111111");
                etExpiration.setText("12/22");
                etCvv.setText("123");
            }
        });
        
        btnClear = findViewById(R.id.btnClearInputs);
        
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCardNumber.setText("");
                etExpiration.setText("");
                etCvv.setText("");
            }
        });
        
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardNumber.setErrorColor(getResources().getColor(R.color.colorRed));
        etCardNumber.setCardBrandChangeListener(new CardNumberEditText.CardBrandChangeListener() {
            @Override
            public void onCardBrandChanged(CardBrand brand) {
                tvBrand.setText(brand.toString());
                imgBrand.setImageResource(brand.img);
            }
        });
        etCardNumber.setCardNumberCompleteListener(new CardNumberEditText.CardNumberCompleteListener() {
            @Override
            public void onCardNumberComplete() {
                Toast
                        .makeText(getApplicationContext(), "Card input completed", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        
        etExpiration = findViewById(R.id.etExpiration);
        etExpiration.setErrorColor(getResources().getColor(R.color.colorRed));
        etExpiration.setExpiryDateEditListener(new ExpiryDateEditText.ExpiryDateEditListener() {
            @Override
            public void onExpiryDateComplete() {
                Toast
                        .makeText(getApplicationContext(), "Expiry completed", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        
        etCvv = findViewById(R.id.etCVV);
        etCvv.setCvcInputListener(new CvcEditText.CvcInputListener() {
            @Override
            public void onCvcInputComplete() {
                Toast
                        .makeText(getApplicationContext(), "CVC completed", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        
    }
    
    private void displayError(String text) {
        tvResult.setTextColor(getResources().getColor(R.color.colorRed));
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
                tvResult.setTextColor(getResources().getColor(R.color.colorGreen));
                tvResult.setText(cardToken.token.toString());
    
                Log.i("TOKEN", ">>> " + cardToken.token.toString());
            } else {
                displayError(cardToken.error.toString());
            }
        }
    }
    
}
