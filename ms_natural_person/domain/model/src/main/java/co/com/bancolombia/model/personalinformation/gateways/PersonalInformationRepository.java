package co.com.bancolombia.model.personalinformation.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.personalinformation.PersonalInformation;

import java.util.List;
import java.util.UUID;

public interface PersonalInformationRepository {
    PersonalInformation save(PersonalInformation personalInformation);

    PersonalInformation findByAcquisition(Acquisition acquisition);

    long countEmailsActive(String state, String email, UUID acquisition);

    long countCellphonesActive(String state, String cell, UUID acquisition);

    List<UUID> findAcquisitionListByEmail(String email, UUID acquisition);

    List<UUID> findAcquisitionListByCellphone(String cell, UUID acquisition);
}