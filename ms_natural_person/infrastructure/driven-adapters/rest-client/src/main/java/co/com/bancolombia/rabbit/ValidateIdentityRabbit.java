package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityRabbitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_WORK;


@Component
@AllArgsConstructor
public class ValidateIdentityRabbit extends ErrorHandleRabbit implements ValidateIdentityRabbitRepository {

    private final PersonalInformationRepository personalInformationRepository;
    private final ContactInformationRepository contactInformationRepository;

    @Override
    public ValidateIdentityReply validateIdentity(AcquisitionIdQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acq = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();
            PersonalInformation personal = personalInformationRepository.findByAcquisition(acq);
            ContactInformation contact = contactInformationRepository.findByAcquisitionAndAddressType(
                    acq, CO_ADDRESS_TYPE_WORK);

            ValidateIdentityReply reply = ValidateIdentityReply.builder()
                    .valid(true)
                    .acquisitionId(query.getAcquisitionId())
                    .emailPersonal(personal == null ? null : personal.getEmail())
                    .cellphonePersonal(personal == null ? null : personal.getCellphone())
                    .firstName(personal == null ? null : personal.getFirstName())
                    .secondName(personal == null ? null : personal.getSecondName())
                    .firstSurname(personal == null ? null : personal.getFirstSurname())
                    .secondSurname(personal == null ? null : personal.getSecondSurname())
                    .birthDate(personal == null ? null : personal.getBirthdate())
                    .expeditionDate(personal == null ? null : personal.getExpeditionDate())
                    .address(contact == null ? null : contact.getAddress())
                    .telephoneNumber(contact == null ? null : contact.getPhone())
                    .cellphoneContact(contact == null ? null : contact.getCellphone())
                    .emailContact(contact == null ? null : contact.getEmail())
                    .companyName(contact == null ? null : contact.getCompanyName())
                    .build();

            return reply;

        } catch (ValidationException | CustomException ex) {
            ValidateIdentityReply reply = ValidateIdentityReply.builder()
                    .valid(false)
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(AcquisitionIdQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
        }
        errorMandatory(fieldList);
    }
}
