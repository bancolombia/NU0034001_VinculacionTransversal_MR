package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.personalinformation.PersonalInformation;

import java.util.List;
import java.util.Optional;

public interface ContactIValidationUseCase {

    void validateIfErrorField(List<ErrorField> efMandatory, List<ErrorField> efMerge);

    boolean validateFirstFieldsContactInfoNull(ContactInformation contactInformation);

    boolean validateFieldsContactInfoNull(ContactInformation contactInformation);

    List<ErrorField> validateIfRecordUpgradeable(
            Optional<PersonalInformation> piOld, Optional<ContactInformation> ci);

    void validateForeignCountry(List<ContactInformation> contactInfo);

    void validateAddressType(List<ContactInformation> contactInformationList);

    boolean existsTypeAddressResBrand(
            Acquisition acquisition, List<ContactInformation> contactInformationList, List<ContactInformation> ciList);

    void validateBrand(
            List<ContactInformation> contactInformationList, List<ContactInformation> ciFilterZ001List);

    /**
     * This function validates the pitufeo of email and cellphone of contact information
     */
    void maxRepetitionEmailCellphone(ContactInformation contactInformation);
}
