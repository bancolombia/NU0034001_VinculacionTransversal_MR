package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.SaveBasicInfoQuery;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.model.exposerabbit.SaveBasicInfoRabbitRepository;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO_PARCIAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_SAVE_BASIC_INFO;

@Component
@AllArgsConstructor
public class SaveBasicInfoRabbit extends ErrorHandleRabbit implements SaveBasicInfoRabbitRepository {

    private final BasicInformationUseCase basicInformationUseCase;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final CoreFunctionDate coreFunctionDate;

    @Override
    public EmptyReply saveResult(SaveBasicInfoQuery query) {
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();
        BasicInformation basicInformation;
        EmptyReply reply = EmptyReply.builder().acquisitionId(query.getAcquisitionId()).build();
        Optional<BasicInformation> basicInformationUpdate = basicInformationUseCase.findByAcquisition(acquisition);
        if(!basicInformationUpdate.isPresent()){
            basicInformation = BasicInformation.builder().acquisition(acquisition)
                    .createdDate(coreFunctionDate.getDatetime())
                    .createdBy(query.getUsrMod()).gender(query.getGender()).build();
        }else{
            basicInformation = basicInformationUpdate.get().toBuilder()
                    .updatedBy(query.getUsrMod()).updatedDate(coreFunctionDate.getDatetime())
                    .gender(query.getGender()).build();
        }
        if (basicInformationUseCase.save(basicInformation) != null) {
            vinculationUpdateUseCase.markOperation(
                    acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_COMPLETADO_PARCIAL);
            return reply.toBuilder().valid(true).build();
        } else {
            adapter.error(ERROR_SAVE_BASIC_INFO);
            return reply.toBuilder().valid(false).build();
        }
    }
}