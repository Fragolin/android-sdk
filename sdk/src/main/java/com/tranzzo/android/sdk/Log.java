package com.tranzzo.android.sdk;

interface Log {
    void debug(String message);
    
    void error(String message);
    
    void error(String message, Throwable ex);
}
