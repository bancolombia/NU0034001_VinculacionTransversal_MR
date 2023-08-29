package co.com.bancolombia.api.customerdocumentpersistence;

import co.com.bancolombia.DocumentsController;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.customerdocumentpersistence.CustomerPersistenceDocumentRequestData;
import co.com.bancolombia.api.model.customerdocumentpersistence.DocumentPersistenceRetriesRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.persistencedocument.PersistenceDocumentUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CUSTOMER_DOCUMENT_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_CODE_ALL_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.util.constants.Constants.CODE_RETRIES_DOCUMENT_PERSISTENCE;
import static co.com.bancolombia.util.constants.Constants.TDC_RETRIES;

@DocumentsController
@Api(tags = {"AcquisitionDocuments",})
public class CustomerDocumentPersistenceRetriesController implements PersistenceDocumentRetriesOperation {

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private PersistenceDocumentUseCase persistenceDocumentUseCase;

    @Autowired
    private CustomerDocumentPersistenceLogController dPLogController;

    @Autowired
    private GenericStep genericStep;

    private static final LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE,
            TDC_RETRIES);

    @Override
    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_RETRIES_DOCUMENT_PERSISTENCE)
    public void persistenceDocumentOperation(@Valid DocumentPersistenceRetriesRequest body) {
        adapter.info("Connection established from Documents Retries");
        body.getData().forEach(pDocument -> {
            String acquisitionId = pDocument.getData().get(0).getAcquisitionId().toString();
            String documentNumber = pDocument.getData().get(0).getDocumentNumber();
            CustomerPersistenceDocumentRequestData data = CustomerPersistenceDocumentRequestData.builder()
                    .acquisitionId(acquisitionId).documentNumber(documentNumber)
                    .documentCode(codeRequest(pDocument.getData())).documentType(pDocument.getDocumentType()).build();
            adapter.info("Retries result related to AcquisitionId" +
                    acquisitionId);
            StepForLogFunctional stepForLogFunctional = genericStep.firstStepForLogFunctional(data, null,
                    CODE_RETRIES_DOCUMENT_PERSISTENCE);
            AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(acquisitionId,
                    documentNumber, pDocument.getDocumentType(), CODE_CUSTOMER_DOCUMENT_PERSISTENCE);
            persistenceDocumentUseCase.processResponse(acquisitionReply, pDocument, Boolean.TRUE);
            functionalLog(acquisitionReply, pDocument, stepForLogFunctional);
        });
    }

    public String codeRequest(List<PersistenceDocumentList> documentLists) {
        if (documentLists.size() > 1) {
            return DOCUMENT_CODE_ALL_DOCUMENTS;
        } else {
            if (CEDULA_SUBTYPE.equals(documentLists.get(0).getSubTypeDocumentary())) {
                return CEDULA_SUBTYPE;
            } else {
                return RUT_SUBTYPE;
            }
        }
    }

    public void functionalLog(AcquisitionReply acquisition, PersistenceDocument persistenceDocumentWithLog,
                              StepForLogFunctional stepForLogFunctional) {
        InfoReuseCommon infoReuseCommon = persistenceDocumentWithLog.getInfoReuseCommon();
        Map<String, String> additionalFields = new HashMap<>();
        List<PersistenceDocumentList> persistenceDocumentList = persistenceDocumentWithLog.getData();

        if (persistenceDocumentList != null) {
            if (persistenceDocumentList.size() > Numbers.ONE.getIntNumber()) {
                additionalFields = dPLogController.additionalFieldsAllDocuments(persistenceDocumentList);
            } else {
                additionalFields = dPLogController.additionalFieldsOneDocument(persistenceDocumentList);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();

        String json = EMPTY;
        try {
            json = objectMapper.writeValueAsString(additionalFields);
        } catch (JsonProcessingException e) {
            adapter.error("Error json process", e);
        }

        infoReuseCommon.setResponseReuse(json);
        genericStep.finallyStep(stepForLogFunctional, acquisition.getAcquisitionId(), infoReuseCommon,
                CODE_RETRIES_DOCUMENT_PERSISTENCE);
    }
}
