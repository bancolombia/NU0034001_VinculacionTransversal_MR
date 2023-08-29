package co.com.bancolombia.controllist;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.controllist.ControlList;
import co.com.bancolombia.model.controllist.ControlListRequest;
import co.com.bancolombia.model.controllist.ControlListRequestItem;
import co.com.bancolombia.model.controllist.ControlListResponse;
import co.com.bancolombia.model.controllist.ControlListSave;
import co.com.bancolombia.model.controllist.gateways.ControlListRestRepository;
import co.com.bancolombia.model.controllist.gateways.ControlListSaveRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_NOT_FOUND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL_RESULT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NAME_CONTROL_LIST_OPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CTR_LIST_CODE_ERROR_FULLNAME;

@RequiredArgsConstructor
public class ControlListUseCaseImpl implements ControlListUseCase {

    private final ControlListRestRepository controlListRestRepository;
    private final NaturalPersonUseCase naturalUseCase;
    private final CoreFunctionDate coreFunctionDate;
    private final ControlListSaveRepository controlSaveRepository;
    private final ControlListTransformUseCase controlListTransform;

    private final LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_VALIDATION, NAME_CONTROL_LIST_OPE);

    @Override
    public ControlList startProcessControlList(AcquisitionReply acquisitionReply, BasicAcquisitionRequest baRequest) {
        ValidateIdentityReply validateReply = naturalUseCase.validateIdentity(acquisitionReply.getAcquisitionId());
        ControlListRequest request = ControlListRequest.builder().data(Collections.singletonList(
                ControlListRequestItem.builder().system(SYSTEM_VTN).product(PRODUCT_VTN)
                        .customerDocumentType(acquisitionReply.getDocumentTypeHomologous())
                        .customerDocumentId(acquisitionReply.getDocumentNumber())
                        .customerFullName(fullName(validateReply)).build())).build();
        adapter.info(CONSUME_EXTERNAL);
        ControlListResponse response = controlListRestRepository.getUserInfoFromControlList(request,
                baRequest.getMessageId(), coreFunctionDate.getDatetime());
        adapter.info(CONSUME_EXTERNAL_RESULT.concat(response.toString()));
        if (response.getData().get(0).getStatus().getCode().equals(CODE_NOT_FOUND)) {
            adapter.warn("Response Not Found");
            return ControlList.builder().infoReuseCommon(response.getInfoReuseCommon()).build();
        }
        controlSaveRepository.save(constructControlList(response, acquisitionReply, baRequest.getUserTransaction()));
        return controlListTransform.transformInfoControlList(acquisitionReply, response);
    }

    public String fullName(ValidateIdentityReply validateReply) {
        if (validateReply.getFirstSurname() == null || validateReply.getFirstName() == null) {
            List<ErrorField> errorFields = new ArrayList<>();
            ErrorField errorField = ErrorField.builder().name("Nombre completo").complement(EMPTY).build();
            errorFields.add(errorField);
            Map<String, List<ErrorField>> errorFieldsCodes = new HashMap<>();
            errorFieldsCodes.put(CTR_LIST_CODE_ERROR_FULLNAME, errorFields);
            throw new ValidationException(errorFieldsCodes);
        }
        return Stream.of(validateReply.getFirstName(), validateReply.getSecondName(), validateReply.getFirstSurname(),
                        validateReply.getSecondSurname()).filter(cadena -> cadena != null && !cadena.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public ControlListSave constructControlList(ControlListResponse controlListResponse,
                                                AcquisitionReply acquisitionReply, String userMod) {
        return ControlListSave.builder().idAcquisition(acquisitionReply.getAcquisitionId())
                .customerDocumentType(controlListResponse.getData().get(0).getCustomerDocumentType())
                .customerDocumentId(controlListResponse.getData().get(0).getCustomerDocumentId())
                .thirdPartyControl(controlListResponse.getData().get(0).getThirdPartyControl())
                .customerStatus(controlListResponse.getData().get(0).getCustomerStatus())
                .state(controlListResponse.getData().get(0).getState())
                .passport(controlListResponse.getData().get(0).getPassport())
                .alerts(controlListResponse.getData().get(0).getAlerts())
                .message(controlListResponse.getData().get(0).getMessage())
                .categories(controlListResponse.getData().get(0).getCategories())
                .ofacMessageOther(controlListResponse.getData().get(0).getOfacMessageOther())
                .requestDate(controlListResponse.getInfoReuseCommon().getDateRequestReuse())
                .responseDate(controlListResponse.getInfoReuseCommon().getDateResponseReuse())
                .messageId(controlListResponse.getMeta().getMessageId())
                .createdBy(userMod).createdDate(coreFunctionDate.getDatetime()).build();
    }

    @Override
    public String findStateValidationCustomerControlList(String acquisitionId,
                                                         String documentType,
                                                         String documentNumber) {
        return controlSaveRepository
                .findStateValidationCustomerControlList(acquisitionId, documentType, documentNumber);
    }
}
