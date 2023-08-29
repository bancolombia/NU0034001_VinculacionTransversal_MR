package co.com.bancolombia.foreigninformation;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.common.OptionalMandatoryArguments;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.common.exception.InvalidOptionalArgumentException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequest;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.response.ResponseFactory;
import co.com.bancolombia.usecase.foreigninformation.ForeignInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_LIST_FOREIGN;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class ForeignInformationController implements ForeignOperations {

    @Autowired
    private CreateModelForeignOperation createModelForeignOperation;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private ForeignInformationUseCase foreignInformationUseCase;

    @Autowired
    private GenericStep genericStep;

    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_FOREIGN_INFORMATION)
    public ResponseEntity<CodeNameResponse> foreignCurrencyInformation(
            @ApiParam(value = "Information related to foreign currency information", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody ForeignCurrencyInfoRequest body) {
        UserInfoRequestData data = body.getData();
        MetaRequest meta = body.getMeta();

        genericStep.firstStepForLogFunctional(data, meta, CODE_FOREIGN_INFORMATION);
        genericStep.validRequest(body.getData());
        new MapErrorAcquisition(body);

        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_FOREIGN_INFORMATION);
        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .typeAcquisition(
                        TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build()).build();

        foreignInformationUseCase.startProcessForeignInformation(
                createModelForeignOperation.creteModelForeignOperation(body, acquisition));
        genericStep.finallyStep(data.getAcquisitionId(), null, CODE_FOREIGN_INFORMATION);
        return new ResponseEntity<>(ResponseFactory.buildCodeNameResponse(body.getMeta()), HttpStatus.OK);
    }
}

class MapErrorAcquisition {
    ForeignCurrencyInfoRequest body;

    public MapErrorAcquisition(ForeignCurrencyInfoRequest foreignCurrencyInfoRequest) {
        this.body = foreignCurrencyInfoRequest;
        List<InvalidOptionalArgumentException> listException = new ArrayList<>();
        if (body.getData().getForeignCurrencyInfoRequestDataList() != null) {
            body.getData().getForeignCurrencyInfoRequestDataList().forEach(
                    list -> listException.addAll(
                            (OptionalMandatoryArguments.validArgumentsList(
                                    list, new Class[]{ValidationOptional.class}, ERROR_CODE_OPTIONAL_LIST_FOREIGN,
                                    list.getForeignCurrencyTransactionType(), EMPTY))));
        }
        OptionalMandatoryArguments.validateException(listException);
    }
}