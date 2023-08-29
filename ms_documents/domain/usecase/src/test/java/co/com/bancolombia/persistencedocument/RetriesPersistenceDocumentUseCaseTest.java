package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTARY_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOC_SEND_CONTINGENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OK_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_RUT;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class RetriesPersistenceDocumentUseCaseTest {

    @InjectMocks
    @Spy
    RetriesPersistenceDocumentUseCaseImpl persistenceDocumentUseCase;

    @Mock
    private PersistenceValidationsRetriesUseCase pVRetriesUC;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processResponseTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<ErrorField> errorFields = new ArrayList<>();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder()
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder()
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(RUT_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocumentList.add(persistenceDocumentRUT);
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder().data(persistenceDocumentList)
                .build();
        doReturn(errorFields).when(persistenceDocumentUseCase).validateCcAndRut(any(AcquisitionReply.class), anyList());
        this.persistenceDocumentUseCase.processResponse(acquisition, persistenceDocumentWithLog);
        verify(this.persistenceDocumentUseCase).processResponse(acquisition, persistenceDocumentWithLog);
    }

    @Test
    public void processResponseExceptionTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<ErrorField> errorFields = new ArrayList<>();
        errorFields.add(ErrorField.builder().name(TYPE_RUT).complement("complement").build());
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder()
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder()
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(RUT_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocumentList.add(persistenceDocumentRUT);
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder().data(persistenceDocumentList)
                .build();
        doReturn(errorFields).when(persistenceDocumentUseCase).validateCcAndRut(any(AcquisitionReply.class), anyList());
        this.persistenceDocumentUseCase.processResponse(acquisition, persistenceDocumentWithLog);
        verify(this.persistenceDocumentUseCase).processResponse(acquisition, persistenceDocumentWithLog);
    }

    @Test
    public void processResponseCCTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder()
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder().data(persistenceDocumentList)
                .build();
        doNothing().when(pVRetriesUC).validateDocument(anyList(), any(AcquisitionReply.class), anyString());
        this.persistenceDocumentUseCase.processResponse(acquisition, persistenceDocumentWithLog);
        verify(this.persistenceDocumentUseCase).processResponse(acquisition, persistenceDocumentWithLog);
    }

    @Test
    public void processResponseRutTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder()
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(RUT_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentRUT);
        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder().data(persistenceDocumentList)
                .build();
        doNothing().when(pVRetriesUC).validateDocument(anyList(), any(AcquisitionReply.class), anyString());
        this.persistenceDocumentUseCase.processResponse(acquisition, persistenceDocumentWithLog);
        verify(this.persistenceDocumentUseCase).processResponse(acquisition, persistenceDocumentWithLog);
    }

    @Test
    public void validateCcAndRutTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(OK_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder().status(OK_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(RUT_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocumentList.add(persistenceDocumentRUT);
        doNothing().when(pVRetriesUC).markState(any(PersistenceDocumentList.class), any(AcquisitionReply.class),
                anyString());
        this.persistenceDocumentUseCase.validateCcAndRut(acquisition, persistenceDocumentList);
        verify(this.persistenceDocumentUseCase).validateCcAndRut(acquisition, persistenceDocumentList);
    }

    @Test
    public void validateCcAndRutErrorTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder()
                .errorCode(DOC_SEND_CONTINGENCY).errorDescription("Description").typeDocumentary(DOCUMENTARY_TYPE)
                .subTypeDocumentary(CEDULA_SUBTYPE).build();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder().status(ERROR_STATE)
                .errorCode("1421").errorDescription("Desc").typeDocumentary(DOCUMENTARY_TYPE)
                .subTypeDocumentary(RUT_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocumentList.add(persistenceDocumentRUT);
        doNothing().when(pVRetriesUC).markState(any(PersistenceDocumentList.class), any(AcquisitionReply.class),
                anyString());
        doReturn("Error").when(pVRetriesUC).validateError(any(PersistenceDocumentList.class));
        assertNotNull(persistenceDocumentUseCase.validateCcAndRut(acquisition, persistenceDocumentList));
    }

    @Test
    public void validateCcAndRutErrorCCTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(ERROR_STATE)
                .errorCode("1134").errorDescription("Description").typeDocumentary(DOCUMENTARY_TYPE)
                .subTypeDocumentary(CEDULA_SUBTYPE).build();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder().status(ERROR_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(RUT_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocumentList.add(persistenceDocumentRUT);
        doNothing().when(pVRetriesUC).markState(any(PersistenceDocumentList.class), any(AcquisitionReply.class),
                anyString());
        doReturn("Error").when(pVRetriesUC).validateError(any(PersistenceDocumentList.class));
        assertNotNull(persistenceDocumentUseCase.validateCcAndRut(acquisition, persistenceDocumentList));
    }

    @Test
    public void validateCcAndRutErrorRutTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(OK_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder().status(ERROR_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(RUT_SUBTYPE).errorCode("1421")
                .errorDescription("Description").build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocumentList.add(persistenceDocumentRUT);
        doNothing().when(pVRetriesUC).markState(any(PersistenceDocumentList.class), any(AcquisitionReply.class),
                anyString());
        doReturn("Error").when(pVRetriesUC).validateError(any(PersistenceDocumentList.class));
        assertNotNull(persistenceDocumentUseCase.validateCcAndRut(acquisition, persistenceDocumentList));
    }
}
