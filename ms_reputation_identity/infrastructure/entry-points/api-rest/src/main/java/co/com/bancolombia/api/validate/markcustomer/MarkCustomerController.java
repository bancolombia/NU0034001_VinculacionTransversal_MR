package co.com.bancolombia.api.validate.markcustomer;

import co.com.bancolombia.ReputationIdentityController;
import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.model.markcustomer.MarkCustomerResponse;
import co.com.bancolombia.api.model.markcustomer.MarkCustomerResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.markcustomer.MarkCustomerUseCase;
import co.com.bancolombia.model.markcustomer.MarkCustomer;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPE_MARKCUSTOMER_VALUE;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@ReputationIdentityController
@Api(tags = {"AcquisitionValidate"})
public class MarkCustomerController implements MarkCustomerOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private MarkCustomerUseCase markCustomerUseCase;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private GenericStep genericStep;

    public BasicAcquisitionRequest createObject(Optional<Acquisition> acquisition, UserInfoRequest body) {
        if (acquisition.isPresent()){
            return BasicAcquisitionRequest.builder().idAcq(acquisition
                    .map(value -> value.getId().toString()).orElse(null))
                    .documentNumber(acquisition.get().getDocumentNumber()).userTransaction(body.getMeta().getUsrMod())
                    .documentType(acquisition.get().getDocumentType().getCode())
                    .messageId(body.getMeta().getMessageId()).build();
        }else{
            return null;
        }
    }

    @ILogRegister(api = API_CUSTOMER_VALUE, operation = OPE_MARKCUSTOMER_VALUE)
    public ResponseEntity<MarkCustomerResponse> markCustomer(
            @ApiParam(value = "Information related to mark customer", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody UserInfoRequest body) {
        UserInfoRequestData data = body.getData();
        MetaRequest meta = body.getMeta();

        webRequest.setAttribute(META, meta, SCOPE_REQUEST);
        StepForLogFunctional step = genericStep.firstStepForLogFunctional(data, meta, OPE_MARKCUSTOMER_VALUE);
        genericStep.validRequest(data);
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition
                (data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), OPE_MARKCUSTOMER_VALUE);
        Optional<Acquisition> acquisition = Optional.ofNullable(Acquisition.builder().id(UUID.fromString
                (acquisitionReply.getAcquisitionId())).documentNumber(acquisitionReply.getDocumentNumber())
                .typeAcquisition(TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .documentType(DocumentType.builder().codeOrderControlList(acquisitionReply
                        .getDocumentTypeOrderControlList()).codeHomologation(acquisitionReply
                        .getDocumentTypeCodeGenericType()).build()).stateAcquisition(StateAcquisition.builder()
                        .code(acquisitionReply.getStateCode()).build()).build());
        if (acquisition.isPresent()){
            MarkCustomer markCustomer = markCustomerUseCase.startProcessMarkOperation(this.createObject
                            (acquisition, body), acquisition.get());
            MarkCustomerResponseData markCustomerResponseData = null;
            if (markCustomer != null) {
                markCustomerResponseData = MarkCustomerResponseData.builder().answerCode(markCustomer.getAnswerCode())
                        .answerName(markCustomer.getAnswerName()).build();
                genericStep.finallyStep(step, data.getAcquisitionId(), markCustomer.getInfoReuseCommon(),
                        OPE_MARKCUSTOMER_VALUE);
                return new ResponseEntity<>(MCResponseFactory.buildMarkCustomerResponse(body, markCustomerResponseData),
                        HttpStatus.OK);
            }
        }
        return null;
    }
}
