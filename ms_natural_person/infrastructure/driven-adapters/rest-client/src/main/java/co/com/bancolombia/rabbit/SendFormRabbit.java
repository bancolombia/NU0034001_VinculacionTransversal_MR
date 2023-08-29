package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SendFormReply;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.exposerabbit.SendFormRabbitRepository;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCasePersist;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.usecase.util.constants.Constants.ERROR_MSG_CONTACT_NOT_FOUND;
import static co.com.bancolombia.usecase.util.constants.Constants.ERROR_MSG_PERSONAL_NOT_FOUND;

@Component
@AllArgsConstructor
public class SendFormRabbit extends ErrorHandleRabbit implements SendFormRabbitRepository {

    private final PersonalInformationUseCase personalInformationUseCase;
    private final ContactInformationUseCasePersist contactInformationUseCasePersist;

    @Override
    public SendFormReply sendFormReply(AcquisitionIdQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acq = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();

            PersonalInformation personalInformation =
                    personalInformationUseCase.findByAcquisition(acq).orElse(null);
            if (personalInformation == null) {
                String message = ERROR_MSG_PERSONAL_NOT_FOUND + query.getAcquisitionId();
                throwExceptionRabbit(message);
            }

            List<ContactInformation> contactInformationList =
                    contactInformationUseCasePersist.findAllByAcquisition(acq);
            if (contactInformationList.isEmpty()) {
                String message = ERROR_MSG_CONTACT_NOT_FOUND + query.getAcquisitionId();
                throwExceptionRabbit(message);
            }

            SendFormReply reply = SendFormReply.builder()
                    .acquisitionId(query.getAcquisitionId())
                    .valid(true)
                    .firstName(personalInformation.getFirstName())
                    .secondName(personalInformation.getSecondName())
                    .firstSurname(personalInformation.getFirstSurname())
                    .secondSurname(personalInformation.getSecondSurname())
                    .fullName(personalInformation.getFullName())
                    .email(contactInformationList.get(0).getEmail())
                    .build();
            return reply;
        } catch (ValidationException | CustomException ex) {
            SendFormReply reply = SendFormReply.builder()
                    .valid(false)
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex)).build();
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
