package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.digitalizationprocesseddocuments.DigitalizationProcessedDocumentsUseCase;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.digitalizationprocesseddocuments.DigitalizationProcessedDocuments;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTARY_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_CODE_ALL_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_CONSUMING_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;

@RequiredArgsConstructor
public class PersistenceDocumentUseCaseImpl implements PersistenceDocumentUseCase {

    private final DigitalizationProcessedDocumentsUseCase digitalizationProcessedDocumentsUseCase;
    private final NaturalPersonUseCase naturalPersonUseCase;
    private final PersistenceDocumentValidationUseCase persistenceDocumentValidationUseCase;
    private final PersistenceProcessUseCase persistenceProcessUseCase;
    private final RetriesPersistenceDocumentUseCase retriesPersistenceDocumentUseCase;

    private static LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE,
            OPER_CUSTOMER_DOC_PERSISTENCE);

    @Override
    public PersistenceDocument startProcess(String documentCode, AcquisitionReply acquisitionReply, String messageId,
                                            String user) {
        TdcDocument tdcDocument = TdcDocument.builder()
                .acquisitionId(acquisitionReply.getAcquisitionId()).messageId(messageId)
                .documentNumber(acquisitionReply.getDocumentNumber())
                .documentType(acquisitionReply.getDocumentType()).build();
        Acquisition acquisition = Acquisition.builder().id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentType()).build())
                .typeAcquisition(TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .build();
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        if (DOCUMENT_CODE_ALL_DOCUMENTS.equals(documentCode)) {
            this.persistenceBothDocument(tdcDocument, documentsFileList, acquisition);
        } else if (CEDULA_SUBTYPE.equals(documentCode)) {
            this.persistenceCC(tdcDocument, documentsFileList, acquisition);
        } else if (documentCode.equals(RUT_SUBTYPE)) {
            this.persistenceRut(tdcDocument, documentsFileList, acquisition);
        }
        ValidateIdentityReply validateIdentityReply = naturalPersonUseCase.validateIdentity(acquisition.getId());
        tdcDocument.setExpedionDateCC(validateIdentityReply.getExpeditionDate());
        return persistenceDocumentValidationUseCase.persistenceDocumentTDC(tdcDocument, user, Boolean.FALSE);
    }

    @Override
    public void processResponse(AcquisitionReply acquisition, PersistenceDocument persistenceDocumentApiResponse,
                                boolean retries) {
        if (persistenceDocumentApiResponse.getData() != null) {
            if (retries) {
                retriesPersistenceDocumentUseCase.processResponse(acquisition, persistenceDocumentApiResponse);
            } else {
                persistenceProcessUseCase.processResponse(acquisition, persistenceDocumentApiResponse);
            }
        } else {
            adapter.error(ERROR_CONSUMING_CUSTOMER_DOC_PERSISTENCE);
        }
    }

    private void persistenceBothDocument(TdcDocument tdcDocument, List<TdcDocumentsFile> documentsFileList,
                                         Acquisition acquisition) {
        Optional<DigitalizationProcessedDocuments> cc = digitalizationProcessedDocumentsUseCase
                .findLastDigitalizationProcessedDocuments(acquisition,
                        DOCUMENTARY_TYPE, CEDULA_SUBTYPE);
        if (cc.isPresent()) {
            TdcDocumentsFile tdcDocumentsFile = TdcDocumentsFile.builder().documentalTypeCode(DOCUMENTARY_TYPE)
                    .documentalSubTypeCode(CEDULA_SUBTYPE).fileNames(cc.get().getFilesNames()).build();
            documentsFileList.add(tdcDocumentsFile);
        }
        Optional<DigitalizationProcessedDocuments> rut = digitalizationProcessedDocumentsUseCase
                .findLastDigitalizationProcessedDocuments(acquisition, DOCUMENTARY_TYPE, RUT_SUBTYPE);
        if (rut.isPresent()) {
            TdcDocumentsFile tdcDocumentsFile = TdcDocumentsFile.builder().documentalTypeCode(DOCUMENTARY_TYPE)
                    .documentalSubTypeCode(RUT_SUBTYPE).fileNames(rut.get().getFilesNames()).build();
            documentsFileList.add(tdcDocumentsFile);
        }
        tdcDocument.setDocumentsFileList(documentsFileList);
    }

    private void persistenceCC(TdcDocument tdcDocument, List<TdcDocumentsFile> documentsFileList,
                               Acquisition acquisition) {
        Optional<DigitalizationProcessedDocuments> cc = digitalizationProcessedDocumentsUseCase
                .findLastDigitalizationProcessedDocuments(acquisition, DOCUMENTARY_TYPE, CEDULA_SUBTYPE);
        if (cc.isPresent()) {
            TdcDocumentsFile tdcDocumentsFile = TdcDocumentsFile.builder().documentalTypeCode(DOCUMENTARY_TYPE)
                    .documentalSubTypeCode(CEDULA_SUBTYPE).fileNames(cc.get().getFilesNames()).build();
            documentsFileList.add(tdcDocumentsFile);
        }
        tdcDocument.setDocumentsFileList(documentsFileList);
    }

    private void persistenceRut(TdcDocument tdcDocument, List<TdcDocumentsFile> documentsFileList,
                                Acquisition acquisition) {
        Optional<DigitalizationProcessedDocuments> rut = digitalizationProcessedDocumentsUseCase
                .findLastDigitalizationProcessedDocuments(acquisition, DOCUMENTARY_TYPE, RUT_SUBTYPE);
        if (rut.isPresent()) {
            TdcDocumentsFile tdcDocumentsFile = TdcDocumentsFile.builder().documentalTypeCode(DOCUMENTARY_TYPE)
                    .documentalSubTypeCode(RUT_SUBTYPE).fileNames(rut.get().getFilesNames()).build();
            documentsFileList.add(tdcDocumentsFile);
        }
        tdcDocument.setDocumentsFileList(documentsFileList);
    }
}
