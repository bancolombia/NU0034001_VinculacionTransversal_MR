package co.com.bancolombia.api.retrievescenario;


import co.com.bancolombia.VinUpdateController;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.api.response.ResponseFactory;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.model.StepForLogClass;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;
import java.util.UUID;

@VinUpdateController
@Api(tags = {"AcquisitionManagement",})
public class RetrieveScenarioController implements RetrieveScenarioOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private ILogFuncAcquisitionUseCase iLogFuncAcquisitionUseCase;

    @Autowired
    private ILogFuncFieldUseCase iLogFuncFieldUseCase;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private AcquisitionValidateUseCase acquisitionValidateUseCase;

    @ILogRegister(api = Constants.API_CUSTOMER_VALUE, operation = Constants.CODE_RETRIEVE_SCENARIO)
    public ResponseEntity<CodeNameResponse> retrieveScenario(
            @ApiParam(value = "Information related to acquisition scenario", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody UserInfoRequest body) {

        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().webRequest(webRequest)
                .acquisitionUseCase(iLogFuncAcquisitionUseCase).fieldUseCase(iLogFuncFieldUseCase)
                .coreFunctionDate(coreFunctionDate).build();

        UUID acqId = stepForLogFunctional.firstStepForLogFunctional(Constants.CODE_RETRIEVE_SCENARIO,
                AcquisitionInitial.builder().meta(body.getMeta())
                        .data(MyDataInitial.builder().documentNumber(body.getData().getDocumentNumber())
                                .acquisitionId(body.getData().getAcquisitionId())
                                .documentType(body.getData().getDocumentType()).build())
                        .build());

        Optional<Acquisition> acquisition = acquisitionValidateUseCase.validateInfoSearchAndGet(
                body.getData().getAcquisitionId(), body.getData().getDocumentType(),
                body.getData().getDocumentNumber(), Constants.CODE_RETRIEVE_SCENARIO);

        stepForLogFunctional.finallyStepForLogFunctional(StepForLogClass.builder().idAcquisition(acqId)
                .operation(Constants.CODE_RETRIEVE_SCENARIO).build());

        return new ResponseEntity<>(ResponseFactory.buildCodeNameResponse(body.getMeta()), HttpStatus.OK);
    }
}