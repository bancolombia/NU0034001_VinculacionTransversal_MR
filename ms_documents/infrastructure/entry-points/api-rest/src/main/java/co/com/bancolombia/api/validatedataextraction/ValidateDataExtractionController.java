package co.com.bancolombia.api.validatedataextraction;

import co.com.bancolombia.DocumentsController;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.validatedataextraction.ValidateDataExtractionRequest;
import co.com.bancolombia.api.model.validatedataextraction.ValidateDataExtractionRequestData;
import co.com.bancolombia.api.model.validatedataextraction.ValidateDataExtractionResponse;
import co.com.bancolombia.api.response.ResponseFactoryDigitization;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.validatedataextraction.ValidateDataExtraction;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.validatedataextraction.ValidateDataExtractionUseCase;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VAL_DATA_EXTRAC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@DocumentsController
@Api(tags = {"AcquisitionUploadDocument",})
public class ValidateDataExtractionController implements ValidateDataExtractionOperation {

    @Autowired
    private ValidateDataExtractionUseCase validateDataExtractionUseCase;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;


    @Autowired
    private WebRequest webRequest;

    @Autowired
    private ValidateDataExtractionCoincidenceCiiuValidate ciiuValidate;

    @Autowired
    private GenericStep genericStep;


    @Override
    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_VAL_DATA_EXTRAC)
    public ResponseEntity<ValidateDataExtractionResponse> validateDataExtraction(
            @ApiParam(value = "Information related to Validate Data Extraction", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody ValidateDataExtractionRequest body) {

        ValidateDataExtractionRequestData data = body.getData();
        MetaRequest meta = body.getMeta();
        webRequest.setAttribute(META, meta, SCOPE_REQUEST);

        genericStep.firstStepForLogFunctional(data, meta, CODE_VAL_DATA_EXTRAC);
        genericStep.validRequest(data);

        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(),
                data.getDocumentNumber(), CODE_VAL_DATA_EXTRAC);

        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId())).documentNumber
                        (acquisitionReply.getDocumentNumber()).typeAcquisition(TypeAcquisition.builder().code
                        (acquisitionReply.getCodeTypeAcquisition()).build()).documentType(DocumentType.builder()
                        .codeOrderControlList(acquisitionReply.getDocumentTypeOrderControlList()).codeHomologation
                                (acquisitionReply.getDocumentTypeCodeGenericType()).build())
                .stateAcquisition(StateAcquisition.builder().code(acquisitionReply.getStateCode()).build()).build();

        ValidateDataExtraction validateDE = validateDataExtractionUseCase.getAnswer(acquisition,
                data.getDocumentalTypeCode(), data.getDocumentalSubTypeCode(), null);

        InfoReuseCommon infoRC = validateDE.getInfoReuseCommon();
        infoRC.setMapFields(ciiuValidate.findCoincidenceCiiu
                (acquisition, validateDE.getUploadDocumentApiResponseData()));
        genericStep.finallyStep(data.getAcquisitionId(), infoRC, CODE_VAL_DATA_EXTRAC);

        if (validateDE.getUploadDocumentApiResponseData() != null) {
            return new ResponseEntity<>
                    (ResponseFactoryDigitization.buildValidateDataExtractionResponse(meta, validateDE), HttpStatus.OK);
        }

        return null;
    }


}