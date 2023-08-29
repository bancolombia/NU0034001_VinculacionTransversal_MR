package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.model.sqs.SqsMessaPararmSave;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentError;
import co.com.bancolombia.model.uploaddocument.UploadDocumentErrorResponse;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.sqs.SqsMessageUplDocUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_RECHAZADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PROCESS_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TITLE;

@RequiredArgsConstructor
public class UploadDocumentExcepUseCaseImpl implements UploadDocumentExcepUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final SqsMessageUplDocUseCase sqsMessageUplDocUseCase;
    private final UploadDocumentProcessedDocumentsUseCase uploadDocumentProcessedDocumentsUseCase;
    private final DocumentRetriesUseCase documentRetriesUseCase;

    @Override
    public void validateBackEndException(
            UploadDocumentApiResponse uploadDocumentApiResponse, SqsMessageParamAllObject sqsMessageParamAllObject) {

        UploadDocumentErrorResponse uDErrorResponse = uploadDocumentApiResponse.getData()
                .getUploadDocumentTotal().getUploadDocumentErrorResponse();

        UploadDocumentError uDError = uDErrorResponse.getErrors().get(0);

        String complement = CODE_ERROR.concat(uDError.getCode()).concat(SPACE).concat(TITLE)
                .concat(uDErrorResponse.getTitle()).concat(SPACE).concat(DETAIL).concat(uDError.getDetail());
        validateException(ERROR_CODE_SYSTEM, OPER_UPLOAD_DOCUMENT, complement, sqsMessageParamAllObject);
    }

    @Override
    public void validateException(
            String exception, String detail, String complement, SqsMessageParamAllObject sqsMessageParamAllObject) {

        HashMap<String, List<ErrorField>> error = new HashMap<>();
        ErrorField errorField = ErrorField.builder().name(detail).complement(complement).build();
        List<ErrorField> eFieldList = new ArrayList<>();
        eFieldList.add(errorField);
        error.put(exception, eFieldList);

        if (sqsMessageParamAllObject != null && sqsMessageParamAllObject.getSqsMessage() != null) {
            ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                    .code(exception).detail(detail)
                    .complement(complement)
                    .build();

            sqsMessageParamAllObject.getUploadDocumentApiResponse().getData()
                    .setProcessDocumentKofaxTotal(
                            getDocumentKofaxWithError(sqsMessageParamAllObject, processDocumentKofaxError));

            saveAndUpdateSqsMessage(sqsMessageParamAllObject);
        } else {
            throw new ValidationException(error);
        }
    }

    @Override
    public void validateBusinessException(
            ProcessDocumentKofaxError processDocumentKofaxError, Acquisition acquisition,
            SqsMessageParamAllObject sqsMessageParamAllObject) {

        String code = processDocumentKofaxError.getCode();
        String detail = processDocumentKofaxError.getDetail();
        UUID acqId = acquisition.getId();

        switch (code) {
            case ERROR_CODE_UPLOAD_DOCUMENT_RETRY:
                errorUploadDocumentRetry(acquisition, detail, sqsMessageParamAllObject);
                break;
            case ERROR_CODE_PROCESS_DOCUMENT:
                vinculationUpdateUseCase.markOperation(acqId, CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_RECHAZADO);
                vinculationUpdateUseCase.updateAcquisition(acqId.toString(), Numbers.TWO.getNumber());
                validateException(ERROR_CODE_PROCESS_DOCUMENT, detail, EMPTY, sqsMessageParamAllObject);
                break;
            case ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL:
                errorAvailableFillOutManual(acquisition, detail, sqsMessageParamAllObject);
                break;
            case ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL:
                vinculationUpdateUseCase.markOperation(acqId, CODE_PROCESS_DOCUMENTS, Numbers.EIGHT.getNumber());
                uploadDocumentProcessedDocumentsUseCase.saveProcessedDocuments(
                        sqsMessageParamAllObject.getUploadDocumentParameter(),
                        sqsMessageParamAllObject
                                .getUploadDocumentApiResponse().getData().getInfoReuseCommon().getDateResponseReuse(),
                        sqsMessageParamAllObject, new HashMap<>());
                validateException(ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL, detail, EMPTY, sqsMessageParamAllObject);
                break;
            default:
                break;
        }
    }

    private void saveAndUpdateSqsMessage(SqsMessageParamAllObject sqsMessageParamAllObject) {
        sqsMessageUplDocUseCase.saveAndUpdateSqsMessage(
                SqsMessaPararmSave.builder()
                        .acquisition(sqsMessageParamAllObject.getAcquisition())
                        .obj(sqsMessageParamAllObject.getSqsMessObjUploadDoc())
                        .uploadDocumentWithLog(sqsMessageParamAllObject.getUploadDocumentApiResponse().getData())
                        .sqsMessage(sqsMessageParamAllObject.getSqsMessage())
                        .saveAsync(true)
                        .build());
    }

    private ProcessDocumentKofaxTotal getDocumentKofaxWithError(
            SqsMessageParamAllObject sqsMessageParamAllObject, ProcessDocumentKofaxError processDocumentKofaxError) {

        ProcessDocumentKofaxTotal prDKT = sqsMessageParamAllObject.getUploadDocumentApiResponse().getData()
                .getProcessDocumentKofaxTotal();

        if(prDKT == null) {
            prDKT = ProcessDocumentKofaxTotal.builder()
                    .processDocumentKofaxError(processDocumentKofaxError)
                    .build();
        } else {
            prDKT.setProcessDocumentKofaxError(processDocumentKofaxError);
        }

        return prDKT;
    }

    private void errorUploadDocumentRetry(
            Acquisition acquisition, String detail, SqsMessageParamAllObject sqsMessageParamAllObject) {

        if (detail.equalsIgnoreCase(TYPE_RUT)) {
            Integer retries = acquisition.getUploadRutRetries() + 1;
            acquisition.setUploadRutRetries(retries);
        } else {
            Integer retries = acquisition.getUploadDocumentRetries() + 1;
            acquisition.setUploadDocumentRetries(retries);
        }

        documentRetriesUseCase.save(acquisition);
        vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.ONE.getNumber());
        vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_RECHAZADO);
        validateException(ERROR_CODE_UPLOAD_DOCUMENT_RETRY, detail, EMPTY, sqsMessageParamAllObject);
    }

    private void errorAvailableFillOutManual(
            Acquisition acquisition, String detail, SqsMessageParamAllObject sqsMessageParamAllObject) {

        vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_PROCESS_DOCUMENTS, Numbers.EIGHT.getNumber());
        uploadDocumentProcessedDocumentsUseCase.saveProcessedDocuments(
                sqsMessageParamAllObject.getUploadDocumentParameter(),
                sqsMessageParamAllObject
                        .getUploadDocumentApiResponse().getData().getInfoReuseCommon().getDateResponseReuse(),
                sqsMessageParamAllObject, new HashMap<>());
        validateException(ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL, detail, EMPTY, sqsMessageParamAllObject);
    }
}
