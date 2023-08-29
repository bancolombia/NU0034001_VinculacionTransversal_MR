package co.com.bancolombia.economicinformation;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.common.OptionalMandatoryArguments;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.common.exception.InvalidOptionalArgumentException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.economicinformation.model.EconomicInfoRequest;
import co.com.bancolombia.economicinformation.model.EconomicInfoRequestData;
import co.com.bancolombia.economicinformation.model.EconomicInfoResponse;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.usecase.economicinformation.EconomicInformationUseCase;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ECONOMIC_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATE_FORMAT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.YES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class EconomicInformationController implements EconomicOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private EconomicInformationUseCase economicInformationUseCase;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private GenericStep genericStep;

    @Autowired
    private CoreFunctionString coreFunctionString;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_ECONOMIC_INFO)
    public ResponseEntity<EconomicInfoResponse> economicInformation(
            @ApiParam(value = "Information related to economic customer", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody EconomicInfoRequest body) {
        EconomicInfoRequestData data = body.getData();
        genericStep.firstStepForLogFunctional(data, body.getMeta(), CODE_ECONOMIC_INFO);
        new MapErrorEconomic(body);

        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_ECONOMIC_INFO);
        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .typeAcquisition(TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build()).build();

        EconomicInformation eInfo = this.constructEconomicInformation(body, Optional.of(acquisition));
        EconomicInformation eResult = economicInformationUseCase.startProcessEconomicInformation(eInfo);

        Map<String, String> map = new HashMap<>();
        map.put("economicActivityMark", eResult.getEconomicActivityMark());
        map.put("requiredRut", eResult.getRequiredRut());
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().mapFields(map).build();
        genericStep.finallyStep(data.getAcquisitionId(), infoReuseCommon, CODE_ECONOMIC_INFO);

        return new ResponseEntity<>(eResult.getRequiredRut().equals(YES) ?
                ResponseFactoryEconomic.buildRequiredRutEconomicResponse(body) :
                ResponseFactoryEconomic.buildNoRequiredRutEconomicResponse(body), HttpStatus.OK);
    }

    public EconomicInformation constructEconomicInformation(
            EconomicInfoRequest body, Optional<Acquisition> acquisition) {
        return EconomicInformation.builder()
                .profession(body.getData().getProfession()).positionTrade(body.getData().getPositionTrade())
                .occupation(body.getData().getOccupation()).currency(body.getData().getCurrency())
                .economicActivity(body.getData().getEconomicActivity()).ciiu(body.getData().getCiiu())
                .monthlyIncome(coreFunctionString.stringToDecimal(body.getData().getMonthlyIncome()))
                .patrimony(coreFunctionString.stringToDecimal(body.getData().getPatrimony()))
                .otherMonthlyIncome(coreFunctionString.stringToDecimal(body.getData().getOtherMonthlyIncome()))
                .totalAssets(coreFunctionString.stringToDecimal(body.getData().getTotalAssets()))
                .totalLiabilities(coreFunctionString.stringToDecimal(body.getData().getTotalLiabilities()))
                .detailOtherMonthlyIncome(body.getData().getDetailOtherMonthlyIncome())
                .totalMonthlyExpenses(coreFunctionString.stringToDecimal(body.getData().getTotalMonthlyExpenses()))
                .annualSales(coreFunctionString.stringToDecimal(body.getData().getAnnualSales()))
                .closingDateSales(coreFunctionDate.getDateFromString(body.getData().getClosingDateSales(),
                        DATE_FORMAT))
                .employeesNumber(body.getData().getEmployeesNumber()).rut(body.getData().getRut())
                .acquisition(acquisition.isPresent() ? acquisition.get() : null)
                .createdBy(body.getMeta().getUsrMod()).createdDate(coreFunctionDate.getDatetime()).build();
    }
}

class MapErrorEconomic {
    EconomicInfoRequest body;
    public MapErrorEconomic(EconomicInfoRequest economicInfoRequest) {
        this.body = economicInfoRequest;
        List<InvalidOptionalArgumentException> listException = new ArrayList<>(
                OptionalMandatoryArguments.validArgumentsList(body.getData(), new Class[]{ValidationOptional.class},
                        ERROR_CODE_OPTIONAL, EMPTY, EMPTY));
        OptionalMandatoryArguments.validateException(listException);
    }
}