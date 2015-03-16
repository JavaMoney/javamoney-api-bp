/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.MonetaryAmount;
import org.javamoney.bp.api.MonetaryAmountFactory;
import org.javamoney.bp.api.MonetaryContext;
import org.javamoney.bp.api.MonetaryCurrencies;

/**
 * Factory for {@link org.javamoney.bp.api.MonetaryAmount} instances for a given type. It can be accessed, by
 * <ul>
 * <li>calling {@link org.javamoney.bp.api.MonetaryAmount#getFactory()}, returning a {@link org.javamoney.bp.api.spi.BaseMonetaryAmountFactory}
 * creating amounts of the same implementation type, which also provided the factory instance.</li>
 * <li>calling {@link org.javamoney.bp.api.MonetaryAmounts#getAmountFactory(Class)} accessing a
 * {@link org.javamoney.bp.api.spi.BaseMonetaryAmountFactory} for a concrete type <code>Class<T></code>.</li>
 * <li>calling {@link org.javamoney.bp.api.MonetaryAmounts#getDefaultAmountFactory()} accessing a default
 * {@link org.javamoney.bp.api.spi.BaseMonetaryAmountFactory}.
 * </ul>
 * <p>
 * Implementations of this interface allow to get {@link org.javamoney.bp.api.MonetaryAmount} instances providing
 * different data as required:
 * <ul>
 * <li>the {@link org.javamoney.bp.api.CurrencyUnit}, or the corresponding currency code (must be solvable by
 * {@link org.javamoney.bp.api.MonetaryCurrencies}).</li>
 * <li>the number part</li>
 * <li>the {@link org.javamoney.bp.api.MonetaryContext}</li>
 * <li>by passing any {@link org.javamoney.bp.api.MonetaryAmount} instance, it is possible to convert an arbitrary amount
 * implementation to the implementation provided by this factory. If the current factory cannot
 * support the precision/scale as required by the current {@link org.javamoney.bp.api.NumberValue} a
 * {@link org.javamoney.bp.api.MonetaryException} must be thrown.</li>
 * </ul>
 * If not defined a default {@link org.javamoney.bp.api.MonetaryContext} is used, which can also be configured by adding
 * configuration to a file {@code /javamoney.properties} to the classpath.
 * <p>
 * Hereby the entries. e.g. for a class {@code MyMoney} should start with {@code a.b.MyMoney.ctx}. The entries valid
 * must be documented
 * on the according implementation class, where the following entries are defined for all implementation types
 * (example below given for a class {@code a.b.MyMoney}:
 * <ul>
 * <li>{@code a.b.MyMoney.ctx.precision} to define the maximal supported precision.</li>
 * <li>{@code a.b.MyMoney.ctx.maxScale} to define the maximal supported scale.</li>
 * <li>{@code a.b.MyMoney.ctx.fixedScale} to define the scale to be fixed (constant).</li>
 * </ul>
 * <p>
 * <h2>Implementation specification</h2> Instances of this interface are <b>not</b> required to be
 * thread-safe!
 *
 * @author Anatole Tresch
 * @author Werner Keil
 * @version 0.6.1
 */
public abstract class BaseMonetaryAmountFactory<T extends MonetaryAmount> implements MonetaryAmountFactory<T> {

    /**
     * Sets the {@link org.javamoney.bp.api.CurrencyUnit} to be used.
     *
     * @param currencyCode the currencyCode of the currency to be used, not {@code null}. The currency code
     *                     will be resolved using {@link org.javamoney.bp.api.MonetaryCurrencies#getCurrency(String, String...)}.
     * @return This factory instance, for chaining.
     * @throws org.javamoney.bp.api.UnknownCurrencyException if the {@code currencyCode} is not resolvable.
     */
    public MonetaryAmountFactory<T> setCurrency(String currencyCode) {
        return setCurrency(MonetaryCurrencies.getCurrency(currencyCode));
    }

    /**
     * Uses an arbitrary {@link org.javamoney.bp.api.MonetaryAmount} to initialize this factory. Properties reused are:
     * <ul>
     * <li>CurrencyUnit,</li>
     * <li>Number value,</li>
     * <li>MonetaryContext.</li>
     * </ul>
     *
     * @param amount the amount to be used, not {@code null}.
     * @return this factory instance, for chaining.
     * @throws org.javamoney.bp.api.MonetaryException when the {@link org.javamoney.bp.api.MonetaryContext} implied by {@code amount.getContext()}
     *                           exceeds the capabilities supported by this factory type.
     */
    public MonetaryAmountFactory<T> setAmount(MonetaryAmount amount) {
        setCurrency(amount.getCurrency());
        setNumber(amount.getNumber());
        setContext(amount.getContext());
        return this;
    }

    /**
     * Returns the maximal {@link org.javamoney.bp.api.MonetaryContext} supported, for requests that exceed these maximal
     * capabilities, an {@link ArithmeticException} must be thrown.
     *
     * @return the maximal {@link org.javamoney.bp.api.MonetaryContext} supported, never {@code null}
     */
    public MonetaryContext getMaximalMonetaryContext() {
        return getDefaultMonetaryContext();
    }

}