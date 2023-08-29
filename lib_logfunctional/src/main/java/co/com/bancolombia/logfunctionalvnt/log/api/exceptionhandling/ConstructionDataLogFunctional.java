package co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.util.ConstantLog;
import co.com.bancolombia.logfunctionalvnt.log.model.LogObjectAttribute;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstructionDataLogFunctional extends ResponseEntityExceptionHandler {

    private static final String STATEACQUISITIONSTR = "stateAcquisition";
    private static final String STATEOPERATIONSTR = "stateOperation";

    @Autowired
    ILogFuncAcquisitionUseCase iLogFuncAcquisitionUseCase;

    @Autowired
    CoreFunctionDate coreFunctionDate;

    @Autowired
    ILogFuncFieldUseCase iLogFuncFieldUseCase;

    LoggerAdapter adapter = new LoggerAdapter(Constants.MY_APP, "ConstrucDataLogFunctional",
            ConstructionDataLogFunctional.class.toString());

    public void removeAttributeLogFunctional(WebRequest request) {
        request.removeAttribute(ConstantLog.LOGFIELD_ID_ACQUISITION, RequestAttributes.SCOPE_SESSION);
        request.removeAttribute(ConstantLog.LOGFIELD_NRO_DOCUMENT, RequestAttributes.SCOPE_SESSION);
        request.removeAttribute(ConstantLog.LOGFIELD_TYPE_DOCUMENT, RequestAttributes.SCOPE_REQUEST);
        request.removeAttribute(ConstantLog.LOGFIELD_CODE_STEP, RequestAttributes.SCOPE_REQUEST);

    }

    public void addAttributeLogFunctional(String stateAcquisition, String stateOperation, WebRequest request) {
        if (stateAcquisition != null) {
            request.setAttribute(ConstantLog.LOGFIELD_STATE_ACQUISITION, stateAcquisition,
                    RequestAttributes.SCOPE_SESSION);
        }
        if (stateOperation != null) {
            request.setAttribute(ConstantLog.LOGFIELD_STATE_OPERATION, stateOperation, RequestAttributes.SCOPE_SESSION);
        }

    }

    public void constructionDataLogFunctional(WebRequest request) {
        this.contructionLogAttribute(request);

        String idAcquisition = null;
        String documentNumber = null;
        String documentType = null;
        String stateAcquisition = null;
        String stateOperation = null;

        try {
            idAcquisition = (String) request.getAttribute(ConstantLog.LOGFIELD_ID_ACQUISITION,
                    RequestAttributes.SCOPE_REQUEST);
            documentNumber = (String) request.getAttribute(ConstantLog.LOGFIELD_NRO_DOCUMENT,
                    RequestAttributes.SCOPE_REQUEST);
            documentType = (String) request.getAttribute(ConstantLog.LOGFIELD_TYPE_DOCUMENT,
                    RequestAttributes.SCOPE_REQUEST);
            String codeStepTry = (String) request.getAttribute(ConstantLog.LOGFIELD_CODE_STEP,
                    RequestAttributes.SCOPE_REQUEST);

            if (codeStepTry == null) {
                adapter.info("Error codeStepTry Null");
            }

        } catch (CustomException e) {
            adapter.info("Error conversion: " + e.getMessage());
        }

        Map<String, String> mapStates = this.stateAcquisitionAndOperation(request, idAcquisition, documentNumber,
                documentType);

        stateAcquisition = mapStates.get(STATEACQUISITIONSTR);
        stateOperation = mapStates.get(STATEOPERATIONSTR);

        this.addAttributeLogFunctional(stateAcquisition, stateOperation, request);

    }

    public Map<String, String> stateAcquisitionAndOperation(WebRequest request, String idAcquisition,
            String documentNumber, String documentType) {

        String stateAcquisition = null;
        String stateOperation = null;

        String codeStep = (String) request.getAttribute(ConstantLog.LOGFIELD_CODE_STEP, RequestAttributes.SCOPE_REQUEST);

        if (idAcquisition != null) {
            Acquisition acquisition = null;
            AcquisitionStateQuery acquisitionStateQuery;
            if (documentNumber != null && documentType != null) {
                acquisitionStateQuery = AcquisitionStateQuery.builder()
                        .documentNumber(documentNumber)
                        .documentType(documentType)
                        .operation(codeStep)
                        .build();
            } else {
                acquisitionStateQuery = AcquisitionStateQuery.builder()
                        .acquisitionId(idAcquisition)
                        .operation(codeStep)
                        .build();
            }


            AcquisitionStateReply acquisitionStateReply = this.iLogFuncAcquisitionUseCase
                    .acquisitionStateReply(acquisitionStateQuery);

            if(acquisitionStateReply != null){
                stateAcquisition = acquisitionStateReply.getStateAcquisitionName();
                stateOperation = acquisitionStateReply.getStateStepName();
            }

        }

        Map<String, String> result = new HashMap<>();
        result.put(STATEACQUISITIONSTR, stateAcquisition);
        result.put(STATEOPERATIONSTR, stateOperation);

        this.removeAttributeLogFunctional(request);
        return result;
    }

    public void contructionLogAttribute(WebRequest request) {
        Map<String, List<String>> list = iLogFuncFieldUseCase.fieldsAllCanNotSaveByActive(true);

        LogObjectAttribute lobAttribute = LogObjectAttribute.builder().fieldNotSve(list)
                .dateInit(coreFunctionDate.getDatetime()).acquisitionId(null).documentNumber(null).documentType(null)
                .build();

        if (lobAttribute != null) {
            request.setAttribute(ConstantLog.LOGFIELD_OBJECT_ATTRIBUTE, lobAttribute, RequestAttributes.SCOPE_SESSION);
        }

    }

}
