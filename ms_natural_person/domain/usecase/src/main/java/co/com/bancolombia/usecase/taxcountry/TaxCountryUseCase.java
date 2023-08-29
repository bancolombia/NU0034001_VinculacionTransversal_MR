package co.com.bancolombia.usecase.taxcountry;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.taxcountry.TaxCountry;

import java.util.List;

public interface TaxCountryUseCase {
    List<TaxCountry> saveAll(List<TaxCountry> taxCountryList);

    List<TaxCountry> findAllByAcquisition(Acquisition acquisition);

    List<TaxCountry> valMandatoryFieldsAndMerge(
            Acquisition acquisition, List<TaxCountry> taxCountryList, Object dependentObject);

    List<TaxCountry> validateIdentifier (List<TaxCountry> taxCountryList);
}