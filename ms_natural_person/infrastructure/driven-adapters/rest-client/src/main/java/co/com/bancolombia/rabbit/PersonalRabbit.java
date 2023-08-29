package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.SavePersonalInfoQuery;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalRabbitRepository;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_SAVE_PERSONAL_INFO;

@Component
@AllArgsConstructor
public class PersonalRabbit extends ErrorHandleRabbit implements PersonalRabbitRepository {

    private final PersonalInformationUseCase personalInformationUseCase;
    private final CoreFunctionDate coreFunctionDate;

    @Override
    public EmptyReply saveResult(SavePersonalInfoQuery query) {
        try { validateMandatory(query);
            validateUUID(query.getAcquisitionId().toString());
        }catch (ValidationException | CustomException ex){
            return  EmptyReply.builder().valid(false).errorList(getErrorFromException(ex))
                   .build(); }
        PersonalInformation personalInformation;
        Acquisition acquisition = Acquisition.builder().id(query.getAcquisitionId()).build();
        Optional<PersonalInformation> pInformationUpdate = personalInformationUseCase.findByAcquisition(acquisition);
        EmptyReply reply = EmptyReply.builder().acquisitionId(query.getAcquisitionId().toString()).build();
        if(!pInformationUpdate.isPresent()){
            personalInformation = PersonalInformation.builder().acquisition(acquisition)
                    .firstName(query.getFirstName()).secondName(query.getSecondName())
                    .firstSurname(query.getFirstSurname()).secondSurname(query.getSecondSurname())
                    .birthdate(query.getBirthDate()).createdBy(query.getUsrMod())
                    .createdDate(coreFunctionDate.getDatetime()).expeditionDate(query.getExpeditionDate()).build();
        }else{
            Date expDateVerify = query.getExpeditionDate() != null ? query.getExpeditionDate() :
                    pInformationUpdate.get().getExpeditionDate();
            personalInformation = pInformationUpdate.get().toBuilder().acquisition(acquisition)
                    .firstName(query.getFirstName()).updatedDate(coreFunctionDate.getDatetime())
                    .secondName(query.getSecondName()).firstSurname(query.getFirstSurname())
                    .secondSurname(query.getSecondSurname()).birthdate(query.getBirthDate())
                    .updatedBy(query.getUsrMod()).expeditionDate(expDateVerify).build();
        }
        if(personalInformationUseCase.save(personalInformation)!=null) {
            return reply.toBuilder().valid(true).acquisitionId(query.getAcquisitionId().toString()).build();
        }else{
            adapter.error(ERROR_SAVE_PERSONAL_INFO);
            return reply.toBuilder().valid(false).acquisitionId(query.getAcquisitionId().toString()).build(); }
    }

    private void validateMandatory(SavePersonalInfoQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().toString().isEmpty()) {
                fieldList.add("acquisitionId");
            }
            if (query.getUsrMod() == null || query.getUsrMod().isEmpty()) {
                fieldList.add("usrMod");
            }
        }
        errorMandatory(fieldList);
    }
}
