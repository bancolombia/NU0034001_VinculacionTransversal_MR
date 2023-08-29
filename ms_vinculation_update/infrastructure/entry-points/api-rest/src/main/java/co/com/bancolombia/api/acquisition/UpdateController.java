package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.VinUpdateController;
import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.api.model.acquisition.AcquisitionResponse;
import co.com.bancolombia.api.model.acquisition.UpdateRequest;
import co.com.bancolombia.api.model.acquisition.UpdateRequestUser;
import co.com.bancolombia.api.response.ResponseFactory;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_UPDATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_START_UPDATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;

@VinUpdateController
@Api(tags = {"AcquisitionManagement",})
public class UpdateController implements UpdateOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private AcquisitionOperationUseCase acquisitionOperationUseCase;

    @Autowired
    private GenericStep genericStep;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, OPER_START_UPDATE);

    @Override
    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_START_UPDATE)
    public ResponseEntity<AcquisitionResponse> startUpdate(
            @ApiParam(value = "Information related to the user customer", required = true)
            @Valid @RequestBody UpdateRequest body) {
        adapter.info("Iniciando una actualizacion");
        webRequest.setAttribute(META, body.getMeta(), RequestAttributes.SCOPE_REQUEST);

        AcquisitionRequestModel requestModel = AcquisitionRequestModel.builder()
                .documentType(body.getData().getDocumentType()).documentNumber(body.getData().getDocumentNumber())
                .typePerson(null).typeProduct(null).typeChannel(body.getData().getChannelId())
                .businessLine(body.getData().getBusinessLineId()).token(body.getData().getToken()).build();

        Acquisition acquisition = acquisitionOperationUseCase
                .startAcquisition(requestModel, body.getMeta().getUsrMod(), Constants.CODE_START_UPDATE);

        UpdateRequestUser requestUser = body.getData();
        UserInfoRequestData infoRequestData = UserInfoRequestData.builder()
                .acquisitionId(acquisition.getId().toString())
                .documentNumber(requestUser.getDocumentNumber())
                .documentType(requestUser.getDocumentType()).build();

        StepForLogFunctional stepForLogFunctional = genericStep
                .firstStepForLogFunctional(infoRequestData, body.getMeta(), CODE_START_UPDATE);

        genericStep.finallyStep(stepForLogFunctional, acquisition.getId().toString(),
                acquisition.getInfoReuse(), CODE_START_UPDATE);

        adapter.info("Finalizando la actualizacion");
        return new ResponseEntity<>(ResponseFactory.buildStartUpdateResponse(body, acquisition), HttpStatus.OK);
    }
}
