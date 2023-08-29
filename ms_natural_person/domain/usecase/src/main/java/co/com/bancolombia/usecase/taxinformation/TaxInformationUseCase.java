package co.com.bancolombia.usecase.taxinformation;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.taxinformation.TaxInformation;

import java.util.List;

public interface TaxInformationUseCase {
    TaxInformation startProcessTaxInformation(TaxInformation taxInformation , List<TaxCountry> taxCountryList);

    TaxInformation findByAcquisition(Acquisition acquisition);

    void mandatoryFieldsTaxInfo(TaxInformation taxInformation);

    void mergeFieldsTaxInfo(TaxInformation infoSavedInDb, TaxInformation taxInformationNew);
}