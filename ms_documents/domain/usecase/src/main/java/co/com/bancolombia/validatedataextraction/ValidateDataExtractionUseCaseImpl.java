package co.com.bancolombia.validatedataextraction;

import co.com.bancolombia.asyncdigitalization.AsyncDigitalizationUseCase;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.validatedataextraction.MessageProcess;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentApiResponse;
import co.com.bancolombia.model.validatedataextraction.ValidateDataExtraction;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.sqs.SqsMessageUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentExcepUseCase;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ASYNC_DIGITALIZATION_FOUNDED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_RECHAZADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VAL_DATA_EXTRAC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_FOUND_BY_ACQ;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MESSAGE_NOT_FOUND_BY_DOC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_VALIDATE_DATA_EXTRACTION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SQS_MESSAGE_FOUNDED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STATE_COMPLETED;

@RequiredArgsConstructor
public class ValidateDataExtractionUseCaseImpl implements ValidateDataExtractionUseCase {

    private final AsyncDigitalizationUseCase asyncDigitalizationUseCase;
    private final SqsMessageUseCase sqsMessageUseCase;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final UploadDocumentExcepUseCase uploadDocumentExcepUseCase;
    private final ValidateDataExtractionLogUseCase validateDataExtractionLogUseCase;

    LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_DIGITALIZATION, OPER_VALIDATE_DATA_EXTRACTION);

    @Override
    public ValidateDataExtraction getAnswer(Acquisition acq, String documentalTypeCode, String documentalSubTypeCode,
            SqsMessageParamAllObject sqsMessageParamAllObject) {

        Optional<SqsMessage> sqsMessage = findValidMessage(acq, documentalTypeCode, documentalSubTypeCode);
        if (sqsMessage.isPresent()) {
            adapter.info(SQS_MESSAGE_FOUNDED + sqsMessage.get().getId() + STATE + sqsMessage.get().getStatusMessage());
            if (sqsMessage.get().getStatusMessage().equals(STATE_COMPLETED)) {
                Optional<AsyncDigitalization> asyncDig = asyncDigitalizationUseCase.findBySqsMessage(sqsMessage.get());
                String result;
                if (asyncDig.isPresent()) {
                    adapter.info(ASYNC_DIGITALIZATION_FOUNDED + asyncDig.get().getId());
                    if (asyncDig.get().getDataResponse() != null && asyncDig.get().getDataResponse().length() > 0) {
                        result = asyncDig.get().getDataResponse();
                        return getAnswerSuccess(acq, asyncDig.get(), result);
                    } else {
                        result = asyncDig.get().getErrorResponse();
                        getAnswerError(acq, asyncDig.get(), sqsMessageParamAllObject, result);
                    }
                }
            } else {
                uploadDocumentExcepUseCase.validateException(ConstantsErrors.ERROR_CODE_DATA_EXTRACTION_IN_PROGRESS,
                        OPER_VALIDATE_DATA_EXTRACTION, EMPTY, sqsMessageParamAllObject);
            }
        } else {
            uploadDocumentExcepUseCase.validateException
                    (ConstantsErrors.ERROR_CODE_NOT_VALIDATE_DATA_EXTRACTION_REQUIRED,
                            OPER_VALIDATE_DATA_EXTRACTION, EMPTY, sqsMessageParamAllObject);
        }
        return null;
    }

    @Override
    public void validateBusinessException(ProcessDocumentKofaxError processDocumentKofaxError,
            Acquisition acquisition, SqsMessageParamAllObject sqsMessageParamAllObject) {

        String code = processDocumentKofaxError.getCode();
        String detail = processDocumentKofaxError.getDetail();
        String complement = processDocumentKofaxError.getComplement();
        switch (code) {
            case ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY:
                vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.ONE.getNumber());
                this.vinculationUpdateUseCase.markOperation
                        (acquisition.getId(), CODE_VAL_DATA_EXTRAC, CODE_ST_OPE_RECHAZADO);
                break;
            case ConstantsErrors.ERROR_CODE_PROCESS_DOCUMENT:
                this.vinculationUpdateUseCase.markOperation
                        (acquisition.getId(), CODE_VAL_DATA_EXTRAC, CODE_ST_OPE_RECHAZADO);
                vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.TWO.getNumber());
                break;
            case ConstantsErrors.ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL:
            case ConstantsErrors.ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL:
                this.vinculationUpdateUseCase.markOperation
                        (acquisition.getId(), CODE_VAL_DATA_EXTRAC, Numbers.EIGHT.getNumber());
                break;
            default:
                break;
        }
        uploadDocumentExcepUseCase.validateException(code, detail, complement, sqsMessageParamAllObject);
    }

    @Override
    public Optional<SqsMessage> findValidMessage(
            Acquisition acquisition, String documentalTypeCode, String documentalSubTypeCode) {

        List<SqsMessage> sqsMessageList = sqsMessageUseCase.findMessageList(OPER_UPLOAD_DOCUMENT, acquisition);
        if (!sqsMessageList.isEmpty()) {
            Optional<SqsMessage> sqsMessage = sqsMessageList.stream().filter(sqs -> {
                String jsonMessage = sqs.getMessage();
                Gson gson = new Gson();
                MessageProcess processDocument = gson.fromJson(jsonMessage, MessageProcess.class);
                if (processDocument != null) {
                    return (documentalTypeCode.equals(processDocument.getData().getFiles().get(0)
                            .getDocumentalTypeCode()) && documentalSubTypeCode.equals(processDocument.getData()
                            .getFiles().get(0).getDocumentalSubTypeCode()));
                } else {
                    return false;
                }
            }).findFirst();
            if (sqsMessage.isPresent()) {
                return sqsMessage;
            } else {
                adapter.info(MESSAGE_NOT_FOUND_BY_DOC);
                return Optional.empty();
            }
        }
        adapter.info(MESSAGE_NOT_FOUND_BY_ACQ);
        return Optional.empty();
    }

    private ValidateDataExtraction getAnswerSuccess(Acquisition acq, AsyncDigitalization asyncDig, String result) {
        Gson gson = new Gson();
        UploadDocumentApiResponse objResponse = gson.fromJson(result, UploadDocumentApiResponse.class);
        vinculationUpdateUseCase.markOperation(acq.getId(), CODE_VAL_DATA_EXTRAC, CODE_ST_OPE_COMPLETADO);
        return validateDataExtractionLogUseCase.getObjectValid(asyncDig, objResponse.getData());
    }

    private void getAnswerError(Acquisition acq, AsyncDigitalization asyncDig,
            SqsMessageParamAllObject sqsMessageParamAllObject, String result) {

        Gson gson = new Gson();
        ProcessDocumentKofaxError objError = gson.fromJson(result, ProcessDocumentKofaxError.class);
        validateDataExtractionLogUseCase.saveInfoLog(asyncDig);
        validateBusinessException(objError, acq, sqsMessageParamAllObject);
    }
}