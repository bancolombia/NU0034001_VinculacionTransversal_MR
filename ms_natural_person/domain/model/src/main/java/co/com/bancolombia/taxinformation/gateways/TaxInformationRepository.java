package co.com.bancolombia.taxinformation.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.taxinformation.TaxInformation;

import java.util.List;
import java.util.UUID;

public interface TaxInformationRepository {
    TaxInformation save(TaxInformation taxInformation);

    TaxInformation findByAcquisition(Acquisition acquisition);

    List<Object> searchDependentValueFieldAndActive(
            UUID idAcq, String table, String searchField, String searchValue, boolean active);
}