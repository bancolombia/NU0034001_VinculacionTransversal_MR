package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CUSTOMER_DOCUMENT_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DELETE_FILE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DELETE_FILE_SUCCESS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ST_COMPLETED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ST_PENDING;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.URL_BUCKET_DOC_PROCESSED;

@RequiredArgsConstructor
public class PersistenceValidationsUseCaseImpl implements PersistenceValidationsUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final DataFileRepository dataFileRepository;

    private static LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE,
            OPER_CUSTOMER_DOC_PERSISTENCE);

    @Override
    public void markState(PersistenceDocumentList persistenceDocument, AcquisitionReply acquisition, String state) {
        switch (state) {
            case ST_PENDING:
                vinculationUpdateUseCase.markOperation(UUID.fromString(acquisition.getAcquisitionId()),
                        Numbers.ONE.getNumber(), CODE_CUSTOMER_DOCUMENT_PERSISTENCE);
                vinculationUpdateUseCase.updateAcquisition(acquisition.getAcquisitionId(), Numbers.ONE.getNumber());
                persistenceDocument.getFileNameOriginal().forEach(this::deleteFile);
                break;
            case ST_COMPLETED:
                vinculationUpdateUseCase.markOperation(UUID.fromString(acquisition.getAcquisitionId()),
                        Numbers.TWO.getNumber(), CODE_CUSTOMER_DOCUMENT_PERSISTENCE);
                vinculationUpdateUseCase.updateAcquisition(acquisition.getAcquisitionId(), Numbers.THREE.getNumber());
                persistenceDocument.getFileNameOriginal().forEach(this::deleteFile);
                break;
            default:
                break;
        }
    }

    @Override
    public void deleteFile(String keyFile) {
        if (dataFileRepository.remove(URL_BUCKET_DOC_PROCESSED + keyFile)) {
            adapter.info(DELETE_FILE_SUCCESS + keyFile);
        } else {
            adapter.error(DELETE_FILE_ERROR + keyFile);
        }
    }
}
