/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.api;

import java.util.Objects;

/**
 * Builder class for creating new instances of {@link CurrencyContext} adding detailed information
 * about a {@link CurrencyUnit} instance. Typically the
 * contexts are created and assigned by the classes that implement the {@link org.javamoney.bp.api.spi.CurrencyProviderSpi}.
 * The according implementation classes should document, which attributes are available.
 * <p>
 * Note this class is NOT thread-safe.
 *
 * @see CurrencyUnit#getContext()
 */
public final class CurrencyContextBuilder extends AbstractContextBuilder<CurrencyContextBuilder, CurrencyContext> {

    /**
     * Creates a new builder.
     *
     * @param provider the provider name, creating the corresponding {@link CurrencyUnit} containing
     *                 the final {@link CurrencyContext} created by this builder, not null.
     */
    private CurrencyContextBuilder(String provider) {
        Objects.requireNonNull(provider);
        setProviderName(provider);
    }

    /**
     * Creates a new builder.
     *
     * @param context the {@link org.javamoney.bp.CurrencyContext} to be used for initializing this builder.
     */
    private CurrencyContextBuilder(CurrencyContext context) {
        Objects.requireNonNull(context);
        importContext(context);
    }

    /**
     * Creates a new instance of {@link org.javamoney.bp.CurrencyContext}.
     *
     * @return a new {@link org.javamoney.bp.CurrencyContext} instance.
     */
    @Override
    public CurrencyContext build() {
        return new CurrencyContext(this);
    }

    /**
     * Creates a new builder.
     *
     * @param provider the provider name, creating the corresponding {@link CurrencyUnit} containing
     *                 the final {@link CurrencyContext} created by this builder, not null.
     * @return a new {@link org.javamoney.bp.CurrencyContextBuilder} instance, never null.
     */
    public static CurrencyContextBuilder of(String provider) {
        return new CurrencyContextBuilder(provider);
    }

    /**
     * Creates a new builder.
     *
     * @param context the {@link org.javamoney.bp.CurrencyContext} to be used for initializing this builder.
     * @return a new {@link org.javamoney.bp.CurrencyContextBuilder} instance, never null.
     */
    public static CurrencyContextBuilder of(CurrencyContext context) {
        return new CurrencyContextBuilder(context);
    }

}