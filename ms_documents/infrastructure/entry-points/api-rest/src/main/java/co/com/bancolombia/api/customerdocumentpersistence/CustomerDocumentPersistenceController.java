package co.com.bancolombia.api.customerdocumentpersistence;

import co.com.bancolombia.DocumentsController;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.customerdocumentpersistence.CustomerPersistenceDocumentRequest;
import co.com.bancolombia.api.model.customerdocumentpersistence.CustomerPersistenceDocumentRequestData;
import co.com.bancolombia.api.model.customerdocumentpersistence.PersistenceDocumentResponse;
import co.com.bancolombia.api.response.PersistenceDocumentResponseFactory;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.persistencedocument.PersistenceDocumentUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CUSTOMER_DOCUMENT_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@DocumentsController
@Api(tags = {"AcquisitionDocuments",})
public class CustomerDocumentPersistenceController implements CustomerDocumentPersistenceOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private PersistenceDocumentUseCase persistenceDocumentUseCase;

    @Autowired
    private GenericStep genericStep;

    @Autowired
    private CustomerDocumentPersistenceLogController dPLogController;

    @Override
    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_CUSTOMER_DOCUMENT_PERSISTENCE)
    public ResponseEntity<PersistenceDocumentResponse> customerDocumentPersistence(
            @ApiParam(value = "Information related to customer document persistence", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody CustomerPersistenceDocumentRequest body) {

        CustomerPersistenceDocumentRequestData data = body.getData();
        MetaRequest meta = body.getMeta();
        webRequest.setAttribute(META, meta, SCOPE_REQUEST);
        StepForLogFunctional stepForLogFunctional = genericStep.firstStepForLogFunctional(data, meta,
                CODE_CUSTOMER_DOCUMENT_PERSISTENCE);
        genericStep.validRequest(data);
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(data.getAcquisitionId(),
                data.getDocumentType(), data.getDocumentNumber(), CODE_CUSTOMER_DOCUMENT_PERSISTENCE);
        PersistenceDocument persistenceDocument = persistenceDocumentUseCase.startProcess(data.getDocumentCode(),
                acquisitionReply, meta.getMessageId(), meta.getUsrMod());
        persistenceDocumentUseCase.processResponse(acquisitionReply, persistenceDocument, Boolean.FALSE);
        dPLogController.finallyStep(stepForLogFunctional, acquisitionReply, persistenceDocument);
        return new ResponseEntity<>(PersistenceDocumentResponseFactory.buildPersistenceDocumentResponse(body),
                HttpStatus.OK);
    }
}
