package co.com.bancolombia.contactinformation;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.common.OptionalMandatoryArguments;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationInvalidInputList;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatoryInputList;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.common.exception.InvalidOptionalArgumentException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.contactinformation.contactinfo.ContactInfoRequest;
import co.com.bancolombia.contactinformation.contactinfo.ContactInfoRequestData;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.response.ResponseFactory;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONTACT_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANDATORY_INVALID_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MANDATORY_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_LIST;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class ContactInformationController implements ContactOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private CoreFunctionString coreFunctionString;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private ContactInformationUseCase contactInformationUseCase;

    @Autowired
    private GenericStep genericStep;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    public void validateErrorInput(ContactInfoRequest body) {
        List<InvalidOptionalArgumentException> listException = new ArrayList<>();
        body.getData().getContactInfoRequestDataAddressListList().forEach(contact -> {
            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    contact, new Class[]{ValidationMandatoryInputList.class},
                    ERROR_CODE_MANDATORY_LIST, contact.getAddressType(), EMPTY));
            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    contact, new Class[]{ValidationInvalidInputList.class},
                    ERROR_CODE_MANDATORY_INVALID_LIST, contact.getAddressType(), EMPTY));
            listException.addAll(OptionalMandatoryArguments.validArgumentsList(
                    contact, new Class[]{ValidationOptional.class},
                    ERROR_CODE_OPTIONAL_LIST, contact.getAddressType(), EMPTY));
        });
        OptionalMandatoryArguments.validateException(listException);
    }

    public List<ContactInformation> creteModelContact(ContactInfoRequest body, Acquisition acquisition) {
        List<ContactInformation> contactInformationList = new ArrayList<>();
        body.getData().getContactInfoRequestDataAddressListList().forEach(contact -> {
            ContactInformation contactInformation = ContactInformation.builder()
                    .addressType(contact.getAddressType()).brand(contact.getBrand()).city(contact.getCity())
                    .address(contact.getAddress()).neighborhood(contact.getNeighborhood())
                    .companyName(contact.getCompanyName()).department(contact.getDepartment())
                    .phone(contact.getPhone()).ext(contact.getExt()).cellphone(contact.getCellphone())
                    .email(coreFunctionString.lowerCaseString(contact.getEmail())).acquisition(acquisition)
                    .country(contact.getCountry()).createdBy(body.getMeta().getUsrMod()).build();
            contactInformationList.add(contactInformation);
        });
        return contactInformationList;
    }

    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_CONTACT_INFO)
    public ResponseEntity<CodeNameResponse> contactInformation(
            @ApiParam(value = "Information related to contact customer", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody ContactInfoRequest body) {

        webRequest.setAttribute(META, body.getMeta(), SCOPE_REQUEST);
        this.validateErrorInput(body);
        ContactInfoRequestData data = body.getData();
        MetaRequest meta = body.getMeta();
        genericStep.firstStepForLogFunctional(data, meta, CODE_CONTACT_INFO);
        genericStep.validRequest(body.getData());

        TypeAcquisition typeAcquisition = TypeAcquisition.builder().code("TVT001").build();

        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_CONTACT_INFO);
        Acquisition acquisition = Acquisition.builder()
                .typeAcquisition(typeAcquisition).id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build()).build();

        List<ContactInformation> listContact = contactInformationUseCase.
                startProcessContactInformation(this.creteModelContact(body, acquisition));
        if (!listContact.isEmpty()) {
            contactInformationUseCase.save(listContact);
        }

        genericStep.finallyStep(data.getAcquisitionId(), null, CODE_CONTACT_INFO);

        return new ResponseEntity<>(ResponseFactory.buildCodeNameResponse(body.getMeta()), HttpStatus.OK);
    }
}