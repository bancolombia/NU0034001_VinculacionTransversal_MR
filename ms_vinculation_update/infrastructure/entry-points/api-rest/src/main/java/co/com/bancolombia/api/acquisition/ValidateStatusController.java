package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.VinUpdateController;
import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.api.model.validatestatus.ValidateStatusResponse;
import co.com.bancolombia.api.response.StatusResponseFactory;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import co.com.bancolombia.commonsvnt.model.startlist.StartList;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.model.StepForLogClass;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
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

import java.util.List;
import java.util.UUID;

@VinUpdateController
@Api(tags = {"AcquisitionManagement",})
public class ValidateStatusController implements ValidateStatusOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CheckListUseCase checkListUseCase;

    @Autowired
    private AcquisitionUseCase acquisitionUseCase;

    @Autowired
    private ILogFuncAcquisitionUseCase iLogFuncAcquisitionUseCase;

    @Autowired
    private ILogFuncFieldUseCase iLogFuncFieldUseCase;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private ILogFuncCheckListUseCase iLogFuncCheckListUseCase;

    @ILogRegister(api = Constants.API_CUSTOMER_VALUE, operation = Constants.CODE_VALIDATE_STATUS)
    public ResponseEntity<ValidateStatusResponse> validateStatus(
            @ApiParam(value = "Information related to validate status", required = true)
            @Validated({ValidationMandatory.class}) @RequestBody UserInfoRequest body) {

        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().webRequest(webRequest)
                .acquisitionUseCase(iLogFuncAcquisitionUseCase).fieldUseCase(iLogFuncFieldUseCase)
                .coreFunctionDate(coreFunctionDate).checkListUseCase(iLogFuncCheckListUseCase).build();

        UUID acqId = stepForLogFunctional.firstStepForLogFunctional(Constants.CODE_VALIDATE_STATUS,
                AcquisitionInitial.builder().meta(body.getMeta())
                        .data(MyDataInitial.builder().documentNumber(body.getData().getDocumentNumber())
                                .documentType(body.getData().getDocumentType()).build())
                        .build());

        UUID acqIdFound = body.getData().getAcquisitionId() != null ?
                UUID.fromString(body.getData().getAcquisitionId()) : null;

        List<StartList> ret = checkListUseCase.getProcessesCheckList(acqIdFound, body.getData().getDocumentType(),
                body.getData().getDocumentNumber(), Constants.CODE_VALIDATE_STATUS);

        Acquisition acquisition = null;
        if (acqId != null) {
            acquisition = acquisitionUseCase.findById(acqId);
        } else {
            if (!ret.isEmpty()) {
                acquisition = acquisitionUseCase.findById(UUID.fromString(ret.get(0).getAcquisitionId()));
            }
        }

        if (acquisition != null) {
            stepForLogFunctional.finallyStepForLogFunctional(StepForLogClass.builder()
                    .idAcquisition(acquisition.getId()).operation(Constants.CODE_VALIDATE_STATUS).build());
        }

        return new ResponseEntity<>(StatusResponseFactory.buildValidateStatusResponse(body, ret), HttpStatus.OK);
    }
}
