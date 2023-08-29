package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.customercontrol.CustomerControlUseCase;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.customercontrol.CustomerControl;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;
import co.com.bancolombia.validatesession.ValidateSessionUseCase;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_UPDATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DESCRIPTION_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_START_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_PERSON;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CUSTOMER_CONTROL;

@RequiredArgsConstructor
public class AcquisitionOpeValidateUseCaseImpl implements AcquisitionOpeValidateUseCase {
    private final CustomerControlUseCase customerControlUseCase;
    private final ValidateSessionUseCase validateSessionUseCase;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, OPER_START_ACQUISITION);

    @Override
    public void validateCustomerControl(String docType, String docNumber) {
        CustomerControl customerControl = customerControlUseCase.
                findByDocumentTypeAndDocumentNumber(docType, docNumber);
        if (customerControl != null) {
            String msj = customerControlUseCase.unblockCustomer(customerControl);
            if (msj != null) {
                adapter.error(ERROR + ERROR_CODE_CUSTOMER_CONTROL);
                throw new ValidationException(new MapErrorAcquisition(ERROR_CODE_CUSTOMER_CONTROL,
                        ErrorField.builder().name(msj).build()).map);
            }
        }
    }

    @Override
    public ValidateSessionResponse getValidateSessionResponse(
            AcquisitionRequestModel acquisitionRequestModel, String operation) {
        ValidateSessionResponse validateSessionResponse = null;
        if (CODE_START_UPDATE.equals(operation)) {
            validateSessionResponse = validateSessionUseCase.validateValidSession(
                    acquisitionRequestModel.getDocumentNumber(), acquisitionRequestModel.getDocumentType(),
                    acquisitionRequestModel.getToken(), operation);
        }
        return validateSessionResponse;
    }

    @Override
    public void saveValidateSession(
            ValidateSessionResponse validateSessionResponse, Acquisition acquisition, String operation) {
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        if (CODE_START_UPDATE.equals(operation)) {
            infoReuseCommon = validateSessionUseCase
                    .getInfoReuseFromValidateSession(validateSessionResponse);
            if (validateSessionResponse != null) {
                validateSessionUseCase.saveValidateSession(validateSessionResponse, acquisition);
            }
        }

        Map<String, String> otherDataResponse = new HashMap<>();
        otherDataResponse.put(TYPE_PERSON, acquisition.getTypePerson().getCode());
        otherDataResponse.put(TYPE_ACQUISITION, acquisition.getTypeAcquisition().getCode());
        otherDataResponse.put(DESCRIPTION_ACQUISITION, acquisition.getTypeAcquisition().getName());

        acquisition.setInfoReuse(infoReuseCommon.toBuilder().mapFields(otherDataResponse).build());
    }
}