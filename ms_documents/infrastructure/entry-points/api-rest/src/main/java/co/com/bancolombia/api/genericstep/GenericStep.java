package co.com.bancolombia.api.genericstep;

import co.com.bancolombia.DocumentsController;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.common.exception.InvalidOptionalArgumentException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.model.StepForLogClass;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.commons.OptionalMandatoryArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL;

@DocumentsController
public class GenericStep {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private ILogFuncAcquisitionUseCase acquisitionUseCase;

    @Autowired
    private ILogFuncFieldUseCase fieldUseCase;

    @Autowired
    private ILogFuncCheckListUseCase checkListUseCase;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    private StepForLogFunctional stepForLogFunctional;

    public StepForLogFunctional firstStepForLogFunctional(
            UserInfoRequestData data, MetaRequest meta, String codeOperation) {
        stepForLogFunctional = StepForLogFunctional.builder()
                .webRequest(webRequest).acquisitionUseCase(acquisitionUseCase).fieldUseCase(fieldUseCase)
                .coreFunctionDate(coreFunctionDate).checkListUseCase(checkListUseCase).build();
        MyDataInitial dataInitial = MyDataInitial.builder()
                .acquisitionId(data.getAcquisitionId()).documentNumber(data.getDocumentNumber())
                .documentType(data.getDocumentType()).build();

        AcquisitionInitial acquisitionInitial = AcquisitionInitial.builder().meta(meta).data(dataInitial).build();
        stepForLogFunctional.firstStepForLogFunctional(codeOperation, acquisitionInitial);
        return stepForLogFunctional;
    }

    public void finallyStep(String acquisitionId, InfoReuseCommon infoReuseCommon, String codeOperation) {
        if (acquisitionId != null) {
            UUID acqId = UUID.fromString(acquisitionId);
            StepForLogClass step = StepForLogClass.builder().idAcquisition(acqId).operation(codeOperation).build();
            if (infoReuseCommon != null) {
                step.setRequestReuse(infoReuseCommon.getRequestReuse());
                step.setRequestDateReuse(infoReuseCommon.getDateRequestReuse());
                step.setResponseReuse(infoReuseCommon.getResponseReuse());
                step.setResponseDateReuse(infoReuseCommon.getDateResponseReuse());
                step.setMapaField(infoReuseCommon.getMapFields());
            }
            stepForLogFunctional.finallyStepForLogFunctional(step);
        }
    }

    public void finallyStep(
            StepForLogFunctional stepForLogFunctional, String acquisitionId,
            InfoReuseCommon infoReuseCommon, String codeOperation){
        if (acquisitionId != null) {
            UUID acqId = UUID.fromString(acquisitionId);
            stepForLogFunctional.finallyStepForLogFunctional(StepForLogClass.builder()
                    .idAcquisition(acqId).operation(codeOperation)
                    .requestReuse(infoReuseCommon.getRequestReuse())
                    .requestDateReuse(infoReuseCommon.getDateRequestReuse())
                    .responseReuse(infoReuseCommon.getResponseReuse())
                    .responseDateReuse(infoReuseCommon.getDateResponseReuse())
                    .mapaField(infoReuseCommon.getMapFields()).build());
        }
    }

    public void validRequest(Object request) {
        List<InvalidOptionalArgumentException> listException = new ArrayList<>(
                OptionalMandatoryArguments.validArgumentsList(
                        request, new Class[]{ValidationOptional.class}, ERROR_CODE_OPTIONAL, EMPTY, EMPTY));
        OptionalMandatoryArguments.validateException(listException);
    }
}