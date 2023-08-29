package co.com.bancolombia.logfunctionalvnt.log.util;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.util.ConstantLog;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;
import co.com.bancolombia.logfunctionalvnt.log.model.LogObjectAttribute;
import co.com.bancolombia.logfunctionalvnt.log.model.StepForLogClass;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Builder
public class StepForLogFunctional {

    private WebRequest webRequest;
    private ILogFuncAcquisitionUseCase acquisitionUseCase;
    private ILogFuncFieldUseCase fieldUseCase;
    private CoreFunctionDate coreFunctionDate;
    private ILogFuncCheckListUseCase checkListUseCase;

    public UUID firstStepForLogFunctional(String operation, AcquisitionInitial body) {
        webRequest.setAttribute(Constants.META, body.getMeta(), RequestAttributes.SCOPE_REQUEST);
        webRequest.setAttribute(ConstantLog.LOGFIELD_CODE_STEP, operation, RequestAttributes.SCOPE_REQUEST);
        if (body.getData() != null) {
            UUID acqId = body.getData().getAcquisitionId() != null ? UUID.fromString(body.getData().getAcquisitionId())
                    : null;
            if (acqId != null) {
                webRequest.setAttribute(ConstantLog.LOGFIELD_ID_ACQUISITION, acqId.toString(),
                        RequestAttributes.SCOPE_REQUEST);
                webRequest.setAttribute(ConstantLog.LOGFIELD_NRO_DOCUMENT, body.getData().getDocumentNumber(),
                        RequestAttributes.SCOPE_REQUEST);
                webRequest.setAttribute(ConstantLog.LOGFIELD_TYPE_DOCUMENT, body.getData().getDocumentType(),
                        RequestAttributes.SCOPE_REQUEST);}
            LogObjectAttribute lobAttribute = LogObjectAttribute.builder()
                    .fieldNotSve(fieldUseCase.fieldsCanNotSaveByOperation(operation))
                    .dateInit(coreFunctionDate.getDatetime())
                    .documentNumber(body.getData().getDocumentNumber())
                    .documentType(body.getData().getDocumentType())
                    .acquisitionId((acqId != null) ? acqId.toString() : null).build();
            try {
                lobAttribute.setCreatedBy(((MetaRequest) body.getMeta()).getUsrMod());
            } catch (ClassCastException e) {
                lobAttribute.setCreatedBy("USUARIO");
            }
            webRequest.setAttribute(ConstantLog.LOGFIELD_OBJECT_ATTRIBUTE, lobAttribute,
                    RequestAttributes.SCOPE_SESSION);
            return acqId;
        }
        return null;
    }

    public void finallyStepForLogFunctional_old(StepForLogClass stepForLogClass) {
        String stateAcquisition = null;
        String stateOperation = null;
        Acquisition acq = null;
        if (stepForLogClass.getIdAcquisition() != null &&
                stepForLogClass.getIdAcquisition().toString().length()>0) {
            acq = acquisitionUseCase.findByIdWitOutState(stepForLogClass.getIdAcquisition());
            if (acq != null) {
                stateAcquisition = acq.getStateAcquisition().getName();
                CheckList checkList = checkListUseCase.getCheckListByAcquisition(acq).stream()
                        .filter(item -> item.getStep().getCode().equals(stepForLogClass.getOperation())).findFirst()
                        .orElse(null);

                if (checkList != null) {
                    stateOperation = checkList.getState().getName();
                }

            }

        }
        if (stateAcquisition != null) {
            webRequest.setAttribute(ConstantLog.LOGFIELD_STATE_ACQUISITION, stateAcquisition,
                    RequestAttributes.SCOPE_SESSION);
        }
        if (stateOperation != null) {
            webRequest.setAttribute(ConstantLog.LOGFIELD_STATE_OPERATION, stateOperation,
                    RequestAttributes.SCOPE_SESSION);
        }

        this.continueValidation(stepForLogClass);

    }


    public void finallyStepForLogFunctional(StepForLogClass stepForLogClass){
        String stateAcquisition = null;
        String stateOperation = null;
        Acquisition acq = null;
        if (stepForLogClass.getIdAcquisition() != null &&
                stepForLogClass.getIdAcquisition().toString().length()>0) {

            AcquisitionStateQuery acquisitionStateQuery = AcquisitionStateQuery.builder()
                    .acquisitionId(stepForLogClass.getIdAcquisition().toString())
                    .operation(stepForLogClass.getOperation())
                    .build();

            AcquisitionStateReply acquisitionStateReply = this.acquisitionUseCase
                    .acquisitionStateReply(acquisitionStateQuery);

            if(acquisitionStateReply != null){
                stateAcquisition = acquisitionStateReply.getStateAcquisitionName();
                stateOperation = acquisitionStateReply.getStateStepName();
            }

        }
        if (stateAcquisition != null) {
            webRequest.setAttribute(ConstantLog.LOGFIELD_STATE_ACQUISITION, stateAcquisition,
                    RequestAttributes.SCOPE_SESSION);
        }
        if (stateOperation != null) {
            webRequest.setAttribute(ConstantLog.LOGFIELD_STATE_OPERATION, stateOperation,
                    RequestAttributes.SCOPE_SESSION);
        }

        this.continueValidation(stepForLogClass);
    }

    public void continueValidation(StepForLogClass stepForLogClass) {

        LogFunctionalReuse logFunctionalReuse = LogFunctionalReuse.builder()
                .requestReuse(stepForLogClass.getRequestReuse()).responseReuse(stepForLogClass.getResponseReuse())
                .dateRequest(stepForLogClass.getRequestDateReuse()).dateResponse(stepForLogClass.getResponseDateReuse())
                .build();
        webRequest.setAttribute(ConstantLog.LOGFIELD_REUSE_INFO, logFunctionalReuse, RequestAttributes.SCOPE_SESSION);

        if (stepForLogClass.getMapaField() != null) {

            JsonObject mapData = new JsonObject();
            for (Map.Entry<String, String> entry : stepForLogClass.getMapaField().entrySet()) {
                mapData.addProperty(entry.getKey(), entry.getValue());

            }

            JsonObject jsonObjectGrand = new JsonObject();
            jsonObjectGrand.add(ConstantLog.LOGFIELD_ADDITIONAL_DATA, mapData);

            webRequest.setAttribute(ConstantLog.LOGFIELD_OTHER_FIELD_DATA, jsonObjectGrand.toString(),
                    RequestAttributes.SCOPE_SESSION);
        }

    }

}
