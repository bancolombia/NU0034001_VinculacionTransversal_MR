package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.markcustomer.MarkCustomer;
import co.com.bancolombia.model.markcustomer.MarkCustomerResponseWithLog;
import co.com.bancolombia.model.markcustomer.RegisterControlListList;
import co.com.bancolombia.model.markcustomer.RegisterControlListRequest;
import co.com.bancolombia.model.markcustomer.gateways.RegisterControlListRestRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL_RESULT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_ANSWER_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_CATEGORY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_CUSTOMER_TYPE_N;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_DEPARTMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_NOT_ANSWER_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_REASON;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_SUBCATEGORY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_MARK_CUSTOMER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPE_MARKCUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;


@RequiredArgsConstructor
public class MarkCustomerUseCaseImpl implements MarkCustomerUseCase {

    private final RegisterControlListRestRepository restRepository;
    private final MarkCustomerSaveUseCase markCustomerSaveUseCase;
    private final CoreFunctionDate coreFunctionDate;
    private final MarkCustomerValidationsUseCase markValidations;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final NaturalPersonUseCase naturalPersonUseCase;


    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_VALIDATION, OPER_MARK_CUSTOMER);

    @Override
    public MarkCustomer startProcessMarkOperation(BasicAcquisitionRequest basicAcquisitionRequest,
                                                  Acquisition acquisition) {
        if (!acquisition.getStateAcquisition().getCode().equals(Numbers.FIVE.getNumber())) {
            vinculationUpdateUseCase.markOperation
                    (acquisition.getId().toString(), OPE_MARKCUSTOMER_VALUE, CODE_ST_OPE_COMPLETADO);
            return MarkCustomer.builder().answerCode(Numbers.TWO.getNumber()).answerName(MC_NOT_ANSWER_NAME)
                    .infoReuseCommon(InfoReuseCommon.builder().build()).build();
        }
        adapter.info(CONSUME_EXTERNAL);
        MarkCustomerResponseWithLog markCustomer = restRepository.getRegisterControl(this.createRequest(acquisition),
                basicAcquisitionRequest.getMessageId(), coreFunctionDate.getDatetime());
        adapter.info(CONSUME_EXTERNAL_RESULT.concat(markCustomer.toString()));
        if (markCustomer.getControlListResponse().getErrors() != null) {
            markValidations.validateResponse(markCustomer.getControlListResponse().getErrors(), acquisition);
        }
        markCustomerSaveUseCase.saveInfo(markCustomer.getControlListResponse().getResponse(), acquisition,
                basicAcquisitionRequest);
        vinculationUpdateUseCase.markOperation
                (acquisition.getId().toString(), OPE_MARKCUSTOMER_VALUE, CODE_ST_OPE_COMPLETADO);
        vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.TWO.getNumber());
        return MarkCustomer.builder().answerCode(Numbers.ONE.getNumber()).answerName(MC_ANSWER_NAME)
                .infoReuseCommon(markCustomer.getInfoReuseCommon()).build();
    }

    @Override
    public RegisterControlListRequest createRequest(Acquisition acquisition) {
        ValidateIdentityReply personalInfoData = naturalPersonUseCase.validateIdentity(acquisition.getId().toString());
        return RegisterControlListRequest.builder().data(Collections.singletonList(RegisterControlListList.builder()
                .department(MC_DEPARTMENT).category(MC_CATEGORY).subCategory(MC_SUBCATEGORY).reason(MC_REASON)
                .customerType(MC_CUSTOMER_TYPE_N).documentType(acquisition.getDocumentType().getCodeOrderControlList())
                .secondSurName(personalInfoData.getSecondName()).surname(personalInfoData.getFirstSurname())
                .firstName(personalInfoData.getFirstName()).secondName(personalInfoData.getSecondName())
                .documentNumber(acquisition.getDocumentNumber()).build())).build();
    }
}
