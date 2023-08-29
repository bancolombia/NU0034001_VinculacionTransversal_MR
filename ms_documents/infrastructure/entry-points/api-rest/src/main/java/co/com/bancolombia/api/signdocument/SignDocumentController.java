package co.com.bancolombia.api.signdocument;

import co.com.bancolombia.DocumentsController;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.signdocument.SDRequest;
import co.com.bancolombia.api.model.signdocument.SDRequestData;
import co.com.bancolombia.api.model.signdocument.SDResponse;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.signdocument.SignDocument;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.signdocument.SignDocumentUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_SIGNDOCUMENT;

@DocumentsController
@Api(tags = {"AcquisitionDocuments"})
public class SignDocumentController implements SignDocumentOperations {

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private SignDocumentUseCase signDocumentUseCase;

    @Autowired
    private GenericStep genericStep;

    @ILogRegister(api = Constants.API_CUSTOMER_VALUE, operation = CODE_SIGNDOCUMENT)
    public ResponseEntity<SDResponse> signDocument(
            @ApiParam(value = "Information related to sign document", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody SDRequest body) throws IOException, MessagingException {
        SDRequestData data = body.getData();
        MetaRequest meta = body.getMeta();
        StepForLogFunctional stepForLogFunctional = genericStep.firstStepForLogFunctional(data, meta,
                CODE_SIGNDOCUMENT);
        genericStep.validRequest(data);
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(data.getAcquisitionId(),
                data.getDocumentType(), data.getDocumentNumber(), CODE_SIGNDOCUMENT);
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType())
                        .codeGenericType(acquisitionReply.getDocumentTypeCodeGenericType()).build())
                .typeAcquisition(TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .build();
        SDRequestTxt sdRequestTxt = SDRequestTxt.builder().ip(meta.getIp()).messageId(meta.getMessageId())
                .dateRequest("").usrMod(meta.getUsrMod()).build();
        SignDocument signDocument = signDocumentUseCase.startProcess(acquisition, sdRequestTxt);
        genericStep.finallyStep(stepForLogFunctional, data.getAcquisitionId(), signDocument.getInfoReuseCommon(),
                CODE_SIGNDOCUMENT);
        return new ResponseEntity<>(SignDocumentResponseFactory.buildSignDocumentResponse(body), HttpStatus.OK);
    }
}