package co.com.bancolombia.api;

import co.com.bancolombia.api.model.ValidateQuestionsRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationInvalidInputList;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatoryInputList;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANDATORY_INVALID_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANDATORY_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL;

@Controller
public class GenericStep {

    @Autowired
    private WebRequest webRequest;
    @Autowired
    private ILogFuncAcquisitionUseCase iLogFuncAcquisitionUseCase;
    @Autowired
    private ILogFuncFieldUseCase iLogFuncFieldUseCase;
    @Autowired
    private CoreFunctionDate coreFunctionDate;
    @Autowired
    private ILogFuncCheckListUseCase iLogFuncCheckListUseCase;

    public StepForLogFunctional firstStepForLogFunctional(
            UserInfoRequestData data, MetaRequest meta, String codeOperation) {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder()
                .webRequest(webRequest).acquisitionUseCase(iLogFuncAcquisitionUseCase)
                .fieldUseCase(iLogFuncFieldUseCase).coreFunctionDate(coreFunctionDate)
                .checkListUseCase(iLogFuncCheckListUseCase).build();
        MyDataInitial dataInitial = MyDataInitial.builder()
                .acquisitionId(data.getAcquisitionId()).documentNumber(data.getDocumentNumber())
                .documentType(data.getDocumentType()).build();

        AcquisitionInitial acquisitionInitial = AcquisitionInitial.builder().meta(meta).data(dataInitial).build();
        stepForLogFunctional.firstStepForLogFunctional(codeOperation, acquisitionInitial);
        return stepForLogFunctional;
    }

    public void finallyStep(
            StepForLogFunctional stepForLogFunctional, String acquisitionId,
            InfoReuseCommon infoReuseCommon, String codeOperation) {
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

    public void validRequestQuestionList(ValidateQuestionsRequest validateQuestionsRequest) {
        List<InvalidOptionalArgumentException> listException = new ArrayList<>();
        List<Integer> index = new ArrayList<>();

        validateQuestionsRequest.getData().getAnswerList().forEach(list -> {
            String compl = "[" + index.size() + "]";
            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    list, new Class[]{ValidationMandatoryInputList.class},
                    ERROR_CODE_MANDATORY_LIST, compl, EMPTY));
            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    list, new Class[]{ValidationInvalidInputList.class},
                    ERROR_CODE_MANDATORY_INVALID_LIST, compl, EMPTY));
            index.add(1);
        });
        OptionalMandatoryArguments.validateException(listException);
    }
}
