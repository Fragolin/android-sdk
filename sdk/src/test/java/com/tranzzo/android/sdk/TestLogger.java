package com.tranzzo.android.sdk;

public class TestLogger implements Log {
    
    @Override
    public void trace(String message) {
        debug(message);
    }
    
    @Override
    public void debug(String message) {
        System.out.println(message);
    }
    
    @Override
    public void error(String message) {
        System.err.println(message);
    }
    
    @Override
    public void error(String message, Throwable ex) {
        System.err.println(message + ": " + ex);
    }
}
