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
package org.javamoney.bp.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.javamoney.bp.CurrencyUnit;
import org.javamoney.bp.MonetaryCurrencies;
import org.javamoney.bp.MonetaryException;
import org.javamoney.bp.spi.Bootstrap;
import org.javamoney.bp.spi.MonetaryConversionsSingletonSpi;

/**
 * This singleton defines access to the exchange and currency conversion logic
 * of JavaMoney. It allows to evaluate the currently available exchange rate
 * type instances and provides access to the corresponding
 * {@link ExchangeRateProvider} and {@link org.javamoney.bp.convert.CurrencyConversion} instances.
 * <p>
 * This class is thread safe.
 * <p>
 * This class is designed to support also contextual behaviour, e.g. in Java EE
 * containers each application may provide its own {@link ExchangeRateProvider}
 * instances, e.g. by registering them as CDI beans. An EE container can
 * register an according {@link org.javamoney.bp.spi.MonetaryConversionsSingletonSpi} that manages the
 * different application contexts transparently. In a SE environment this class
 * is expected to behave like an ordinary singleton, loading its SPIs e.g. from the
 * JDK {@link java.util.ServiceLoader} or an alternate component and service provider.
 * <p>
 * This class is thread-safe. Hereby it is important to know that it delegates
 * to the registered {@link org.javamoney.bp.spi.MonetaryConversionsSingletonSpi} SPI, which also is required
 * to be thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public final class MonetaryConversions{

    /**
     * The SPI currently active, use {@link java.util.ServiceLoader} to register an
     * alternate implementation.
     */
    private static final MonetaryConversionsSingletonSpi MONETARY_CONVERSION_SPI = loadSpi();

    private static MonetaryConversionsSingletonSpi loadSpi() {
        MonetaryConversionsSingletonSpi spi = Bootstrap.getService(MonetaryConversionsSingletonSpi.class);
        if(spi==null){
            throw new MonetaryException("MonetaryConversionsSingletonSpi no available: no conversion will be possible.");
        }
        return spi;
    }

    /**
     * Private singleton constructor.
     */
    private MonetaryConversions(){
    }

    /**
     * Access an instance of {@link CurrencyConversion} for the given providers.
     * Use {@link #getProviderNames()} to check, which are available.
     *
     * @param termCurrency the terminating or target currency, not {@code null}
     * @param providers    Additional providers, for building a provider chain
     * @return the exchange rate type if this instance.
     * @throws IllegalArgumentException if no such {@link ExchangeRateProvider} is available.
     */
    public static CurrencyConversion getConversion(CurrencyUnit termCurrency, String... providers){
        Objects.requireNonNull(providers);
        Objects.requireNonNull(termCurrency);
        if(providers.length == 0){
            return MONETARY_CONVERSION_SPI.getConversion(
                    ConversionQueryBuilder.of().setTermCurrency(termCurrency).setProviderNames(getDefaultProviderChain())
                            .build());
        }
        return MONETARY_CONVERSION_SPI.getConversion(
                ConversionQueryBuilder.of().setTermCurrency(termCurrency).setProviderNames(providers).build());
    }

    /**
     * Access an instance of {@link CurrencyConversion} for the given providers.
     * Use {@link #getProviderNames()}} to check, which are available.
     *
     * @param termCurrencyCode the terminating or target currency code, not {@code null}
     * @param providers        Additional providers, for building a provider chain
     * @return the exchange rate type if this instance.
     * @throws MonetaryException if no such {@link ExchangeRateProvider} is available or if no {@link CurrencyUnit} was
     *                           matching the given currency code.
     */
    public static CurrencyConversion getConversion(String termCurrencyCode, String... providers){
        Objects.requireNonNull(termCurrencyCode, "Term currency code may not be null");
        return getConversion(MonetaryCurrencies.getCurrency(termCurrencyCode), providers);
    }

    /**
     * Access an instance of {@link CurrencyConversion} for the given providers.
     * Use {@link #getProviderNames()}} to check, which are available.
     *
     * @param conversionQuery The {@link ConversionQuery} required, not {@code null}
     * @return the {@link CurrencyConversion}  instance matching.
     * @throws IllegalArgumentException if the query defines {@link ExchangeRateProvider}s that are not available.
     */
    public static CurrencyConversion getConversion(ConversionQuery conversionQuery){
        if(MONETARY_CONVERSION_SPI==null){
            throw new MonetaryException(
                    "No MonetaryConveresionsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return MONETARY_CONVERSION_SPI.getConversion(conversionQuery);
    }

    /**
     * Checks if a {@link org.javamoney.bp.convert.CurrencyConversion} is available for the given parameters.
     *
     * @param conversionQuery the {@link org.javamoney.bp.convert.ConversionQuery}, not null.
     * @return true, if a conversion is accessible from {@link #getConversion(ConversionQuery)}.
     */
    public static boolean isConversionAvailable(ConversionQuery conversionQuery){
        if(MONETARY_CONVERSION_SPI==null){
            throw new MonetaryException(
                    "No MonetaryConveresionsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return MONETARY_CONVERSION_SPI.isConversionAvailable(conversionQuery);
    }

    /**
     * Checks if a {@link org.javamoney.bp.convert.CurrencyConversion} is available for the given parameters.
     *
     * @param currencyCode The currencoy code, resolbable by {@link org.javamoney.bp.MonetaryCurrencies#getCurrency
     *                     (String, String...)}
     * @param providers    Additional providers, for building a provider chain
     * @return true, if a conversion is accessible from {@link #getConversion(String, String...)}.
     */
    public static boolean isConversionAvailable(String currencyCode, String... providers){
        if(MONETARY_CONVERSION_SPI==null){
            throw new MonetaryException(
                    "No MonetaryConveresionsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return MONETARY_CONVERSION_SPI.isConversionAvailable(MonetaryCurrencies.getCurrency(currencyCode), providers);
    }

    /**
     * Checks if a {@link org.javamoney.bp.convert.CurrencyConversion} is available for the given parameters.
     *
     * @param termCurrency the terminating or target currency, not {@code null}
     * @param providers    Additional providers, for building a provider chain
     * @return true, if a conversion is accessible from {@link #getConversion(String, String...)}.
     */
    public static boolean isConversionAvailable(CurrencyUnit termCurrency, String... providers){
        if(MONETARY_CONVERSION_SPI==null){
            throw new MonetaryException(
                    "No MonetaryConveresionsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return MONETARY_CONVERSION_SPI.isConversionAvailable(termCurrency, providers);
    }

    /**
     * Access an instance of {@link CurrencyConversion} using the given
     * providers as a provider chain. Use {@link #getProviderNames()}s
     * to check, which are available.
     *
     * @return the exchange rate provider.
     * @throws IllegalArgumentException if no such {@link ExchangeRateProvider} is available.
     */
    public static ExchangeRateProvider getExchangeRateProvider(String... providers){
        if(providers.length == 0){
            List<String> defaultProviderChain = getDefaultProviderChain();
            return MONETARY_CONVERSION_SPI.getExchangeRateProvider(ConversionQueryBuilder.of().setProviderNames(
                    defaultProviderChain.toArray(new String[defaultProviderChain.size()])).build());
        }
        ExchangeRateProvider provider = MONETARY_CONVERSION_SPI
                .getExchangeRateProvider(ConversionQueryBuilder.of().setProviderNames(providers).build());
        if(provider==null){
            throw new MonetaryException("No such rate provider: " + Arrays.toString(providers));
        }
        return provider;
    }

	/**
	 * Access an instance of {@link CurrencyConversion} using the
	 * {@link ExchangeRateProviderSupplier}.
	 * 
	 * @param provider
	 *            the exchange rate provider.
	 * @param providers
	 *            the exchange rate provider.
	 * @return the exchange rate provider.
	 * @throws IllegalArgumentException
	 *             if no such {@link ExchangeRateProvider} is available.
	 */
	public static ExchangeRateProvider getExchangeRateProvider(
			ExchangeRateProviderSupplier provider,
			ExchangeRateProviderSupplier... providers) {

		List<ExchangeRateProviderSupplier> suppliers = new ArrayList<ExchangeRateProviderSupplier>();
        suppliers.add(Objects.requireNonNull(provider));
		for(ExchangeRateProviderSupplier supp: providers){
            suppliers.add(supp);
        }

		String[] array = new String[suppliers.size()];
        for(int i=0;i<array.length;i++){
            array[i] = suppliers.get(i).get();
        }
		return getExchangeRateProvider(array);

	}
    /**
     * Access an instance of {@link CurrencyConversion} using the given
     * providers as a provider chain. Use {@link #getProviderNames()}
     * to check, which are available.
     *
     * @return the exchange rate provider.
     * @throws IllegalArgumentException if no such {@link ExchangeRateProvider} is available.
     */
    public static ExchangeRateProvider getExchangeRateProvider(ConversionQuery conversionQuery){
        if(MONETARY_CONVERSION_SPI==null){
            throw new MonetaryException(
                    "No MonetaryConveresionsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return MONETARY_CONVERSION_SPI.getExchangeRateProvider(conversionQuery);
    }

    /**
     * Checks if a {@link org.javamoney.bp.convert.ExchangeRateProvider} is available for the given parameters.
     *
     * @param conversionQuery the {@link org.javamoney.bp.convert.ConversionQuery}, not null.
     * @return true, if a rate provider is accessible from {@link #getExchangeRateProvider(ConversionQuery)}}.
     */
    public static boolean isExchangeRateProviderAvailable(ConversionQuery conversionQuery){
        if(MONETARY_CONVERSION_SPI==null){
            throw new MonetaryException(
                    "No MonetaryConveresionsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return MONETARY_CONVERSION_SPI.isExchangeRateProviderAvailable(conversionQuery);
    }


    /**
     * Return the (non localized) names of all providers available in the
     * current context. Each id can be used to obtain
     * {@link ExchangeRateProvider} or {@link CurrencyConversion} instances.
     *
     * @return all supported provider ids, never {@code null}.
     */
    public static Collection<String> getProviderNames(){
        if(MONETARY_CONVERSION_SPI==null){
            throw new MonetaryException(
                    "No MonetaryConveresionsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return MONETARY_CONVERSION_SPI.getProviderNames();
    }

    /**
     * Get the default provider used.
     *
     * @return the default provider, never {@code null}.
     */
    public static List<String> getDefaultProviderChain(){
        if(MONETARY_CONVERSION_SPI==null){
            throw new MonetaryException(
                    "No MonetaryConveresionsSingletonSpi " + "loaded, query functionality is not available.");
        }
        List<String> defaultChain = MONETARY_CONVERSION_SPI.getDefaultProviderChain();
        Objects.requireNonNull(defaultChain, "No default provider chain provided by SPI: " +
                MONETARY_CONVERSION_SPI.getClass().getName());
        return defaultChain;
    }

}
