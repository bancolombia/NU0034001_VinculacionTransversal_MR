package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentError;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentResponse;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.model.persistencedocument.gateways.DocumentPersistenceRestRepository;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.parameters.ParametersUseCase;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_DOC_PUBLICATION_PROCESS;
import static co.com.bancolombia.util.constants.Constants.DOC_PROCESSED_BUCKET_FOLDER;
import static co.com.bancolombia.util.constants.Constants.ERROR_DETAIL_FETCHING_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.ERROR_DETAIL_TIMEOUT_PERSISTING_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.MESSAGE_CONSUMING_SOAP_SERVICE;
import static co.com.bancolombia.util.constants.Constants.NAME_TIMEOUT_PARAMETER;
import static co.com.bancolombia.util.constants.Constants.PARENT_DOC_PERSISTENCE;

@RequiredArgsConstructor
public class PersistenceDocumentValidateUseCaseImpl implements PersistenceDocumentValidateUseCase {

    private final DocumentPersistenceRestRepository documentPersistenceRestRepository;
    private final DataFileRepository dataFileRepositoryAdapter;
    private final ParametersUseCase parametersUseCase;
    private final PersistenceValidateTypeDocument persistenceValidateTypeDocument;

    private static LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE,
            OPER_CUSTOMER_DOC_PERSISTENCE);

    @Override
    public PersistenceDocument getPersistenceDocumentWithLog(TdcDocument tdcDocument,
                                                             TdcDocumentsFile tdcDocumentsFile) {
        String base64File = EMPTY;
        try {
            if (tdcDocumentsFile.getFileNames().size() > 1) {
                dataFileRepositoryAdapter.getBase64File(tdcDocumentsFile.getFileNames().get(0),
                        DOC_PROCESSED_BUCKET_FOLDER);
                dataFileRepositoryAdapter.getBase64File(tdcDocumentsFile.getFileNames().get(1),
                        DOC_PROCESSED_BUCKET_FOLDER);
                base64File = persistenceValidateTypeDocument.getBase64DiffExtension(tdcDocumentsFile, tdcDocument);
            } else {
                base64File = dataFileRepositoryAdapter.getBase64File(tdcDocumentsFile.getFileNames().get(0),
                        DOC_PROCESSED_BUCKET_FOLDER);
            }
        } catch (IOException | CustomException e) {
            PersistenceDocumentError persistenceDocumentError = filesNotFound();
            return PersistenceDocument.builder().persistenceDocumentError(persistenceDocumentError)
                    .persistenceDocumentResponse(PersistenceDocumentResponse.builder().build()).build();
        }
        return persistOneDocument(tdcDocument, tdcDocumentsFile, base64File, EMPTY);
    }

    private PersistenceDocument persistOneDocument(TdcDocument tdcDocument,
                                                   TdcDocumentsFile tdcDocumentsFile,
                                                   String base64File, String rutExpeditionDate) {
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder().build();
        Future<PersistenceDocument> future = documentPersistenceRestRepository.getPersistence(tdcDocument,
                tdcDocumentsFile, base64File, rutExpeditionDate);
        try {
            persistenceDocumentWithLog = future.get(this.getTimeOutTime(), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(Boolean.TRUE);
            PersistenceDocumentError persistenceDocumentError = PersistenceDocumentError.builder()
                    .code(ERROR_CODE_DOC_PUBLICATION_PROCESS).description(ERROR_DETAIL_TIMEOUT_PERSISTING_DOCUMENT)
                    .build();
            persistenceDocumentWithLog.setPersistenceDocumentError(persistenceDocumentError);
            persistenceDocumentWithLog.setPersistenceDocumentResponse(PersistenceDocumentResponse.builder().build());
        } catch (InterruptedException | ExecutionException e) {
            adapter.error(MESSAGE_CONSUMING_SOAP_SERVICE, e);
            Thread.currentThread().interrupt();
        }
        return persistenceDocumentWithLog;
    }

    private PersistenceDocumentError filesNotFound() {
        return PersistenceDocumentError.builder()
                .code(ERROR_CODE_DOC_PUBLICATION_PROCESS)
                .description(ERROR_DETAIL_FETCHING_DOCUMENT)
                .build();
    }

    private Long getTimeOutTime() {
        long timeOut = Numbers.ONE.getIntNumber();
        Optional<Parameters> timeOutParameter = parametersUseCase.findByNameAndParent(NAME_TIMEOUT_PARAMETER,
                PARENT_DOC_PERSISTENCE);
        if (timeOutParameter.isPresent()) {
            timeOut = Long.parseLong(timeOutParameter.get().getCode());
        }
        return timeOut;
    }
}
