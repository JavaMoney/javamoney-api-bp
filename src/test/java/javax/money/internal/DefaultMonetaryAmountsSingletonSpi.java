/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package javax.money.internal;

import javax.money.DummyAmount;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryException;
import javax.money.spi.BaseMonetaryAmountsSingletonSpi;
import javax.money.spi.Bootstrap;
import javax.money.spi.MonetaryAmountFactoryProviderSpi;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of MonetaryAmountsSpi for testing only.
 */
public class DefaultMonetaryAmountsSingletonSpi extends BaseMonetaryAmountsSingletonSpi {

	private Map<Class<? extends MonetaryAmount>, MonetaryAmountFactoryProviderSpi<?>> factories =
            new ConcurrentHashMap<Class<? extends MonetaryAmount>, MonetaryAmountFactoryProviderSpi<?>>();

	public DefaultMonetaryAmountsSingletonSpi() {
		for (MonetaryAmountFactoryProviderSpi<?> f : Bootstrap
				.getServices(MonetaryAmountFactoryProviderSpi.class)) {
			factories.put(f.getAmountType(), f);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends MonetaryAmount> MonetaryAmountFactory<T> getAmountFactory(
			Class<T> amountType) {
		MonetaryAmountFactoryProviderSpi<T> f = MonetaryAmountFactoryProviderSpi.class
				.cast(factories.get(amountType));
		if (f!=null) {
			return f.createMonetaryAmountFactory();
		}
		throw new MonetaryException(
				"No matching MonetaryAmountFactory found, type="
						+ amountType.getName());
	}

	@Override
	public Set<Class<? extends MonetaryAmount>> getAmountTypes() {
		return factories.keySet();
	}


	@Override
	public Class<? extends MonetaryAmount> getDefaultAmountType() {
		return DummyAmount.class;
	}

}
