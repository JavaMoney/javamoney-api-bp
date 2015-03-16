/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2013, Credit Suisse All rights
 * reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.CurrencyUnit;
import org.javamoney.bp.api.MonetaryRounding;
import org.javamoney.bp.api.RoundingQuery;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * This class models the accessor for rounding instances, modeled as
 * {@link org.javamoney.bp.api.MonetaryOperator}.
 * <p>
 * This class is thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public interface MonetaryRoundingsSingletonSpi {

    /**
     * Allows to access the names of the current defined roundings.
     *
     * @param providers the providers and ordering to be used. By default providers and ordering as defined in
     *                  #getDefaultProviders is used.
     * @return the set of custom rounding ids, never {@code null}.
     */
    Set<String> getRoundingNames(String... providers);

    /**
     * Allows to access the names of the current registered rounding providers.
     *
     * @return the set of provider names, never {@code null}.
     */
    Set<String> getProviderNames();

    /**
     * Access a list of the currently registered default providers. The default providers are used, when
     * no provider names are passed by the caller.
     *
     * @return the provider names in order, defining the provider chain.
     * The default provider chain configured in {@code javamoney.properties} is used.
     * @see org.javamoney.bp.api.RoundingQueryBuilder
     */
    List<String> getDefaultProviderChain();

    /**
     * Execute a query for {@link org.javamoney.bp.api.MonetaryRounding}. This allows to model more complex used cases,
     * such as historic or special roundings.
     *
     * @param query the query to be exected, not null.
     * @return the roundings found, never null.
     */
    Collection<MonetaryRounding> getRoundings(RoundingQuery query);


    /**
     * Creates a {@link MonetaryRounding} that can be added as {@link org.javamoney.bp.api.MonetaryOperator} to
     * chained calculations. The instance must lookup the concrete
     * {@link org.javamoney.bp.api.MonetaryRounding} instance from the {@link org.javamoney.bp.api.spi.MonetaryRoundingsSingletonSpi}
     * based on the input {@link org.javamoney.bp.api.MonetaryAmount}'s {@link org.javamoney.bp.api.CurrencyUnit}.
     *
     * @return the (shared) default rounding instance.
     */
    MonetaryRounding getDefaultRounding();

    /**
     * Access a {@link org.javamoney.bp.api.MonetaryRounding} for rounding {@link org.javamoney.bp.api.MonetaryAmount}
     * instances given a currency.
     *
     * @param currencyUnit The currency, which determines the required precision. As
     *                     {@link java.math.RoundingMode}, by default, {@link java.math.RoundingMode#HALF_UP}
     *                     is sued.
     * @param providers    the optional provider list and ordering to be used
     * @return a new instance {@link org.javamoney.bp.api.MonetaryOperator} implementing the
     * rounding, never {@code null}.
     * @throws org.javamoney.bp.api.MonetaryException if no such rounding could be provided.
     */
    MonetaryRounding getRounding(CurrencyUnit currencyUnit, String... providers);


    /**
     * Access a {@link org.javamoney.bp.api.MonetaryRounding} using the rounding name.
     *
     * @param roundingName The rounding name, not null.
     * @param providers    the optional provider list and ordering to be used
     * @return the corresponding {@link org.javamoney.bp.api.MonetaryOperator} implementing the
     * rounding, never {@code null}.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link org.javamoney.bp.api.spi.RoundingProviderSpi} instance.
     */
    MonetaryRounding getRounding(String roundingName, String... providers);


    /**
     * Query a specific rounding with the given query. If multiple roundings match the query the first one is
     * selected, since the query allows to determine the providers and their ordering by setting {@link
     * RoundingQuery#getProviderNames()}.
     *
     * @param query the rounding query, not null.
     * @return the rounding found, or null, if no rounding matches the query.
     */
    MonetaryRounding getRounding(RoundingQuery query);

    /**
     * Checks if any {@link org.javamoney.bp.api.MonetaryRounding} is matching the given query.
     *
     * @param query the rounding query, not null.
     * @return true, if at least one rounding matches the query.
     */
    boolean isRoundingAvailable(RoundingQuery query);

    /**
     * Checks if a {@link MonetaryRounding} is available given a roundingId.
     *
     * @param roundingId The rounding identifier.
     * @param providers  the providers and ordering to be used. By default providers and ordering as defined in
     *                   #getDefaultProviders is used.
     * @return true, if a corresponding {@link org.javamoney.bp.api.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    boolean isRoundingAvailable(String roundingId, String... providers);

    /**
     * Checks if a {@link MonetaryRounding} is available given a {@link org.javamoney.bp.api.CurrencyUnit}.
     *
     * @param currencyUnit The currency, which determines the required precision. As {@link java.math.RoundingMode},
     *                     by default, {@link java.math.RoundingMode#HALF_UP} is used.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return true, if a corresponding {@link org.javamoney.bp.api.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    boolean isRoundingAvailable(CurrencyUnit currencyUnit, String... providers);
}