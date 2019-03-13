package com.tranzzo.android.sdk;

/**
 * Represents either successful result ({@link Outcome#value}) or formatted error ({@link Outcome#error}).
 * To check where result was successful use {@link #isSuccessful()}
 *
 * @see #isSuccessful()
 */
public class Outcome<T> {
    
    public final T value;
    public final TrzError error;
    
    static <T> Outcome<T> failure(TrzError error) {
        return new Outcome<>(null, error);
    }
    
    static <T> Outcome<T> success(final T value) {
        return new Outcome<>(value, null);
    }
    
    private Outcome(T value, TrzError error) {
        this.value = value;
        this.error = error;
    }
    
    public boolean isSuccessful() {
        return null == error;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Outcome that = (Outcome) o;
        
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return error != null ? error.equals(that.error) : that.error == null;
    }
    
    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Outcome{");
        sb.append("value=").append(value);
        sb.append(", error=").append(error);
        sb.append('}');
        return sb.toString();
    }
}
