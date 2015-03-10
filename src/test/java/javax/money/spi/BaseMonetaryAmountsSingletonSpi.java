/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package javax.money.spi;

import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SPI (core) for the backing implementation of the {@link javax.money.MonetaryAmounts} singleton. It
 * should load and manage (including contextual behavior), if needed) the different registered
 * {@link javax.money.MonetaryAmountFactory} instances.
 *
 * @author Anatole Tresch
 */
public abstract class BaseMonetaryAmountsSingletonSpi implements MonetaryAmountsSingletonSpi{

    /**
     * Access the default {@link javax.money.MonetaryAmountFactory}.
     *
     * @return a the default {@link javax.money.MonetaryAmount} type corresponding, never {@code null}.
     * @throws javax.money.MonetaryException if no {@link javax.money.spi.MonetaryAmountFactoryProviderSpi} is available, or no
     *                           {@link javax.money.spi.MonetaryAmountFactoryProviderSpi} targeting the configured default
     *                           {@link javax.money.MonetaryAmount} type.
     * @see javax.money.MonetaryAmounts#getDefaultAmountType()
     */
    public MonetaryAmountFactory<?> getDefaultAmountFactory(){
        return getAmountFactory(getDefaultAmountType());
    }

    /**
     * Get the currently registered {@link javax.money.MonetaryAmount} implementation classes.
     *
     * @return the {@link java.util.Set} if registered {@link javax.money.MonetaryAmount} implementations, never
     * {@code null}.
     */
    public Collection<MonetaryAmountFactory<?>> getAmountFactories(){
        List<MonetaryAmountFactory<?>> factories = new ArrayList<MonetaryAmountFactory<?>>();
        for(Class<? extends MonetaryAmount> type : getAmountTypes()){
            factories.add(getAmountFactory(type));
        }
        return factories;
    }

}