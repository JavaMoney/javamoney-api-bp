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
package javax.money.spi;

import javax.money.CurrencyUnit;
import javax.money.MonetaryRounding;
import javax.money.RoundingQuery;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * This class models the accessor for rounding instances, modeled as
 * {@link javax.money.MonetaryOperator}.
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
     * @see javax.money.RoundingQueryBuilder
     */
    List<String> getDefaultProviderChain();

    /**
     * Execute a query for {@link javax.money.MonetaryRounding}. This allows to model more complex used cases,
     * such as historic or special roundings.
     *
     * @param query the query to be exected, not null.
     * @return the roundings found, never null.
     */
    Collection<MonetaryRounding> getRoundings(RoundingQuery query);


    /**
     * Creates a {@link MonetaryRounding} that can be added as {@link javax.money.MonetaryOperator} to
     * chained calculations. The instance must lookup the concrete
     * {@link javax.money.MonetaryRounding} instance from the
     * based on the input {@link javax.money.MonetaryAmount}'s {@link javax.money.CurrencyUnit}.
     *
     * @return the (shared) default rounding instance.
     */
    MonetaryRounding getDefaultRounding();

    /**
     * Access a {@link javax.money.MonetaryRounding} for rounding {@link javax.money.MonetaryAmount}
     * instances given a currency.
     *
     * @param currencyUnit The currency, which determines the required precision. As
     *                     {@link java.math.RoundingMode}, by default, {@link java.math.RoundingMode#HALF_UP}
     *                     is sued.
     * @param providers    the optional provider list and ordering to be used
     * @return a new instance {@link javax.money.MonetaryOperator} implementing the
     * rounding, never {@code null}.
     * @throws javax.money.MonetaryException if no such rounding could be provided.
     */
    MonetaryRounding getRounding(CurrencyUnit currencyUnit, String... providers);


    /**
     * Access a {@link javax.money.MonetaryRounding} using the rounding name.
     *
     * @param roundingName The rounding name, not null.
     * @param providers    the optional provider list and ordering to be used
     * @return the corresponding {@link javax.money.MonetaryOperator} implementing the
     * rounding, never {@code null}.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
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
     * Checks if any {@link javax.money.MonetaryRounding} is matching the given query.
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
     * @return true, if a corresponding {@link javax.money.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    boolean isRoundingAvailable(String roundingId, String... providers);

    /**
     * Checks if a {@link MonetaryRounding} is available given a {@link javax.money.CurrencyUnit}.
     *
     * @param currencyUnit The currency, which determines the required precision. As {@link java.math.RoundingMode},
     *                     by default, {@link java.math.RoundingMode#HALF_UP} is used.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return true, if a corresponding {@link javax.money.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    boolean isRoundingAvailable(CurrencyUnit currencyUnit, String... providers);
}
