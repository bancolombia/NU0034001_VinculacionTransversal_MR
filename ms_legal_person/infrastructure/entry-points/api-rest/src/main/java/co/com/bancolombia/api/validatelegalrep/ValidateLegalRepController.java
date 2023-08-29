package co.com.bancolombia.api.validatelegalrep;

import co.com.bancolombia.LegalPersonController;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.validatelegalrep.ValidateLegalRepResponse;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.validatelegalrep.ValidateLegalRep;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.validatelegalrep.ValidateLegalRepUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_LEGAL_REP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@LegalPersonController
@Api(tags = { "AcquisitionValidation", })
public class ValidateLegalRepController implements ValidateLegalRepOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private ValidateLegalRepUseCase validateLegalRepUseCase;

    @Autowired
    private GenericStep genericStep;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Override
    @ILogRegister(api = Constants.API_CUSTOMER_VALUE, operation = CODE_VALIDATE_LEGAL_REP)
    public ResponseEntity<ValidateLegalRepResponse> validateLegalRep(
            @ApiParam(value = "Information related to the customer's legal representative", required = true)
            @Validated({ValidationMandatory.class}) @RequestBody UserInfoRequest body) {

        UserInfoRequestData data = body.getData();
        MetaRequest meta = body.getMeta();

        webRequest.setAttribute(META, meta, SCOPE_REQUEST);

        genericStep.firstStepForLogFunctional(data, meta, CODE_VALIDATE_LEGAL_REP);
        genericStep.validRequest(data);

        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition
                (data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_VALIDATE_LEGAL_REP);

        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build())
                .documentNumber(acquisitionReply.getDocumentNumber()).build();

        ValidateLegalRep validateLegalRep = validateLegalRepUseCase.startProcessValidateLegalRep
                (acquisition, CODE_VALIDATE_LEGAL_REP);

        genericStep.finallyStep(data.getAcquisitionId(), null, CODE_VALIDATE_LEGAL_REP);

        return new ResponseEntity<>(
                VLRResponseFactory.buildValidateLegalRepResponse(body, validateLegalRep), HttpStatus.OK);
    }
}
