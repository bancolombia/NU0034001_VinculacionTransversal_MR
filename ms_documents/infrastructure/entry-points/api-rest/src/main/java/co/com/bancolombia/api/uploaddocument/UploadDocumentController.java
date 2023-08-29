package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.DocumentsController;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequest;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequestData;
import co.com.bancolombia.api.response.ResponseFactoryDigitization;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.uploaddocument.UploadDocumentValidateUseCase;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@DocumentsController
@Api(tags = {"AcquisitionDocuments"})
public class UploadDocumentController implements UploadDocumentOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private UploadDocumentControllerUtil uploadDocumentControllerUtil;

    @Autowired
    private UploadDocumentValidateUseCase uploadDocumentValidateUseCase;

    @Autowired
    private GenericStep genericStep;

    @ILogRegister(api = Constants.API_DOCUMENTATION_VALUE, operation = CODE_PROCESS_DOCUMENTS)
    public ResponseEntity<UploadDocumentApiResponse> uploadDocument(
            @ApiParam(value = "Information related to processDocument", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody UploadDocumentRequest body) {

        UploadDocumentRequestData data = body.getData();
        MetaRequest meta = body.getMeta();

        webRequest.setAttribute(META, meta, SCOPE_REQUEST);

        genericStep.firstStepForLogFunctional(data, meta, CODE_PROCESS_DOCUMENTS);
        genericStep.validRequest(data);
        uploadDocumentControllerUtil.validateOptionalList(data);

        Acquisition acquisition = uploadDocumentControllerUtil.getAcquisition(data);

        if (uploadDocumentControllerUtil.getDocumentSubtype(body).equals(RUT_SUBTYPE)) {
            if (!uploadDocumentValidateUseCase.validateRutDataExtraction(acquisition, null)) {
                InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
                uploadDocumentControllerUtil.finallyStep(genericStep, data, acquisition, infoReuseCommon);

                return new ResponseEntity<>(
                        ResponseFactoryDigitization.buildUploadDocumentNotRutExtractionResponse(body), HttpStatus.OK);
            }
        }

        List<UploadDocumentFile> listGet = transformUploadDocumentFiles(body.getData());
        List<ProcessDocumentFiles> processDocumentFiles = uploadDocumentValidateUseCase.getProcessDocumentFiles(
                uploadDocumentControllerUtil.getDocumentSubtype(body), listGet, null);

        UploadDocumentWithLog uplDocWiLog = uploadDocumentControllerUtil.getUploadDocumentWithLog(
                acquisition, processDocumentFiles, body, listGet);

        return validateResponse(uplDocWiLog, acquisition, body);
    }

    public ResponseEntity<UploadDocumentApiResponse> validateResponse(
            UploadDocumentWithLog uplDocWiLog, Acquisition acquisition, UploadDocumentRequest body) {

        if (uplDocWiLog != null) {
            InfoReuseCommon infoReuseCommon = uplDocWiLog.getInfoReuseCommon() == null
                    ? InfoReuseCommon.builder().build() : uplDocWiLog.getInfoReuseCommon();
            uploadDocumentControllerUtil.finallyStep(genericStep, body.getData(), acquisition, infoReuseCommon);
            if (uplDocWiLog.getUploadDocumentTotal().getUploadDocumentResponse() != null) {
                UploadDocumentResponse uplDocRes = uplDocWiLog.getUploadDocumentTotal().getUploadDocumentResponse();
                return new ResponseEntity<>
                        (ResponseFactoryDigitization.buildUploadDocumentResponse(body, uplDocRes), HttpStatus.OK);
            }
        }
        return null;
    }

    private List<UploadDocumentFile> transformUploadDocumentFiles(UploadDocumentRequestData data) {
        List<UploadDocumentFile> listGet = new ArrayList<>();
        data.getFilesList().stream().forEach(item -> listGet.add(UploadDocumentFile.builder()
                .documentalSubTypeCode(item.getDocumentalSubTypeCode())
                .documentalTypeCode(item.getDocumentalTypeCode())
                .fileName(item.getFileName())
                .flagDataExtraction(item.getFlagDataExtraction())
                .flagSynchronous(item.getFlagSynchronous())
                .documentnumber(data.getDocumentNumber())
                .documentType(data.getDocumentType())
                .build()));
        return listGet;
    }
}
