package co.com.bancolombia.personalinformation;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.personalinformation.model.PersonalInfoRequest;
import co.com.bancolombia.personalinformation.model.PersonalInfoRequestData;
import co.com.bancolombia.response.ResponseFactory;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationStartProcessUseCase;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PERSONAL_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATE_FORMAT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class PersonalInformationController implements PersonalInformationOperations {

    @Autowired
    private CoreFunctionString coreFunctionString;

    @Autowired
    private PersonalInformationStartProcessUseCase personalInformationStartProcessUseCase;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private GenericStep genericStep;

    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_PERSONAL_INFO)
    public ResponseEntity<CodeNameResponse> personalInformation(
            @ApiParam(value = "Information related to personal customer", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody PersonalInfoRequest body) {
        webRequest.setAttribute(META, body.getMeta(), SCOPE_REQUEST);

        UserInfoRequestData dataRequest = UserInfoRequestData.builder().acquisitionId(body.getData().getAcquisitionId())
                .documentType(body.getData().getDocumentType()).documentNumber(body.getData().getDocumentNumber())
                .build();
        genericStep.firstStepForLogFunctional(dataRequest,body.getMeta(),CODE_PERSONAL_INFO);
        genericStep.validRequest(body.getData());

        PersonalInfoRequestData data = body.getData();

        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_PERSONAL_INFO);
        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build()).build();

        PersonalInformation pInfo = this.constructPersonalInformation(body, acquisition);
        personalInformationStartProcessUseCase.startProcessPersonalInformation(pInfo);

        genericStep.finallyStep(data.getAcquisitionId(),null,CODE_PERSONAL_INFO);

        return new ResponseEntity<>(ResponseFactory.buildCodeNameResponse(body.getMeta()), HttpStatus.OK);
    }

    public PersonalInformation constructPersonalInformation(PersonalInfoRequest body,
                                                            Acquisition acquisition) {
        return PersonalInformation.builder()
                .firstName(coreFunctionString.upperCaseString(body.getData().getFirstName()))
                .secondName(coreFunctionString.upperCaseString(body.getData().getSecondName()))
                .firstSurname(coreFunctionString.upperCaseString(body.getData().getFirstSurname()))
                .secondSurname(coreFunctionString.upperCaseString(body.getData().getSecondSurname()))
                .expeditionPlace(body.getData().getExpeditionPlace())
                .expeditionCountry(body.getData().getExpeditionCountry())
                .expeditionDepartment(body.getData().getExpeditionDepartment())
                .birthdate(coreFunctionDate.getDateFromString(body.getData().getBirthdate(), DATE_FORMAT))
                .expeditionDate(
                        coreFunctionDate.getDateFromString(body.getData().getExpeditionDate(), DATE_FORMAT))
                .cellphone(body.getData().getCellphone())
                .email(coreFunctionString.lowerCaseString(body.getData().getEmail())).acquisition(acquisition)
                .createdBy(body.getMeta().getUsrMod()).createdDate(coreFunctionDate.getDatetime()).build();

    }


}
