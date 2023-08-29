package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.UpdateCiiuQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.model.economicinformation.gateways.EconomicRabbitRepository;
import co.com.bancolombia.usecase.economicinformation.EconomicInformationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.YES;
import static co.com.bancolombia.usecase.util.constants.Constants.ERROR_MSG_ECONOMIC_NOT_FOUND;

@Component
@AllArgsConstructor
public class EconomicRabbit extends ErrorHandleRabbit implements EconomicRabbitRepository {

    private final EconomicInformationRepository economicInformationRepository;
    private final EconomicInformationUseCase economicInformationUseCase;
    private final CoreFunctionDate coreFunctionDate;

    @Override
    public InfoRutReply getRequiredRut(AcquisitionIdQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acq = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();
            EconomicInformation economic = economicInformationRepository.findByAcquisition(acq);
            if (economic == null) {
                String message = ERROR_MSG_ECONOMIC_NOT_FOUND + query.getAcquisitionId();
                throwExceptionRabbit(message);
            }

            InfoRutReply reply = InfoRutReply.builder()
                    .valid(true)
                    .acquisitionId(query.getAcquisitionId())
                    .requiredRut(economic.getRequiredRut().equals(YES))
                    .ciiu(economic.getCiiu())
                    .flagCiiu(economic.getFlagCiiu())
                    .build();

            return reply;

        } catch (ValidationException | CustomException ex) {
            InfoRutReply reply = InfoRutReply.builder()
                    .valid(false)
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    @Override
    public EmptyReply updateCiiu(UpdateCiiuQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acq = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();
            EconomicInformation economic;
            Optional<EconomicInformation> economicUpdate = economicInformationUseCase.findByAcquisition(acq);

            if (economicUpdate.isPresent()) {
                if (!economicUpdate.get().getCiiu().equals(query.getCiiu())) {
                    economic = economicUpdate.get().toBuilder()
                            .ciiu(query.getCiiu())
                            .flagCiiu(NOT)
                            .updatedBy(query.getUsrMod())
                            .updatedDate(coreFunctionDate.getDatetime())
                            .build();
                } else {
                    economic = economicUpdate.get().toBuilder()
                            .flagCiiu(YES)
                            .updatedBy(query.getUsrMod())
                            .updatedDate(coreFunctionDate.getDatetime())
                            .build();
                }
                economicInformationUseCase.save(economic);
            }

            EmptyReply reply = EmptyReply.builder()
                    .valid(economicUpdate.isPresent())
                    .acquisitionId(query.getAcquisitionId())
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            EmptyReply reply = EmptyReply.builder()
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

    private void validateMandatory(UpdateCiiuQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
            if (query.getCiiu() == null || query.getCiiu().isEmpty()) {
                fieldList.add("ciiu");
            }
            if (query.getUsrMod() == null || query.getUsrMod().isEmpty()) {
                fieldList.add("usrMod");
            }
        }
        errorMandatory(fieldList);
    }
}
