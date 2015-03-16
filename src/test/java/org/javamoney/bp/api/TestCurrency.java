/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2015, Credit SUISSE All rights
 * reserved.
 */
package org.javamoney.bp.api;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adapter that implements the new {@link org.javamoney.bp.CurrencyUnit} interface using the
 * JDK's {@link java.util.Currency}.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 * @version 0.5
 */
public final class TestCurrency implements CurrencyUnit, Serializable, Comparable<CurrencyUnit> {

    private static final CurrencyContext CONTEXT = CurrencyContextBuilder.of("test-only").build();

    /**
     * The predefined name space for ISO 4217 currencies, similar to
     * {@link java.util.Currency}.
     */
    public static final String ISO_NAMESPACE = "ISO-4217";

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -2523936311372374236L;

    /**
     * currency code for this currency.
     */
    private final String currencyCode;
    /**
     * numeric code, or -1.
     */
    private final int numericCode;
    /**
     * fraction digits, or -1.
     */
    private final int defaultFractionDigits;

    private static final Map<String, CurrencyUnit> CACHED = new ConcurrentHashMap<String, CurrencyUnit>();

    /**
     * Private constructor.
     */
    private TestCurrency(String code, int numCode, int fractionDigits) {
        this.currencyCode = code;
        this.numericCode = numCode;
        this.defaultFractionDigits = fractionDigits;
    }

    public static CurrencyUnit of(Currency currency) {
        String key = ISO_NAMESPACE + ':' + currency.getCurrencyCode();
        CurrencyUnit cachedItem = CACHED.get(key);
        if (cachedItem==null) {
            cachedItem = new JDKCurrencyAdapter(currency);
            CACHED.put(key, cachedItem);
        }
        return cachedItem;
    }


    public static CurrencyUnit of(String currencyCode) {
        CurrencyUnit cu = CACHED.get(currencyCode);
        if (cu==null) {
            Currency cur = Currency.getInstance(currencyCode);
            if (cur!=null) {
                return of(cur);
            }
        }
        return cu;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public int getNumericCode() {
        return numericCode;
    }

    public int getDefaultFractionDigits() {
        return defaultFractionDigits;
    }

    @Override
    public CurrencyContext getContext() {
        return CONTEXT;
    }

    public int getCashRounding() {
        throw new UnsupportedOperationException("Not supported yet."); // To
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

    @SuppressWarnings("NullableProblems")
    public int compareTo(CurrencyUnit currency) {
        Objects.requireNonNull(currency);
        return getCurrencyCode().compareTo(currency.getCurrencyCode());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return currencyCode;
    }

    public static final class Builder {
        /**
         * currency code for this currency.
         */
        private String currencyCode;
        /**
         * numeric code, or -1.
         */
        private int numericCode = -1;
        /**
         * fraction digits, or -1.
         */
        private int defaultFractionDigits = -1;

        public Builder() {
        }

        public Builder(String currencyCode) {
            withCurrencyCode(currencyCode);
        }

        public Builder withCurrencyCode(String currencyCode) {

            if (currencyCode==null) {
                throw new IllegalArgumentException("currencyCode may not be null.");
            }
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder withDefaultFractionDigits(int defaultFractionDigits) {
            if (defaultFractionDigits < -1) {
                throw new IllegalArgumentException("Invalid value for defaultFractionDigits: " + defaultFractionDigits);
            }
            this.defaultFractionDigits = defaultFractionDigits;
            return this;
        }

        public Builder withNumericCode(int numericCode) {
            if (numericCode < -1) {
                throw new IllegalArgumentException("Invalid value for numericCode: " + numericCode);
            }
            this.numericCode = numericCode;
            return this;
        }


        public CurrencyUnit build() {
            return build(true);
        }

        public CurrencyUnit build(boolean cache) {
            if (currencyCode==null || currencyCode.isEmpty()) {
                throw new IllegalStateException("Can not of TestCurrencyUnit.");
            }
            if (cache) {
                String key = currencyCode;
                CurrencyUnit current = CACHED.get(key);
                if (current==null) {
                    current = new TestCurrency(currencyCode, numericCode, defaultFractionDigits);
                    CACHED.put(key, current);
                }
                return current;
            }
            return new TestCurrency(currencyCode, numericCode, defaultFractionDigits);
        }
    }

    /**
     * Adapter that implements the new {@link org.javamoney.bp.CurrencyUnit} interface using the
     * JDK's {@link java.util.Currency}.
     * <p>
     * This adapter will be removed in the final platform implementation.
     *
     * @author Anatole Tresch
     * @author Werner Keil
     */
    private final static class JDKCurrencyAdapter implements CurrencyUnit, Serializable, Comparable<CurrencyUnit> {

        private static final CurrencyContext JDK_CONTEXT = CurrencyContextBuilder.of(Currency.class.getName()).build();

        /**
         * serialVersionUID.
         */
        private static final long serialVersionUID = -2523936311372374236L;

        /**
         * ISO 4217 currency code for this currency.
         *
         * @serial
         */
        private final Currency currency;

        /**
         * Private constructor.
         *
         * @param currency the underlying JDK currency, not null.
         */
        private JDKCurrencyAdapter(Currency currency) {
            if (currency==null) {
                throw new IllegalArgumentException("Currency required.");
            }
            this.currency = currency;
        }

        @SuppressWarnings("NullableProblems")
        public int compareTo(CurrencyUnit currency) {
            Objects.requireNonNull(currency);
            int compare = getCurrencyCode().compareTo(currency.getCurrencyCode());
            if (compare == 0) {
                compare = getCurrencyCode().compareTo(currency.getCurrencyCode());
            }
            return compare;
        }

        public String getCurrencyCode() {
            return this.currency.getCurrencyCode();
        }

        public int getNumericCode() {
            return this.currency.getNumericCode();
        }

        public int getDefaultFractionDigits() {
            return this.currency.getDefaultFractionDigits();
        }

        @Override
        public CurrencyContext getContext() {
            return JDK_CONTEXT;
        }

        public String toString() {
            return this.currency.toString();
        }

    }

    // it be used and if so,
    // consider changing it to a pattern similar as getAvailableCurrencies()
    // (including the type Set, unless Collection provides value)
    public static Collection<CurrencyUnit> getAllMatching(String expression) {
        return Collections.emptySet();
    }

}
