package co.com.bancolombia.api.customerdocumentpersistence;

import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CUSTOMER_DOCUMENT_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FIRST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DETAIL_FIRST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DETAIL_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTAL_SUB_TYPE_CODE_FIRST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTAL_SUB_TYPE_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTAL_TYPE_CODE_FIRST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTAL_TYPE_CODE_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_ID_FIRST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_ID_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_MESSAGE_PUBLISHING_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FAIL_RESULT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT_APPLY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_APPLY_SIMULATED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OK_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PUBLISHING_RESULT_FIRST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PUBLISHING_RESULT_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SUCCESFUL_RESULT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TITTLE_FIRST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TITTLE_SECOND;

@AllArgsConstructor
public class CustomerDocumentPersistenceLogController {

    @Autowired
    private GenericStep genericStep;

    private static LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE,
            OPER_CUSTOMER_DOC_PERSISTENCE);

    public void finallyStep(StepForLogFunctional stepForLogFunctional, AcquisitionReply acquisition,
                            PersistenceDocument persistenceDocumentApiResponse) {
        InfoReuseCommon infoReuseCommon = persistenceDocumentApiResponse.getInfoReuseCommon();
        Map<String, String> additionalFields = new HashMap<>();
        List<PersistenceDocumentList> persistenceDocumentList = persistenceDocumentApiResponse.getData();
        if (persistenceDocumentList != null) {
            if (persistenceDocumentList.size() > Numbers.ONE.getIntNumber()) {
                additionalFields = additionalFieldsAllDocuments(persistenceDocumentList);
            } else {
                additionalFields = additionalFieldsOneDocument(persistenceDocumentList);
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
                CODE_CUSTOMER_DOCUMENT_PERSISTENCE);
    }

    public Map<String, String> additionalFieldsAllDocuments(List<PersistenceDocumentList> persistenceDocumentList) {
        Map<String, String> map = new LinkedHashMap<>();
        PersistenceDocumentList cc = persistenceDocumentList.get(0);
        PersistenceDocumentList rut = persistenceDocumentList.get(1);
        if (cc.getStatus().equals(OK_STATE) && rut.getStatus().equals(OK_STATE)) {
            map.put(DOCUMENTAL_TYPE_CODE_FIRST, cc.getTypeDocumentary());
            map.put(DOCUMENTAL_SUB_TYPE_CODE_FIRST, cc.getSubTypeDocumentary());
            map.put(DOCUMENT_ID_FIRST, cc.getIdDocument());
            map.put(PUBLISHING_RESULT_FIRST, SUCCESFUL_RESULT);
            map.put(CODE_FIRST, NOT_APPLY);
            map.put(TITTLE_FIRST, NOT_APPLY);
            map.put(DETAIL_FIRST, NOT_APPLY);
            map.put(DOCUMENTAL_TYPE_CODE_SECOND, rut.getTypeDocumentary());
            map.put(DOCUMENTAL_SUB_TYPE_SECOND, rut.getSubTypeDocumentary());
            map.put(DOCUMENT_ID_SECOND, rut.getIdDocument());
            map.put(PUBLISHING_RESULT_SECOND, SUCCESFUL_RESULT);
            map.put(CODE_SECOND, NOT_APPLY);
            map.put(TITTLE_SECOND, NOT_APPLY);
            map.put(DETAIL_SECOND, NOT_APPLY);

            return map;
        } else {
            return this.additionalFieldsAllDocumentsOtherCases(map, cc, rut);
        }

    }

    private Map<String, String> additionalFieldsAllDocumentsOtherCases(Map<String, String> map,
                                                                       PersistenceDocumentList cc,
                                                                       PersistenceDocumentList rut) {
        if (cc.getStatus().equals(ERROR_STATE) && rut.getIdDocument().equals(NO_APPLY_SIMULATED)) {
            map.put(DOCUMENTAL_TYPE_CODE_FIRST, cc.getTypeDocumentary());
            map.put(DOCUMENTAL_SUB_TYPE_CODE_FIRST, cc.getSubTypeDocumentary());
            map.put(DOCUMENT_ID_FIRST, NOT_APPLY);
            map.put(PUBLISHING_RESULT_FIRST, FAIL_RESULT);
            map.put(CODE_FIRST, cc.getErrorCode());
            map.put(TITTLE_FIRST, ERROR_MESSAGE_PUBLISHING_DOCUMENTS);
            map.put(DETAIL_FIRST, cc.getErrorDescription());
            map.put(DOCUMENTAL_TYPE_CODE_SECOND, rut.getTypeDocumentary());
            map.put(DOCUMENTAL_SUB_TYPE_SECOND, rut.getSubTypeDocumentary());
            map.put(DOCUMENT_ID_SECOND, NOT_APPLY);
            map.put(PUBLISHING_RESULT_SECOND, FAIL_RESULT);
            map.put(CODE_SECOND, NOT_APPLY);
            map.put(TITTLE_SECOND, NOT_APPLY);
            map.put(DETAIL_SECOND, NOT_APPLY);
            return map;
        } else {
            return this.additionalFieldsAllDocumentsRutError(map, cc, rut);
        }
    }

    private Map<String, String> additionalFieldsAllDocumentsRutError(Map<String, String> map,
                                                                     PersistenceDocumentList cc,
                                                                     PersistenceDocumentList rut) {
        if (cc.getStatus().equals(OK_STATE) && rut.getStatus().equals(ERROR_STATE)) {
            map.put(DOCUMENTAL_TYPE_CODE_FIRST, cc.getTypeDocumentary());
            map.put(DOCUMENTAL_SUB_TYPE_CODE_FIRST, cc.getSubTypeDocumentary());
            map.put(DOCUMENT_ID_FIRST, cc.getIdDocument());
            map.put(PUBLISHING_RESULT_FIRST, SUCCESFUL_RESULT);
            map.put(CODE_FIRST, NOT_APPLY);
            map.put(TITTLE_FIRST, NOT_APPLY);
            map.put(DETAIL_FIRST, NOT_APPLY);
            map.put(DOCUMENTAL_TYPE_CODE_SECOND, rut.getTypeDocumentary());
            map.put(DOCUMENTAL_SUB_TYPE_SECOND, rut.getSubTypeDocumentary());
            map.put(DOCUMENT_ID_SECOND, NOT_APPLY);
            map.put(PUBLISHING_RESULT_SECOND, FAIL_RESULT);
            map.put(CODE_SECOND, rut.getErrorCode());
            map.put(TITTLE_SECOND, ERROR_MESSAGE_PUBLISHING_DOCUMENTS);
            map.put(DETAIL_SECOND, rut.getErrorDescription());
        }
        return map;
    }

    public Map<String, String> additionalFieldsOneDocument(List<PersistenceDocumentList> persistenceDocumentList) {
        Map<String, String> map = new LinkedHashMap<>();
        PersistenceDocumentList document = persistenceDocumentList.get(0);
        if (document.getSubTypeDocumentary().equals(CEDULA_SUBTYPE)) {
            map.put(DOCUMENTAL_TYPE_CODE_FIRST, document.getTypeDocumentary());
            map.put(DOCUMENTAL_SUB_TYPE_CODE_FIRST, document.getSubTypeDocumentary());
            map.put(DOCUMENT_ID_FIRST, document.getStatus().equals(OK_STATE) ? document.getIdDocument() : NOT_APPLY);
            map.put(PUBLISHING_RESULT_FIRST, document.getStatus().equals(OK_STATE) ? SUCCESFUL_RESULT : FAIL_RESULT);
            map.put(CODE_FIRST, document.getStatus().equals(OK_STATE) ? NOT_APPLY : document.getErrorCode());
            map.put(TITTLE_FIRST, document.getStatus().equals(OK_STATE) ? NOT_APPLY
                    : ERROR_MESSAGE_PUBLISHING_DOCUMENTS);
            map.put(DETAIL_FIRST, document.getStatus().equals(OK_STATE) ? NOT_APPLY : document.getErrorDescription());
            map.put(DOCUMENTAL_TYPE_CODE_SECOND, NOT_APPLY);
            map.put(DOCUMENTAL_SUB_TYPE_SECOND, NOT_APPLY);
            map.put(DOCUMENT_ID_SECOND, NOT_APPLY);
            map.put(PUBLISHING_RESULT_SECOND, NOT_APPLY);
            map.put(CODE_SECOND, NOT_APPLY);
            map.put(TITTLE_SECOND, NOT_APPLY);
            map.put(DETAIL_SECOND, NOT_APPLY);

            return map;
        } else {
            return this.additionalFieldsOneDocumentRut(map, persistenceDocumentList);
        }

    }

    private Map<String, String> additionalFieldsOneDocumentRut(Map<String, String> map,
                                                               List<PersistenceDocumentList> persistenceDocumentList) {
        PersistenceDocumentList document = persistenceDocumentList.get(0);
        if (document.getSubTypeDocumentary().equals(RUT_SUBTYPE)) {
            map.put(DOCUMENTAL_TYPE_CODE_FIRST, NOT_APPLY);
            map.put(DOCUMENTAL_SUB_TYPE_CODE_FIRST, NOT_APPLY);
            map.put(DOCUMENT_ID_FIRST, NOT_APPLY);
            map.put(PUBLISHING_RESULT_FIRST, NOT_APPLY);
            map.put(CODE_FIRST, NOT_APPLY);
            map.put(TITTLE_FIRST, NOT_APPLY);
            map.put(DETAIL_FIRST, NOT_APPLY);
            map.put(DOCUMENTAL_TYPE_CODE_SECOND, document.getTypeDocumentary());
            map.put(DOCUMENTAL_SUB_TYPE_SECOND, document.getSubTypeDocumentary());
            map.put(DOCUMENT_ID_SECOND, document.getStatus().equals(OK_STATE) ? document.getIdDocument() : NOT_APPLY);
            map.put(PUBLISHING_RESULT_SECOND, document.getStatus().equals(OK_STATE) ? SUCCESFUL_RESULT : FAIL_RESULT);
            map.put(CODE_SECOND, document.getStatus().equals(OK_STATE) ? NOT_APPLY : document.getErrorCode());
            map.put(TITTLE_SECOND, document.getStatus().equals(OK_STATE) ? NOT_APPLY
                    : ERROR_MESSAGE_PUBLISHING_DOCUMENTS);
            map.put(DETAIL_SECOND, document.getStatus().equals(OK_STATE) ? NOT_APPLY : document.getErrorDescription());
        }
        return map;
    }
}
