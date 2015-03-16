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
package org.javamoney.bp.api;


/**
 * Represents a supplier of {@link CurrencyUnit}-valued results. This is the
 * {@link CurrencyUnit}-producing specialization of {@code Supplier} (as in Java 8).
 * 
 * <p>
 * There is no requirement that a distinct result be returned each time the
 * supplier is invoked.
 * 
 * <p>
 * This is a <b>functional interface</b> whose
 * functional method is {@link #getCurrency()}.
 * This class does not extends {@code java.util.function.Supplier} since {@link MonetaryAmount} implements
 * both supplier interfaces, {@link NumberSupplier} and {@link CurrencySupplier},
 * which will lead
 * to method name conflicts.
 * </p>
 *
 * @author Werner Keil
 * @version 0.5
 * @since 0.8
 */
//@FunctionalInterface
public interface CurrencySupplier {

	/**
	 * Gets the corresponding {@link CurrencyUnit}.
	 * 
	 * @return the corresponding {@link CurrencyUnit}, not null.
	 */
	CurrencyUnit getCurrency();
}