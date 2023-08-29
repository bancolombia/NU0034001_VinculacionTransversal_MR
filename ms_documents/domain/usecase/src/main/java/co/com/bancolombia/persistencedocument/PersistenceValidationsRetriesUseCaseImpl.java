package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CUSTOMER_DOCUMENT_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DELETE_FILE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DELETE_FILE_SUCCESS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOC_SEND_CONTINGENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OK_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ST_COMPLETED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ST_PENDING;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.URL_BUCKET_DOC_PROCESSED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;

@RequiredArgsConstructor
public class PersistenceValidationsRetriesUseCaseImpl implements PersistenceValidationsRetriesUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final DataFileRepository dataFileRepository;

    private static LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE,
            OPER_CUSTOMER_DOC_PERSISTENCE);

    @Override
    public String validateError(PersistenceDocumentList persistenceDocument) {
        String code = persistenceDocument.getErrorCode();
        String description = persistenceDocument.getErrorDescription();
        return CODE_ERROR.concat(code).concat(SPACE).concat(DETAIL).concat(description);
    }

    @Override
    public void validateDocument(List<PersistenceDocumentList> data, AcquisitionReply acquisition,
                                 String codeDocument) {
        Optional<PersistenceDocumentList> persistenceDocumentFound = data.stream().filter(
                persistenceDocument -> persistenceDocument.getSubTypeDocumentary().equals(codeDocument)).findFirst();
        if (persistenceDocumentFound.isPresent()) {
            if (OK_STATE.equals(persistenceDocumentFound.get().getStatus())
                    || DOC_SEND_CONTINGENCY.equals(persistenceDocumentFound.get().getErrorCode())) {
                markState(persistenceDocumentFound.get(), acquisition, ST_COMPLETED);
            } else if (ERROR_STATE.equals(persistenceDocumentFound.get().getStatus())) {
                validateError(persistenceDocumentFound.get());
            }
        }
    }

    @Override
    public void markState(PersistenceDocumentList persistenceDocument, AcquisitionReply acquisition, String stateAux) {
        switch (stateAux) {
            case ST_PENDING:
                persistenceDocument.getFileNameOriginal().forEach(this::deleteFile);
                break;
            case ST_COMPLETED:
                vinculationUpdateUseCase.updateAcquisition(acquisition.getAcquisitionId(), Numbers.THREE.getNumber());
                vinculationUpdateUseCase.markOperation(UUID.fromString(acquisition.getAcquisitionId()),
                        Numbers.NINE.getNumber(), CODE_CUSTOMER_DOCUMENT_PERSISTENCE);
                persistenceDocument.getFileNameOriginal().forEach(this::deleteFile);
                break;
            default:
                break;
        }
    }

    @Override
    public void deleteFile(String keyFile) {
        if (!dataFileRepository.remove(URL_BUCKET_DOC_PROCESSED + keyFile)) {
            adapter.error(DELETE_FILE_ERROR + keyFile);
        } else {
            adapter.info(DELETE_FILE_SUCCESS + keyFile);
        }
    }
}
