package com.tranzzo.android.sdk;

interface Log {
    
    void trace(String message);
    
    void debug(String message);
    
    void error(String message);
    
    void error(String message, Throwable ex);
    
}
