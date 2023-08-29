package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxRequest;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.model.uploaddocument.gateways.ProcessDocumentRestRepository;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_CEDULA;

@RequiredArgsConstructor
public class ProcessDocumentUseCaseImpl implements ProcessDocumentUseCase {

    private final ProcessDocumentRestRepository processDocumentRestRepository;
    private final CoreFunctionDate coreFunctionDate;
    private final ProcessDocumentSaveUseCase processDocumentSaveUseCase;

    @Override
    public UploadDocumentWithLog consumeKofax(ProcessDocument processDocument, String userTransaction) {
        ProcessDocumentKofaxRequest processDocumentKofaxRequest = ProcessDocumentKofaxRequest.builder()
                .processCode(processDocument.getProcessCode())
                .processName(processDocument.getProcessName())
                .userCode(processDocument.getUserCode())
                .files(processDocument.getFiles())
                .build();

        UploadDocumentWithLog uploadDocumentWithLog = processDocumentRestRepository.getRest(
                        processDocumentKofaxRequest, processDocument.getMessageId(), coreFunctionDate.getDatetime());

        if (uploadDocumentWithLog.getUploadDocumentTotal().getUploadDocumentResponse() != null) {
            if (processDocument.getProcessCode().equals(PROCESS_CODE_CEDULA)) {
                processDocumentSaveUseCase.saveDigitalizationIdentity(
                        uploadDocumentWithLog.getUploadDocumentTotal().getUploadDocumentResponse(),
                        processDocument.getAcquisition(), userTransaction);
            } else {
                processDocumentSaveUseCase.saveDigitalizationRut(
                        uploadDocumentWithLog.getUploadDocumentTotal().getUploadDocumentResponse(),
                        processDocument.getAcquisition(), userTransaction);
            }
        }

        return uploadDocumentWithLog;
    }
}
