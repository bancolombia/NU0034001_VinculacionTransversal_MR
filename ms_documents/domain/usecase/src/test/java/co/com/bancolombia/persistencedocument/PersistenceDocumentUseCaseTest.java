package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.digitalizationprocesseddocuments.DigitalizationProcessedDocumentsUseCase;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.digitalizationprocesseddocuments.DigitalizationProcessedDocuments;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENT_CODE_ALL_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class PersistenceDocumentUseCaseTest {

    @InjectMocks
    @Spy
    PersistenceDocumentUseCaseImpl persistenceDocumentUseCase;

    @Mock
    private DigitalizationProcessedDocumentsUseCase digitalizationProcessedDocumentsUseCase;
    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;
    @Mock
    private PersistenceDocumentValidationUseCase persistenceDocumentValidationUseCase;
    @Mock
    private PersistenceProcessUseCase persistenceProcessUseCase;
    @Mock
    private RetriesPersistenceDocumentUseCase retriesPersistenceDocumentUseCase;

    @Mock
    private LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE, OPER_CUSTOMER_DOC_PERSISTENCE);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startProcessAllDocuTest() {
        String user = "userTest";
        String messageId = "asd";
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString())
                .build();
        DigitalizationProcessedDocuments digitalizationProcessedDocuments = DigitalizationProcessedDocuments.builder()
                .build();
        doReturn(Optional.of(digitalizationProcessedDocuments)).when(digitalizationProcessedDocumentsUseCase)
                .findLastDigitalizationProcessedDocuments(any(Acquisition.class), anyString(), anyString());
        ValidateIdentityReply validateIdentityReply = ValidateIdentityReply.builder().expeditionDate(new Date())
                .build();
        doReturn(validateIdentityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().build();
        doReturn(persistenceDocument).when(persistenceDocumentValidationUseCase)
                .persistenceDocumentTDC(any(TdcDocument.class), anyString(), anyBoolean());
        PersistenceDocument result = persistenceDocumentUseCase.startProcess(DOCUMENT_CODE_ALL_DOCUMENTS,
                acquisitionReply, messageId, user);
        assertNotNull(result);
    }

    @Test
    public void startProcessCCTest() {
        String user = "userTest";
        String messageId = "asd";
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString())
                .build();
        DigitalizationProcessedDocuments digitalizationProcessedDocuments = DigitalizationProcessedDocuments.builder()
                .build();
        doReturn(Optional.of(digitalizationProcessedDocuments)).when(digitalizationProcessedDocumentsUseCase)
                .findLastDigitalizationProcessedDocuments(any(Acquisition.class), anyString(), anyString());
        ValidateIdentityReply validateIdentityReply = ValidateIdentityReply.builder().expeditionDate(new Date())
                .build();
        doReturn(validateIdentityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().build();
        doReturn(persistenceDocument).when(persistenceDocumentValidationUseCase)
                .persistenceDocumentTDC(any(TdcDocument.class), anyString(), anyBoolean());
        PersistenceDocument result = persistenceDocumentUseCase.startProcess(CEDULA_SUBTYPE,
                acquisitionReply, messageId, user);
        assertNotNull(result);
    }

    @Test
    public void startProcessRutTest() {
        String user = "userTest";
        String messageId = "asd";
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString())
                .build();
        DigitalizationProcessedDocuments digitalizationProcessedDocuments = DigitalizationProcessedDocuments.builder()
                .build();
        doReturn(Optional.of(digitalizationProcessedDocuments)).when(digitalizationProcessedDocumentsUseCase)
                .findLastDigitalizationProcessedDocuments(any(Acquisition.class), anyString(), anyString());
        ValidateIdentityReply validateIdentityReply = ValidateIdentityReply.builder().expeditionDate(new Date())
                .build();
        doReturn(validateIdentityReply).when(naturalPersonUseCase).validateIdentity(any(UUID.class));
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().build();
        doReturn(persistenceDocument).when(persistenceDocumentValidationUseCase)
                .persistenceDocumentTDC(any(TdcDocument.class), anyString(), anyBoolean());
        PersistenceDocument result = persistenceDocumentUseCase.startProcess(RUT_SUBTYPE,
                acquisitionReply, messageId, user);
        assertNotNull(result);
    }

    @Test
    public void processResponseRetriesTrueTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString())
                .build();
        List<PersistenceDocumentList> documentsList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().build();
        documentsList.add(persistenceDocumentList);
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().data(documentsList).build();
        doNothing().when(retriesPersistenceDocumentUseCase).processResponse(any(AcquisitionReply.class),
                any(PersistenceDocument.class));
        persistenceDocumentUseCase.processResponse(acquisitionReply,persistenceDocument,true);
        verify(persistenceDocumentUseCase, times(1)).processResponse(acquisitionReply,
                persistenceDocument, true);
    }

    @Test
    public void processResponseRetriesFalseTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString())
                .build();
        List<PersistenceDocumentList> documentsList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().build();
        documentsList.add(persistenceDocumentList);
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().data(documentsList).build();
        doNothing().when(persistenceProcessUseCase).processResponse(any(AcquisitionReply.class),
                any(PersistenceDocument.class));
        persistenceDocumentUseCase.processResponse(acquisitionReply,persistenceDocument,false);
        verify(persistenceDocumentUseCase, times(1)).processResponse(acquisitionReply,
                persistenceDocument, false);
    }

    @Test
    public void processResponseRetriesNullTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString())
                .build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().data(null).build();
        persistenceDocumentUseCase.processResponse(acquisitionReply,persistenceDocument,false);
        verify(persistenceDocumentUseCase, times(1)).processResponse(acquisitionReply,
                persistenceDocument, false);
    }
}
