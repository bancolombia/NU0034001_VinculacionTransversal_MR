package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.ProcessedDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.sqs.SqsMessageUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.IN_PROCCESS_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.IN_PROCCESS_DOCUMENT_REASON;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.N_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_CODE_ONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_CEDULA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_NAME_DIGITALIZATION;

@RequiredArgsConstructor
public class UploadDocumentSyncUseCaseImpl implements UploadDocumentSyncUseCase {

    private final SqsMessageUseCase sqsMessageUseCase;

    @Override
    public boolean validateAsynchronousProcess(List<UploadDocumentFile> listGet) {
        if (listGet != null) {
            List<UploadDocumentFile> list = new ArrayList<>();
            listGet.forEach(item -> {
                if (N_FOREIGN_INFORMATION.equals(item.getFlagSynchronous())) {
                    list.add(item);
                }
            });

            if (!list.isEmpty()) {
                return list.size() == listGet.size();
            }
        }

        return false;
    }

    @Override
    public UploadDocumentWithLog asynchronousProcess(
            UploadDocumentParameter uploadDocumentParameter, String messageId) {

        sqsMessageUseCase.sendSqsMessageProcessDocument(uploadDocumentParameter, messageId);

        return UploadDocumentWithLog.builder()
                .uploadDocumentTotal(
                        UploadDocumentTotal.builder()
                                .uploadDocumentResponse(
                                        UploadDocumentResponse.builder()
                                                .data(
                                                        UploadDocument.builder()
                                                                .processedDocument(Arrays.asList(
                                                                        ProcessedDocument.builder()
                                                                                .codeAnswerDocument(OUT_COME_CODE_ONE)
                                                                                .answerDocument(IN_PROCCESS_DOCUMENT)
                                                                                .reason(IN_PROCCESS_DOCUMENT_REASON)
                                                                                .build()))
                                                                .build())
                                                .build())
                                .build())
                .build();
    }

    @Override
    public ProcessDocument getRequest(
            Acquisition acquisition, List<ProcessDocumentFiles> processDocumentFiles,
            String documentSubtype, String messageId) {

        return ProcessDocument.builder()
                .userCode(Numbers.ONE.getIntNumber())
                .processName(PROCESS_NAME_DIGITALIZATION)
                .processCode(documentSubtype.equals(CEDULA_SUBTYPE) ? PROCESS_CODE_CEDULA : PROCESS_CODE_RUT)
                .files(processDocumentFiles).acquisition(
                        AcquisitionProcessDocument.builder()
                                .documentNumber(acquisition.getDocumentNumber())
                                .id(acquisition.getId())
                                .uploadDocumentRetries(acquisition.getUploadDocumentRetries())
                                .build())
                .messageId(messageId)
                .build();
    }
}
