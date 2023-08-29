package co.com.bancolombia.model.taxcountry.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.taxcountry.TaxCountry;

import java.util.List;

public interface TaxCountryRepository {
    List<TaxCountry> saveAll(List<TaxCountry> taxCountryList);
    List<TaxCountry> findAllByAcquisition(Acquisition acquisition);
}