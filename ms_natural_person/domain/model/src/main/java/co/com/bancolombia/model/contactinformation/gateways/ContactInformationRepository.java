package co.com.bancolombia.model.contactinformation.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.contactinformation.ContactInformation;

import java.util.List;
import java.util.UUID;

public interface ContactInformationRepository {
    ContactInformation save(ContactInformation contactInformation);

    ContactInformation findByAcquisitionAndAddressType(Acquisition acquisition, String addressType);

    List<ContactInformation> findAllByAcquisition(Acquisition acquisition);

    List<ContactInformation> saveAll(List<ContactInformation> contactInformationList);

    long countEmailsActive(String email, UUID acquisition, String addressType);

    long countCellphonesActive(String cell, UUID acquisition, String addressType);

    List<UUID> findAcquisitionListByEmail(String email, UUID acquisition, String addressType);

    List<UUID> findAcquisitionListByCellphone(String cell, UUID acquisition, String addressType);
}
