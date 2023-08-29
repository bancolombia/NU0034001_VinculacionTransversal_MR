package co.com.bancolombia.api.validate.controllist;

import co.com.bancolombia.ReputationIdentityController;
import co.com.bancolombia.ResponseFactoryV;
import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.model.ControlListResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.controllist.ControlListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.controllist.ControlList;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONTROL_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FINISH_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NAME_CONTROL_LIST_OPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.START_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@ReputationIdentityController
@Api(tags = {"AcquisitionValidate"})
public class ControlListController implements ControlListOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private GenericStep genericStep;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private ControlListUseCase controlListUseCase;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_VALIDATION, NAME_CONTROL_LIST_OPE);

    @Override
    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_CONTROL_LIST)
    public ResponseEntity<ControlListResponse> validateCustomerControlList(
            @ApiParam(value = "Information related to validate customer control list ", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody UserInfoRequest body) {
        webRequest.setAttribute(META, body.getMeta(), SCOPE_REQUEST);
        UserInfoRequestData data = body.getData();
        adapter.info(START_OPERATION);
        StepForLogFunctional stepForLogFunctional = genericStep.firstStepForLogFunctional(data,
                body.getMeta(), CODE_CONTROL_LIST);
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_CONTROL_LIST);
        ControlList controlList = controlListUseCase.startProcessControlList(acquisitionReply,
                BasicAcquisitionRequest.builder().messageId(body.getMeta().getMessageId())
                        .userTransaction(body.getMeta().getUsrMod()).build());
        adapter.info(FINISH_OPERATION);
        genericStep.finallyStep(stepForLogFunctional, data.getAcquisitionId(), controlList.getInfoReuseCommon(),
                CODE_CONTROL_LIST);
        return new ResponseEntity<>(ResponseFactoryV.buildControlListResponse(body, controlList), HttpStatus.OK);
    }
}
