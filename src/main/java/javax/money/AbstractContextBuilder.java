/*
 * Copyright 2012-2016 Credit Suisse
 * Copyright 2018-2020 Werner Keil, Otavio Santana, Trivadis AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.money;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This interface defines the common generic parts of a query. Queries are used to pass complex parameters sets
 * to lookup monetary artifacts, e.g. {@link MonetaryAmountFactory},
 * {@link MonetaryRounding},
 * {@link CurrencyUnit}, {@link javax.money.convert.ExchangeRateProvider} and {@link javax.money.convert.CurrencyConversion}.
 * <p>
 * Instances of this class are not thread-safe and not serializable.
 */
@SuppressWarnings("ALL")
public abstract class AbstractContextBuilder<B extends AbstractContextBuilder, C extends AbstractContext>{

    /**
     * The data map containing all values.
     */
    final Map<String, Object> data = new HashMap<String, Object>();

    /**
     * Apply all attributes on the given context.
     *
     * @param context             the context to be applied, not null.
     * @param overwriteDuplicates flag, if existing entries should be overwritten.
     * @return this Builder, for chaining
     */
    public B importContext(AbstractContext context, boolean overwriteDuplicates){
        for (Map.Entry<String, Object> en : context.data.entrySet()) {
            if (overwriteDuplicates) {
                this.data.put(en.getKey(), en.getValue());
            }else{
                Object value = this.data.get(en.getKey());
                if(value==null){
                    this.data.put(en.getKey(), en.getValue());
                }
            }
        }
        return (B) this;
    }

    /**
     * Apply all attributes on the given context, hereby existing entries are preserved.
     *
     * @param context the context to be applied, not null.
     * @return this Builder, for chaining
     * @see #importContext(AbstractContext, boolean)
     */
    public B importContext(AbstractContext context){
        Objects.requireNonNull(context);
        return importContext(context, false);
    }

    /**
     * Sets an Integer attribute.
     *
     * @param key   the key, non null.
     * @param value the value
     * @return the Builder, for chaining.
     */
    public B set(String key, int value) {
        this.data.put(key, Objects.requireNonNull(value));
        return (B) this;
    }


    /**
     * Sets an Boolean attribute.
     *
     * @param key   the key, non null.
     * @param value the value
     * @return the Builder, for chaining.
     */
    public B set(String key, boolean value) {
        this.data.put(key, Objects.requireNonNull(value));
        return (B) this;
    }


    /**
     * Sets an Long attribute.
     *
     * @param key   the key, non null.
     * @param value the value
     * @return the Builder, for chaining.
     */
    public B set(String key, long value) {
        this.data.put(key, Objects.requireNonNull(value));
        return (B) this;
    }


    /**
     * Sets an Float attribute.
     *
     * @param key   the key, non null.
     * @param value the value
     * @return the Builder, for chaining.
     */
    public B set(String key, float value) {
        this.data.put(key, Objects.requireNonNull(value));
        return (B) this;
    }

    /**
     * Sets an Double attribute.
     *
     * @param key   the key, non null.
     * @param value the value
     * @return the Builder, for chaining.
     */
    public B set(String key, double value) {
        this.data.put(key, Objects.requireNonNull(value));
        return (B) this;
    }


    /**
     * Sets an Character attribute.
     *
     * @param key   the key, non null.
     * @param value the value
     * @return the Builder, for chaining.
     */
    public B set(String key, char value) {
        this.data.put(key, Objects.requireNonNull(value));
        return (B) this;
    }


    /**
     * Sets an attribute, using {@code attribute.getClass()} as attribute
     * <i>type</i> and {@code attribute.getClass().getName()} as attribute
     * <i>name</i>.
     *
     * @param value the attribute value
     * @return this Builder, for chaining
     */
    public B set(Object value) {
        data.put(value.getClass().getName(), value);
        return (B) this;
    }

    /**
     * Sets an attribute, using {@code attribute.getClass()} as attribute
     * <i>type</i>.
     *
     * @param value the attribute value
     * @param key   the attribute's key, not {@code null}
     * @return this Builder, for chaining
     */
    public B set(String key, Object value) {
        data.put(key, value);
        return (B) this;
    }

    /**
     * Sets an attribute, using {@code attribute.getClass()} as attribute
     * <i>type</i>.
     *
     * @param value the attribute value
     * @param key   the attribute's key, not {@code null}
     * @return this Builder, for chaining
     */
    public <T> B set(Class<T> key, T value) {
        Object old = set(key.getName(), value);
        if (old != null && old.getClass().isAssignableFrom(value.getClass())) {
            return (B) old;
        }
        return (B) this;
    }

    /**
     * Sets the provider.
     *
     * @param provider the provider, not null.
     * @return the Builder for chaining
     */
    public B setProviderName(String provider) {
        Objects.requireNonNull(provider);
        set(AbstractContext.KEY_PROVIDER, provider);
        return (B) this;
    }
//
//    /**
//     * Set the target timestamp in UTC millis. This allows to select historical roundings that were valid in the
//     * past. Its implementation specific, to what extend historical roundings are available. By default if this
//     * property is not set always current {@link  javax.money.MonetaryRounding} instances are provided.
//     *
//     * @param timestamp the target timestamp
//     * @return this instance for chaining
//     * @see #setTimestamp(java.time.temporal.TemporalAccessor)
//     */
//    public B setTimestampMillis(long timestamp){
//        set(AbstractContext.KEY_TIMESTAMP, timestamp);
//        return (B) this;
//    }
//
//    /**
//     * Set the target timestamp as {@link java.time.temporal.TemporalAccessor}. This allows to select historical
//     * roundings that were valid in the past. Its implementation specific, to what extend historical roundings
//     * are available. By default if this property is not set always current {@link  javax.money.MonetaryRounding}
//     * instances are provided.
//     *
//     * @param timestamp the target timestamp
//     * @return this instance for chaining
//     * @see #setTimestampMillis(long)
//     */
//	public B setTimestamp(LocalDateTime timestamp) {
//        set(AbstractContext.KEY_TIMESTAMP, Objects.requireNonNull(timestamp));
//        return (B) this;
//    }

    /**
     * Removes an entry of a certain keys. This can be useful, when a context is initialized with another
     * existing context, but only subset of the entries should be visible. For example {@code removeAttributes
     * ("a", "b", "c")} removes all attributes named 'a','b' and 'c'.
     *
     * @param keys the keys
     * @return this Builder, for chaining
     */
    public B removeAttributes(String... keys) {
        for (String key : keys) {
            this.data.remove(key);
        }
        return (B) this;
    }

    /**
     * Creates a new {@link AbstractContext} with the data from this Builder
     * instance.
     *
     * @return a new {@link AbstractContext}. never {@code null}.
     */
    public abstract C build();

    /*
     * (non-Javadoc)
     *
     * @see Object#toString()
     */

    @Override
    public String toString(){
        return getClass().getSimpleName() + " [attributes:\n" + new TreeMap<String, Object>(data).toString() + ']';
    }
}