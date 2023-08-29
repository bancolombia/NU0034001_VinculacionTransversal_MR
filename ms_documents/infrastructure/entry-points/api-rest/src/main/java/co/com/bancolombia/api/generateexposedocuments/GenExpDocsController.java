package co.com.bancolombia.api.generateexposedocuments;

import co.com.bancolombia.DocumentsController;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsRequest;
import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsRequestData;
import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsResponse;
import co.com.bancolombia.api.response.ResponseFactoryGenExpDocs;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.generateexposedocuments.GenExpDocsUseCase;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.exposedocuments.ExposeDocs;
import co.com.bancolombia.model.generatepdf.GeneratePdf;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_DOCUMENTATION_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_GEN_EXP_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@DocumentsController
@Api(tags = {"AcquisitionDocuments",})
public class GenExpDocsController implements GenExpDocsOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private GenExpDocsUseCase exposeDocsUseCase;

    @Autowired
    private GenericStep genericStep;

    @ILogRegister(api = API_DOCUMENTATION_VALUE, operation = CODE_GEN_EXP_DOCS)
    public ResponseEntity<GenExpDocsResponse> generateExposeDocuments(
            @ApiParam(value = "Information related to generate - expose documents", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody GenExpDocsRequest body) {

        GenExpDocsRequestData data = body.getData();
        MetaRequest meta = body.getMeta();

        webRequest.setAttribute(META, meta, SCOPE_REQUEST);

        genericStep.firstStepForLogFunctional(data, meta, CODE_GEN_EXP_DOCS);
        genericStep.validRequest(data);

        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(), data.getDocumentNumber(), CODE_GEN_EXP_DOCS);

        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build())
                .typeAcquisition(TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .uploadDocumentRetries(0).uploadRutRetries(0).build();

        GeneratePdf generatePdf = exposeDocsUseCase.getPdf(acquisition);

        genericStep.finallyStep(data.getAcquisitionId(), generatePdf.getInfoReuseCommon(), CODE_GEN_EXP_DOCS);

        ExposeDocs exposeDocs = exposeDocsUseCase.urlFinal(acquisition, generatePdf);
        return new ResponseEntity<>(ResponseFactoryGenExpDocs.buildExposeDocsResponse(body, exposeDocs), HttpStatus.OK);
    }
}