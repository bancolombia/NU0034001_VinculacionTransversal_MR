package co.com.bancolombia.usecase.personalinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.personalinformation.PersonalInformation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PersonalInformationUseCase {
    /**
     * Saves the personal information.
     * If the record exists in DB, then a merge between the new and the old one is made.
     *
     * @return The saved object
     * @params pInfo, info PersonalInformation object
     */
    PersonalInformation save(PersonalInformation pInfo, PersonalInformation info);

    /**
     * This function validates the pitufeo of email of personal information
     */
    Map<String, List<ErrorField>> validationRepetitionMaxEmail(String email, UUID acquisitionId);

    /**
     * This function validates the pitufeo of cellphone of personal information
     */
    Map<String, List<ErrorField>> validationRepetitionMaxCellphone(String cellphone, UUID acquisitionId);


    Optional<PersonalInformation> findByAcquisition(Acquisition acquisition);

    PersonalInformation save(PersonalInformation personalInformation);

}
