package com.tranzzo.android.sdk.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.tranzzo.android.sdk.Card;
import com.tranzzo.android.sdk.*;
import com.tranzzo.android.sdk.view.*;

public class MainActivity extends AppCompatActivity {
    
    private TextView tvResult;
    private Button btnTokenize;
    private Button btnFillInDefault;
    private Button btnFillInWrong;
    private Button btnClear;
    
    private CardNumberEditText etCardNumber;
    private ExpiryDateEditText etExpiration;
    private TranzzoEditText etCvv;
    
    private Card collectCard(){
        return new Card(
                etCardNumber.getCardNumber(),
                etExpiration.getValidDateFields()[0],
                etExpiration.getValidDateFields()[1],
                etCvv.getText().toString()
        );
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnTokenize = findViewById(R.id.btnTokenize);
        
        btnTokenize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                boolean valid = etCardNumber.isCardNumberValid() &&
                        etExpiration.isDateValid() &&
                        etCvv.length() == 3;
                
                if (valid){
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
        
        etExpiration = findViewById(R.id.etExpiration);
        etExpiration.setErrorColor(getResources().getColor(R.color.colorRed));
    
        etCvv = findViewById(R.id.etCVV);
    
        tvResult = findViewById(R.id.tvResult);
        
    }
    
    private void displayError(String text){
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
            if (cardToken.isSuccessful()){
                tvResult.setTextColor(getResources().getColor(R.color.colorGreen));
                tvResult.setText(cardToken.token.toString());
            } else {
                displayError(cardToken.error.toString());
            }
        }
    }
    
}
