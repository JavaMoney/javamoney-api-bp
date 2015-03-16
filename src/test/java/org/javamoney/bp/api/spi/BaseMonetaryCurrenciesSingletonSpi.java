/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE.
 *
 * Specification: JSR-354 Money and Currency API ("Specification")
 *
 * Copyright (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.CurrencyQuery;
import org.javamoney.bp.api.CurrencyQueryBuilder;
import org.javamoney.bp.api.CurrencyUnit;
import org.javamoney.bp.api.MonetaryException;
import org.javamoney.bp.api.UnknownCurrencyException;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Factory singleton backing interface for {@link org.javamoney.bp.MonetaryCurrencies} that provides access to
 * different registered {@link CurrencyProviderSpi} instances.
 * <p>
 * Implementations of this interface must be thread safe.
 *
 * @author Anatole Tresch
 * @version 0.8
 */
public abstract class BaseMonetaryCurrenciesSingletonSpi implements MonetaryCurrenciesSingletonSpi{

    /**
     * Access a new instance based on the currency code. Currencies are
     * available as provided by {@link CurrencyProviderSpi} instances registered
     * with the {@link org.javamoney.bp.api.spi.Bootstrap}.
     *
     * @param currencyCode the ISO currency code, not {@code null}.
     * @param providers    the (optional) specification of providers to consider. If not set (empty) the providers
     *                     as defined by #getDefaultProviderChain() should be used.
     * @return the corresponding {@link org.javamoney.bp.CurrencyUnit} instance.
     * @throws org.javamoney.bp.UnknownCurrencyException if no such currency exists.
     */
    public CurrencyUnit getCurrency(String currencyCode, String... providers) {
        Objects.requireNonNull(currencyCode, "Currency Code may not be null");
        Collection<CurrencyUnit> found =
                getCurrencies(CurrencyQueryBuilder.of().setCurrencyCodes(currencyCode).setProviderNames(providers).build());
        if (found.isEmpty()) {
            throw new UnknownCurrencyException(currencyCode);
        }
        if (found.size() > 1) {
            throw new MonetaryException("Ambiguous CurrencyUnit for code: " + currencyCode + ": " + found);
        }
        return found.iterator().next();
    }

    /**
     * Access a new instance based on the currency code. Currencies are
     * available as provided by {@link CurrencyProviderSpi} instances registered
     * with the {@link org.javamoney.bp.api.spi.Bootstrap}.
     *
     * @param country   the ISO currency's country, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
     * @return the corresponding {@link org.javamoney.bp.CurrencyUnit} instance.
     * @throws org.javamoney.bp.UnknownCurrencyException if no such currency exists.
     */
    public CurrencyUnit getCurrency(Locale country, String... providers) {
        Collection<CurrencyUnit> found =
                getCurrencies(CurrencyQueryBuilder.of().setCountries(country).setProviderNames(providers).build());
        if (found.isEmpty()) {
            throw new MonetaryException("No currency unit found for locale: " + country);
        }
        if (found.size() > 1) {
            throw new MonetaryException("Ambiguous CurrencyUnit for locale: " + country + ": " + found);
        }
        return found.iterator().next();
    }

    /**
     * Provide access to all currently known currencies.
     *
     * @param locale    the target {@link java.util.Locale}, typically representing an ISO country,
     *                  not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
     * @return a collection of all known currencies, never null.
     */
    public Set<CurrencyUnit> getCurrencies(Locale locale, String... providers) {
        return getCurrencies(CurrencyQueryBuilder.of().setCountries(locale).setProviderNames(providers).build());
    }

    /**
     * Allows to check if a {@link org.javamoney.bp.CurrencyUnit} instance is defined, i.e.
     * accessible from {@link org.javamoney.bp.api.spi.BaseMonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}.
     *
     * @param code      the currency code, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
     * @return {@code true} if {@link org.javamoney.bp.api.spi.BaseMonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}
     * would return a result for the given code.
     */
    public boolean isCurrencyAvailable(String code, String... providers) {
        return !getCurrencies(CurrencyQueryBuilder.of().setCurrencyCodes(code).setProviderNames(providers).build())
                .isEmpty();
    }

    /**
     * Allows to check if a {@link org.javamoney.bp.CurrencyUnit} instance is
     * defined, i.e. accessible from {@link #getCurrency(String, String...)}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
     * @return {@code true} if {@link #getCurrencies(java.util.Locale, String...)} would return a
     * non empty result for the given code.
     */
    public boolean isCurrencyAvailable(Locale locale, String... providers) {
        return !getCurrencies(CurrencyQueryBuilder.of().setCountries(locale).setProviderNames(providers).build()).isEmpty();
    }

    /**
     * Provide access to all currently known currencies.
     *
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
     * @return a collection of all known currencies, never null.
     */
    public Set<CurrencyUnit> getCurrencies(String... providers) {
        return getCurrencies(CurrencyQueryBuilder.of().setProviderNames(providers).build());
    }

    /**
     * Access a single currency by query.
     *
     * @param query The currency query, not null.
     * @return the {@link org.javamoney.bp.CurrencyUnit} found, never null.
     * @throws org.javamoney.bp.MonetaryException if multiple currencies match the query.
     */
    public CurrencyUnit getCurrency(CurrencyQuery query) {
        Set<CurrencyUnit> currencies = getCurrencies(query);
        if (currencies.isEmpty()) {
            return null;
        }
        if (currencies.size() == 1) {
            return currencies.iterator().next();
        }
        throw new MonetaryException("Ambiguous request for CurrencyUnit: " + query + ", found: " + currencies);
    }
}
