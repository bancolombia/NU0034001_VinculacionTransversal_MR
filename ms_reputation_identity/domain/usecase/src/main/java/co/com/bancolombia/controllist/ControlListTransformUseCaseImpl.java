package co.com.bancolombia.controllist;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.controllist.ControlList;
import co.com.bancolombia.model.controllist.ControlListResponse;
import co.com.bancolombia.model.controllist.DataControlList;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONTROL_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_CODE_SIX_NOT_IN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_CODE_THREE_BLOQ;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_CODE_TWO_ALERT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_CTR_ALERT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_MESSAGE_CONTINUE_ALERT_BLOQ;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_MESSAGE_NOT_IN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_STR_BLOQ;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_VIGENTE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NAME_CONTROL_LIST_OPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STATE_EXTERNAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CTR_LIST_CODE_ERROR;

@RequiredArgsConstructor
public class ControlListTransformUseCaseImpl implements ControlListTransformUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;

    private final LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_VALIDATION, NAME_CONTROL_LIST_OPE);

    public void createResponseException(String codeTypeError, String typeControl, String categories) {
        List<ErrorField> errorFields = new ArrayList<>();
        ErrorField errorField = ErrorField.builder().name(typeControl).complement(categories).build();
        errorFields.add(errorField);
        Map<String, List<ErrorField>> errorFieldsCodes = new HashMap<>();
        errorFieldsCodes.put(codeTypeError, errorFields);
        throw new ValidationException(errorFieldsCodes);
    }

    public void cancelAcquisition(AcquisitionReply acquisition, String typeControl, String categories) {
        vinculationUpdateUseCase.markOperation(acquisition.getAcquisitionId(), CODE_CONTROL_LIST,
                Numbers.THREE.getNumber());
        vinculationUpdateUseCase.updateAcquisition(acquisition.getAcquisitionId(), Numbers.TWO.getNumber());
        createResponseException(CTR_LIST_CODE_ERROR, typeControl, categories);

    }

    public ControlList continueWithExeptionAcquisition(AcquisitionReply acquisition, String codeControl,
                                                       String txtMessage) {
        vinculationUpdateUseCase.markOperation(acquisition.getAcquisitionId(), CODE_CONTROL_LIST,
                Numbers.TWO.getNumber());
        return ControlList.builder().validationName(txtMessage).validationCode(codeControl).build();
    }

    public String transformAlertControlList(String alertControl) {
        String result = "";
        switch (alertControl) {
            case CTR_LIST_CODE_TWO_ALERT:
                result = CTR_LIST_CTR_ALERT;
                break;
            case CTR_LIST_CODE_THREE_BLOQ:
                result = CTR_LIST_STR_BLOQ;
                break;
            default:
                result = "";
        }
        adapter.info(STATE_EXTERNAL.concat(result));
        return result;
    }

    public ControlList validateAlerts(AcquisitionReply acquisition, DataControlList dataControlList) {
        ControlList controlList = null;
        if (dataControlList.getState().equals(CTR_LIST_VIGENTE)) {
            cancelAcquisition(acquisition, transformAlertControlList(dataControlList.getAlerts()),
                    dataControlList.getCategories());
        } else {
            controlList = continueWithExeptionAcquisition(acquisition, dataControlList.getAlerts(),
                    CTR_LIST_MESSAGE_CONTINUE_ALERT_BLOQ
                            .replace("{0}", transformAlertControlList(dataControlList.getAlerts()))
                            .replace("{1}", dataControlList.getState()));
        }
        return controlList;
    }

    @Override
    public ControlList transformInfoControlList(AcquisitionReply acquisition,
                                                ControlListResponse controlListResponse) {
        ControlList controlList = null;
        DataControlList dataControlList = controlListResponse.getData().get(0);
        switch (dataControlList.getAlerts()) {
            case CTR_LIST_CODE_TWO_ALERT:
            case CTR_LIST_CODE_THREE_BLOQ:
                controlList = validateAlerts(acquisition, dataControlList);
                break;
            case CTR_LIST_CODE_SIX_NOT_IN:
                controlList = continueWithExeptionAcquisition(acquisition, dataControlList.getAlerts(),
                        CTR_LIST_MESSAGE_NOT_IN);
                break;
            default:
                break;
        }
        if (controlList != null) {
            controlList.setInfoReuseCommon(controlListResponse.getInfoReuseCommon());
        }
        return controlList;
    }
}
