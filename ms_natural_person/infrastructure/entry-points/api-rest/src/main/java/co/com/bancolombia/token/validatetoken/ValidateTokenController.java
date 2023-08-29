package co.com.bancolombia.token.validatetoken;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenRequestData;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenResponse;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.token.ResponseFactoryToken;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_TOKEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class ValidateTokenController implements ValidateTokenOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private ValidateTokenUseCase validateTokenUseCase;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private GenericStep genericStep;


    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_VALIDATE_TOKEN)
    public ResponseEntity<ValidateTokenResponse> validateToken(
            @ApiParam(value = "Information related to validate token", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody ValidateTokenRequest body) {

        ValidateTokenRequestData data = body.getData();
        MetaRequest meta = body.getMeta();

        webRequest.setAttribute(META, meta, SCOPE_REQUEST);

        genericStep.firstStepForLogFunctional(data, meta, CODE_VALIDATE_TOKEN);
        genericStep.validRequest(data);

        ValidateToken validateToken = startProcess(data, meta);

        genericStep.finallyStep(data.getAcquisitionId(), validateToken.getInfoReuseCommon(), CODE_VALIDATE_TOKEN);
        return new ResponseEntity<>(
                ResponseFactoryToken.buildValidateTokenResponse(body, validateToken), HttpStatus.OK);
    }

    public ValidateToken startProcess(ValidateTokenRequestData data, MetaRequest meta) {
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(),
                data.getDocumentNumber(), CODE_VALIDATE_TOKEN);

        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .typeAcquisition(TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .documentType(DocumentType.builder().codeHomologation(acquisitionReply.getDocumentTypeCodeGenericType())
                        .build())
                .build();

        ValidateToken validateToken = ValidateToken.builder()
                .acquisition(acquisition).tokenCode(data.getTokenCode()).build();

        BasicAcquisitionRequest ba = BasicAcquisitionRequest.builder()
                .idAcq(data.getAcquisitionId()).documentType(data.getDocumentType())
                .documentNumber(data.getDocumentNumber()).userTransaction(meta.getUsrMod())
                .messageId(meta.getMessageId()).build();
        return validateTokenUseCase.startProcessValidateToken(ba, validateToken);
    }
}