package co.com.bancolombia.basicinformation;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.basicinformation.basicinfo.BasicInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.response.ResponseFactory;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_BASIC_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class BasicInformationController implements BasicInformationOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private CoreFunctionString coreFunctionString;

    @Autowired
    private BasicInformationUseCase basicInformationUseCase;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private GenericStep genericStep;

    @ILogRegister(api = API_CUSTOMER_VALUE, operation = Constants.CODE_BASIC_INFO)
    public ResponseEntity<CodeNameResponse> basicInformation(
            @ApiParam(value = "Information related to basic customer", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody BasicInfoRequest body) {
        UserInfoRequestData data = body.getData();
        MetaRequest meta = body.getMeta();

        webRequest.setAttribute(META, meta, SCOPE_REQUEST);

        genericStep.firstStepForLogFunctional(data, meta, CODE_BASIC_INFO);
        genericStep.validRequest(body.getData());

        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_BASIC_INFO);
        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build()).build();
        BasicInformation bInfo = constructBasicInformation(body, acquisition);
        basicInformationUseCase.startProcessBasicInformation(bInfo);

        genericStep.finallyStep(data.getAcquisitionId(), null, CODE_BASIC_INFO);
        return new ResponseEntity<>(ResponseFactory.buildCodeNameResponse(body.getMeta()), HttpStatus.OK);
    }

    public BasicInformation constructBasicInformation(BasicInfoRequest body, Acquisition acquisition) {
        return BasicInformation.builder().gender(body.getData().getGender())
                .birthCity(body.getData().getBirthCity()).country(body.getData().getCountry())
                .birthDepartment(body.getData().getBirthDepartment()).civilStatus(body.getData().getCivilStatus())
                .nationality(body.getData().getNationality()).pep(body.getData().getPep())
                .dependants(coreFunctionString.stringToInteger(body.getData().getDependants()))
                .educationLevel(body.getData().getEducationLevel()).socialStratum(body.getData().getSocialStratum())
                .housingType(body.getData().getHousingType()).contractType(body.getData().getContractType())
                .entryCompanyDate(coreFunctionDate
                        .getDateFromString(body.getData().getEntryCompanyDate(), Constants.DATE_FORMAT))
                .acquisition(acquisition).createdBy(body.getMeta().getUsrMod())
                .createdDate(coreFunctionDate.getDatetime()).build();
    }

    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Customer", HttpStatus.OK);
    }
}