package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.VinUpdateController;
import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.api.model.acquisition.AcquisitionRequest;
import co.com.bancolombia.api.model.acquisition.AcquisitionRequestUser;
import co.com.bancolombia.api.model.acquisition.AcquisitionResponse;
import co.com.bancolombia.api.response.ResponseFactory;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ACQUISITION_CREATED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_START_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.START_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;

@VinUpdateController
@Api(tags = {"AcquisitionManagement",})
public class AcquisitionController implements AcquisitionOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private AcquisitionOperationUseCase acquisitionOperationUseCase;

    @Autowired
    private GenericStep genericStep;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, OPER_START_ACQUISITION);

    @ILogRegister(api = Constants.API_CUSTOMER_VALUE, operation = Constants.CODE_START_ACQUISITION)
    public ResponseEntity<AcquisitionResponse> startAcquisition(@Valid @RequestBody AcquisitionRequest body) {
        adapter.info(START_ACQUISITION);
        AcquisitionRequestUser requestUser = body.getData();
        webRequest.setAttribute(META, body.getMeta(), RequestAttributes.SCOPE_REQUEST);

        AcquisitionRequestModel requestModel = AcquisitionRequestModel.builder()
                .documentType(requestUser.getDocumentType()).documentNumber(requestUser.getDocumentNumber())
                .typeProduct(requestUser.getProductId()).typeChannel(requestUser.getChannelId())
                .businessLine(requestUser.getBusinessLineId()).typePerson(null).build();
        
        Acquisition acquisition = acquisitionOperationUseCase
                .startAcquisition(requestModel, body.getMeta().getUsrMod(), Constants.CODE_START_ACQUISITION);

        UserInfoRequestData infoRequestData = UserInfoRequestData.builder()
                .acquisitionId(acquisition.getId().toString())
                .documentNumber(requestUser.getDocumentNumber())
                .documentType(requestUser.getDocumentType()).build();
        
        StepForLogFunctional stepForLogFunctional = genericStep
                .firstStepForLogFunctional(infoRequestData, body.getMeta(), CODE_START_ACQUISITION);

        genericStep.finallyStep(stepForLogFunctional, acquisition.getId().toString(),
                acquisition.getInfoReuse(), CODE_START_ACQUISITION);

        adapter.info(ACQUISITION_CREATED);
        return new ResponseEntity<>(ResponseFactory.buildStartAcquisitionResponse(body, acquisition), HttpStatus.OK);
    }

    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Customer", HttpStatus.OK);
    }
}
