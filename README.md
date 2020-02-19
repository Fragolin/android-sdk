# Tranzzo Android SDK
![](https://img.shields.io/badge/platform-android-brightgreen)
[![](https://jitpack.io/v/tranzzo/android-sdk.svg)](https://jitpack.io/#tranzzo/android-sdk)

## Requirements
minSdkVersion 16+

## Installation
### Gradle
```
dependencies {
      implementation 'com.github.tranzzo:android-sdk:$version'
}
  ```
### Maven
```
<dependency>
	    <groupId>com.github.tranzzo</groupId>
	    <artifactId>android-sdk</artifactId>
	    <version>$version</version>
</dependency>
  ```
## Features

**Tokenization**: Simple way to obtain a Tranzzo user card token, generated on our servers.
    For more information visit our [**documentation**](https://cdn.tranzzo.com/tranzzo-api/index.html)
**Validation**: Use straightforward card utilities to validate, define and format your card input

## Usage

#### 1. Insert in project Tranzzo views
```
<com.tranzzo.android.sdk.view.CardNumberEditText
.../>

<com.tranzzo.android.sdk.view.ExpiryDateEditText
.../>

<com.tranzzo.android.sdk.view.CvcEditText
.../>
```
#### 2. Bind views and set in listener
```
TranzzoInputListener cardInputListener = new TranzzoInputListener(
                etCardNumber,
                etExpiration,
                etCvc,
                () -> btnTokenize.setEnabled(true)
        );
```
#### 3. Create methods for collect data card
```
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
```
    
 #### 4. Add validate method
 
 ```
  if (cardInputListener.isFormValid()) {
                collectCard().consume(
                          System.out.println(error.message),
                        c ->                
                                new TokenizeTask().execute(c);
                        }
                );
            }
        });
 ```
#### 5. Execute token in background 
 ```
   class TokenizeTask extends AsyncTask<Card, Void, Either<TrzError, CardToken>> {
        
        @Override
        protected Either<TrzError, CardToken> doInBackground(Card... cards) {
            return initTranzzo().tokenize(cards[0], getApplicationContext());
        }
        
        @Override
        protected void onPostExecute(Either<TrzError, CardToken> result) {
              result.consume(error->
                    System.out.println(error.message),
                    token -> {
                        System.out.println(token.toString());                     
                    });
        }
        }
    }
    
 ```
 
## License

    Copyright 2013 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 

