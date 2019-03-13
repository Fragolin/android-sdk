package com.tranzzo.android.sdk;

import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;

/**
 * Represents either successful result ({@link Either#value}) or formatted error ({@link Either#error}).
 * To check where result was successful use {@link #isSuccessful()}
 *
 * @see #isSuccessful()
 */
public class Either<E, T> {
    
    public final T value;
    public final E error;
    
    public static <E, T> Either<E, T> success(final T value) {
        return new Either<>(value, null);
    }
    
    public static <E, T> Either<E, T> failure(E error) {
        return new Either<>(null, error);
    }
    
    private Either(T value, E error) {
        this.value = value;
        this.error = error;
    }
    
    public <V> Either<E, V> flatMap(Function<T, Either<E, V>> f) {
        if (isSuccessful()) {
            return f.apply(this.value);
        } else {
            return Either.failure(this.error);
        }
    }
    
    public <V> Either<E, V> map(Function<T, V> f) {
        if (isSuccessful()) {
            return Either.success(f.apply(this.value));
        } else {
            return Either.failure(this.error);
        }
    }
    
    public <V> V fold(Function<E, V> onError, Function<T, V> onSuccess) {
        if (isSuccessful()) {
            return onSuccess.apply(this.value);
        } else {
            return onError.apply(this.error);
        }
    }
    
    public void consume(Consumer<E> onError, Consumer<T> onSuccess) {
        if (isSuccessful()) {
            onSuccess.accept(this.value);
        } else {
            onError.accept(this.error);
        }
    }
    
    public boolean isSuccessful() {
        return null == error;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Either that = (Either) o;
        
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
        final StringBuilder sb = new StringBuilder("Either{");
        sb.append("value=").append(value);
        sb.append(", error=").append(error);
        sb.append('}');
        return sb.toString();
    }
    
}
