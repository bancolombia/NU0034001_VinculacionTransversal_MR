package co.com.bancolombia.taxinformation;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.common.OptionalMandatoryArguments;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptionalList;
import co.com.bancolombia.commonsvnt.common.exception.InvalidOptionalArgumentException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.response.ResponseFactory;
import co.com.bancolombia.taxinformation.model.TaxInfoRequest;
import co.com.bancolombia.taxinformation.model.TaxInfoRequestData;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.taxinformation.TaxInformationUseCase;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_TAX_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_LIST_TAX;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class TaxInformationController implements TaxOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private TaxInformationUseCase taxInformationUseCase;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private CoreFunctionString coreFunctionString;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @Autowired
    private GenericStep genericStep;

    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_TAX_INFO)
    public ResponseEntity<CodeNameResponse> taxInformation(
            @ApiParam(value = "Information related to tax customer", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody TaxInfoRequest body) {
        TaxInfoRequestData data = body.getData();
        genericStep.firstStepForLogFunctional(data, body.getMeta(), CODE_TAX_INFO);
        new MapErrorTax(body);
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_TAX_INFO);
        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .typeAcquisition(
                        TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build()).build();

        TaxInformation taxInformation = constructTaxInformationObject(body, acquisition);
        List<TaxCountry> taxCountryList = this.constructTaxCountry(body, acquisition);
        taxInformationUseCase.startProcessTaxInformation(taxInformation, taxCountryList);

        genericStep.finallyStep(data.getAcquisitionId(), null, CODE_TAX_INFO);
        return new ResponseEntity<>(ResponseFactory.buildCodeNameResponse(body.getMeta()), HttpStatus.OK);
    }

    public TaxInformation constructTaxInformationObject(TaxInfoRequest body, Acquisition acquisition) {
        return TaxInformation.builder().declaringIncome(body.getData().getDeclaringIncome())
                .withHoldingAgent(body.getData().getWithHoldingAgent()).vatRegime(body.getData().getVatRegime())
                .originAssetComeFrom(body.getData().getOriginAssetComeFrom())
                .sourceCountryResource(body.getData().getSourceCountryResource())
                .sourceCityResource(body.getData().getSourceCityResource())
                .requiredToTaxUsTax(body.getData().getRequiredToTaxUsTax()).taxId(body.getData().getTaxId())
                .country(body.getData().getCountry()).businessTaxPayment(body.getData().getBusinessTaxPayment())
                .socialSecurityPayment(body.getData().getSocialSecurityPayment())
                .declareTaxInAnotherCountry(body.getData().getDeclareTaxInAnotherCountry()).acquisition(acquisition)
                .createdBy(body.getMeta().getUsrMod()).createdDate(coreFunctionDate.getDatetime()).build();
    }


    public List<TaxCountry> constructTaxCountry(TaxInfoRequest body, Acquisition acq) {
        List<TaxCountry> taxCountryList = new ArrayList<>();
        if (body.getData().getTaxInfoRequestDataCountryList() != null) {
            body.getData().getTaxInfoRequestDataCountryList().forEach(countryList -> {
                TaxCountry taxCountry = TaxCountry.builder().identifier(coreFunctionString.stringToInteger(
                        countryList.getIdentifier())).taxId(countryList.getTaxId())
                        .country(countryList.getCountry()).acquisition(acq).createdBy(body.getMeta().getUsrMod())
                        .createdDate(coreFunctionDate.getDatetime()).build();
                taxCountryList.add(taxCountry);
            });
        }
        return taxCountryList;
    }
}

class MapErrorTax {
    TaxInfoRequest body;
    public MapErrorTax(TaxInfoRequest taxInfoRequest) {
        this.body = taxInfoRequest;
        List<InvalidOptionalArgumentException> listException = new ArrayList<>(
                OptionalMandatoryArguments.validArgumentsList(
                        body.getData(), new Class[]{ValidationOptional.class},
                        ConstantsErrors.ERROR_CODE_OPTIONAL, EMPTY, EMPTY));
        if (body.getData().getTaxInfoRequestDataCountryList() != null) {
            body.getData().getTaxInfoRequestDataCountryList()
                    .forEach(countryList -> listException.addAll((
                            OptionalMandatoryArguments.validArgumentsList(
                                    countryList, new Class[]{ValidationOptionalList.class},
                                    ERROR_CODE_OPTIONAL_LIST_TAX, countryList.getIdentifier(), EMPTY))));
        }
        OptionalMandatoryArguments.validateException(listException);
    }
}