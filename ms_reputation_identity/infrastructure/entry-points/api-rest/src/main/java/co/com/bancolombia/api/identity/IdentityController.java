package co.com.bancolombia.api.identity;

import co.com.bancolombia.ReputationIdentityController;
import co.com.bancolombia.ResponseFactory;
import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.model.ValidateIdentityRequest;
import co.com.bancolombia.api.model.ValidateIdentityRequestData;
import co.com.bancolombia.api.model.ValidateIdentityResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FINISH_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.START_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@ReputationIdentityController
@Api(tags = {"AcquisitionIdentity",})
public class IdentityController implements IdentityOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private GenericStep genericStep;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private ValidateIdentityUseCase validateIdentityUseCase;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_IDENTITY, OPER_VALIDATE_IDENTITY);

    @Override
    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_VALIDATE_IDENTITY)
    public ResponseEntity<ValidateIdentityResponse> validateIdentity(
            @ApiParam(value = "Validate identity customer", required = true) @Validated({ValidationMandatory.class,
                    ValidationAcquisitionId.class}) @RequestBody ValidateIdentityRequest body) {
        webRequest.setAttribute(META, body.getMeta(), SCOPE_REQUEST);
        UserInfoRequestData dataRequest = UserInfoRequestData.builder().acquisitionId(body.getData().getAcquisitionId())
                .documentType(body.getData().getDocumentType()).documentNumber(body.getData().getDocumentNumber())
                .build();
        adapter.info(START_OPERATION);
        StepForLogFunctional stepForLogFunctional = genericStep.firstStepForLogFunctional(dataRequest, body.getMeta(),
                CODE_VALIDATE_IDENTITY);
        ValidateIdentityRequestData data = body.getData();
        genericStep.validRequest(data);
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_VALIDATE_IDENTITY);
        ValidateIdentity validateIdentity = validateIdentityUseCase.startProcessValidateIdentity(acquisitionReply,
                BasicAcquisitionRequest.builder().messageId(body.getMeta().getMessageId())
                        .userTransaction(body.getMeta().getUsrMod()).build());
        adapter.info(FINISH_OPERATION);
        genericStep.finallyStep(stepForLogFunctional, data.getAcquisitionId(), validateIdentity.getInfoReuseCommon(),
                CODE_VALIDATE_IDENTITY);
        return new ResponseEntity<>(ResponseFactory.buildValidateIdentityResponse(body, validateIdentity),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Customer", HttpStatus.OK);
    }
}
