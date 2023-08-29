package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.model.persistencedocument.gateways.PersistenceDocumentRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOC_SEND_CONTINGENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_APPLY_SIMULATED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_DOC_PUBLICATION_PROCESS;
import static co.com.bancolombia.util.constants.Constants.MESSAGE_CONSUMING_SOAP_SERVICE;
import static co.com.bancolombia.util.constants.Constants.NO_APPLY;
import static co.com.bancolombia.util.constants.Constants.STATUS_ERROR;
import static co.com.bancolombia.util.constants.Constants.STATUS_OK;

@RequiredArgsConstructor
public class PersistenceDocumentValidationUseCaseImpl implements PersistenceDocumentValidationUseCase {

    private final PersistenceDocumentValidateUseCase perDValidateUC;
    private final PersistenceQueueUseCase persistenceQueueUseCase;
    private final CoreFunctionDate coreFunctionDate;
    private final PersistenceDocumentRepository persistenceDocumentRepository;

    @Override
    public PersistenceDocument persistenceDocumentTDC(TdcDocument tdcDocument, String user, boolean retries) {
        List<TdcDocumentsFile> dFiles = tdcDocument.getDocumentsFileList();
        if (dFiles.size() > Numbers.ONE.getIntNumber()) {
            return getListDocumentsR(tdcDocument, user, retries, dFiles);
        } else {
            List<PersistenceDocumentList> data = new ArrayList<>();
            InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
            PersistenceDocument perLog3 = perDValidateUC.getPersistenceDocumentWithLog(tdcDocument, dFiles.get(0));
            PersistenceDocumentList persistenceDocumentCC = processDocumentResponse(perLog3, tdcDocument, dFiles.get(0),
                    user);
            sendMessagesToRetriesQueue(persistenceDocumentCC, tdcDocument, retries);
            data.add(persistenceDocumentCC);
            if (perLog3.getInfoReuseCommon() != null) {
                infoReuseCommon.setDateRequestReuse(perLog3.getInfoReuseCommon().getDateRequestReuse());
                infoReuseCommon.setDateResponseReuse(perLog3.getInfoReuseCommon().getDateResponseReuse());
            }
            return PersistenceDocument.builder().data(data).infoReuseCommon(infoReuseCommon).build();
        }
    }

    public PersistenceDocument getListDocumentsR(TdcDocument tdcD, String user, boolean retries,
                                                 List<TdcDocumentsFile> dFiles) {
        List<PersistenceDocumentList> data = new ArrayList<>();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = perDValidateUC.getPersistenceDocumentWithLog(tdcD, dFiles.get(0));
        PersistenceDocumentList perDocumentCC = processDocumentResponse(persistenceDocument, tdcD, dFiles.get(0), user);
        data.add(0, perDocumentCC);
        if (persistenceDocument.getInfoReuseCommon() != null) {
            infoReuseCommon.setDateRequestReuse(persistenceDocument.getInfoReuseCommon().getDateRequestReuse());
            infoReuseCommon.setDateResponseReuse(persistenceDocument.getInfoReuseCommon().getDateResponseReuse());
        }
        if (STATUS_OK.equals(perDocumentCC.getStatus()) ||
                DOC_SEND_CONTINGENCY.equals(perDocumentCC.getErrorCode())) {
            PersistenceDocument perLog2 = perDValidateUC.getPersistenceDocumentWithLog(tdcD, dFiles.get(1));
            PersistenceDocumentList perDocumentRUT = processDocumentResponse(perDValidateUC
                    .getPersistenceDocumentWithLog(tdcD, dFiles.get(1)), tdcD, dFiles.get(1), user);
            data.add(1, perDocumentRUT);
            if (perLog2.getInfoReuseCommon() != null) {
                infoReuseCommon.setDateResponseReuse(perLog2.getInfoReuseCommon().getDateResponseReuse());
            }
        } else {
            sendMessagesToRetriesQueue(perDocumentCC, tdcD, retries);
            PersistenceDocumentList persistenceDocumentRUT = mapperObjectPersistenceDoc(perDocumentCC, dFiles.get(1),
                    user);
            data.add(1, persistenceDocumentRUT);
            save(persistenceDocumentRUT);
        }
        return PersistenceDocument.builder().data(data).infoReuseCommon(infoReuseCommon).build();
    }

    @Override
    public PersistenceDocumentList save(PersistenceDocumentList persistenceDocument) {
        return persistenceDocumentRepository.save(persistenceDocument);
    }

    public PersistenceDocumentList processDocumentResponse(PersistenceDocument pDocument, TdcDocument tdcDocument,
                                                           TdcDocumentsFile tdcFiles, String user) {
        String errorC = NO_APPLY;
        String errorD = NO_APPLY;
        String idDocument = NO_APPLY;
        String status;
        if (pDocument.getPersistenceDocumentResponse().getIdDocument() != null) {
            Optional<String> optionalIdDocument = Optional.ofNullable(pDocument.getPersistenceDocumentResponse()
                    .getIdDocument());
            idDocument = optionalIdDocument.orElse(EMPTY);
            status = STATUS_OK;
        } else if (pDocument.getPersistenceDocumentError().getCode() != null) {
            errorC = pDocument.getPersistenceDocumentError().getCode();
            status = STATUS_ERROR;
            if (pDocument.getPersistenceDocumentError().getDescription() != null) {
                errorD = pDocument.getPersistenceDocumentError().getDescription();
            }
        } else {
            errorC = ERROR_CODE_DOC_PUBLICATION_PROCESS;
            errorD = MESSAGE_CONSUMING_SOAP_SERVICE;
            status = STATUS_ERROR;
        }
        List<String> fileNameList = new ArrayList<>(tdcFiles.getFileNames());
        PersistenceDocumentList pDocumentR = PersistenceDocumentList.builder().idDocument(idDocument).errorCode(errorC)
                .acquisitionId(UUID.fromString(tdcDocument.getAcquisitionId())).status(status).errorDescription(errorD)
                .documentNumber(tdcDocument.getDocumentNumber()).typeDocumentary(tdcFiles.getDocumentalTypeCode())
                .subTypeDocumentary(tdcFiles.getDocumentalSubTypeCode()).fileNameOriginal(fileNameList).build();
        pDocumentR.setCreatedBy(user);
        pDocumentR.setCreatedDate(coreFunctionDate.getDatetime());
        return save(pDocumentR);
    }

    public PersistenceDocumentList mapperObjectPersistenceDoc(PersistenceDocumentList persistenceDocument,
                                                              TdcDocumentsFile tdcDocumentsFile, String user) {
        PersistenceDocumentList persistenceDocumentR = PersistenceDocumentList.builder().status(STATUS_ERROR)
                .documentNumber(persistenceDocument.getDocumentNumber()).idDocument(NO_APPLY_SIMULATED)
                .errorCode(persistenceDocument.getErrorCode()).typeDocumentary(tdcDocumentsFile.getDocumentalTypeCode())
                .errorDescription(persistenceDocument.getErrorDescription())
                .fileNameOriginal(tdcDocumentsFile.getFileNames()).acquisitionId(persistenceDocument.getAcquisitionId())
                .subTypeDocumentary(tdcDocumentsFile.getDocumentalSubTypeCode()).build();
        persistenceDocumentR.setCreatedDate(coreFunctionDate.getDatetime());
        persistenceDocumentR.setCreatedBy(user);
        return persistenceDocumentR;
    }

    private void sendMessagesToRetriesQueue(PersistenceDocumentList persistenceDocument, TdcDocument tdcDocument,
                                            boolean retries) {
        if (!persistenceDocument.getErrorDescription().equals(NO_APPLY) && !retries) {
            persistenceQueueUseCase.sendMessagesToQueue(tdcDocument);
        }
    }
}
