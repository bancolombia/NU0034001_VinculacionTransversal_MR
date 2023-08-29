package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTARY_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOC_SEND_CONTINGENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OK_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.util.constants.Constants.TYPE_CC;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class PersistenceValidateDocUseCaseTest {

    @InjectMocks
    @Spy
    PersistenceValidateDocUseCaseImpl persistenceDocumentUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private Exceptions exceptions;

    @Mock
    private PersistenceValidationsUseCase persistenceValidationsUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateErrorTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        PersistenceDocumentList persistenceDocument = PersistenceDocumentList.builder().status(ERROR_STATE)
                .errorCode("1421").errorDescription("Description").typeDocumentary(DOCUMENTARY_TYPE)
                .subTypeDocumentary(CEDULA_SUBTYPE).build();
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(exceptions).createException(any(Map.class), anyString(), anyString(), anyString());
        assertNotNull(persistenceDocumentUseCase.validateError(persistenceDocument, acquisition, null));
    }

    @Test
    public void validateErrorExceptionTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        PersistenceDocumentList persistenceDocument = PersistenceDocumentList.builder().status(ERROR_STATE)
                .errorCode("1421").errorDescription("Description").typeDocumentary(DOCUMENTARY_TYPE)
                .subTypeDocumentary(CEDULA_SUBTYPE).build();
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(exceptions).createException(any(Map.class), anyString(), anyString(), anyString());
        persistenceDocumentUseCase.validateError(persistenceDocument, acquisition, "CC");
        verify(persistenceDocumentUseCase).validateError(persistenceDocument, acquisition, "CC");
    }

    @Test
    public void validateDocumentTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(OK_STATE)
                .errorCode(DOC_SEND_CONTINGENCY).typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE)
                .build();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder().status(ERROR_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(RUT_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocumentList.add(persistenceDocumentRUT);
        doNothing().when(persistenceValidationsUseCase).markState(any(PersistenceDocumentList.class),
                any(AcquisitionReply.class), anyString());
        persistenceDocumentUseCase.validateDocument(persistenceDocumentList, acquisition, CEDULA_SUBTYPE, TYPE_CC);
        verify(persistenceDocumentUseCase).validateDocument(persistenceDocumentList, acquisition, CEDULA_SUBTYPE,
                TYPE_CC);
    }

    @Test
    public void validateDocumentTwoTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder()
                .errorCode(DOC_SEND_CONTINGENCY).typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE)
                .build();
        PersistenceDocumentList persistenceDocumentRUT = PersistenceDocumentList.builder().status(ERROR_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(RUT_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocumentList.add(persistenceDocumentRUT);
        doNothing().when(persistenceValidationsUseCase).markState(any(PersistenceDocumentList.class),
                any(AcquisitionReply.class), anyString());
        persistenceDocumentUseCase.validateDocument(persistenceDocumentList, acquisition, CEDULA_SUBTYPE, TYPE_CC);
        verify(persistenceDocumentUseCase).validateDocument(persistenceDocumentList, acquisition, CEDULA_SUBTYPE,
                TYPE_CC);
    }

    @Test
    public void validateDocumentErrorTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(ERROR_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        doReturn("Error").when(persistenceDocumentUseCase).validateError(any(PersistenceDocumentList.class),
                any(AcquisitionReply.class), anyString());
        persistenceDocumentUseCase.validateDocument(persistenceDocumentList, acquisition, CEDULA_SUBTYPE, TYPE_CC);
        verify(persistenceDocumentUseCase).validateDocument(persistenceDocumentList, acquisition, CEDULA_SUBTYPE,
                TYPE_CC);
    }

    @Test
    public void validateDocumentErrorTwoTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status("asd")
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        doReturn("Error").when(persistenceDocumentUseCase).validateError(any(PersistenceDocumentList.class),
                any(AcquisitionReply.class), anyString());
        persistenceDocumentUseCase.validateDocument(persistenceDocumentList, acquisition, CEDULA_SUBTYPE, TYPE_CC);
        verify(persistenceDocumentUseCase).validateDocument(persistenceDocumentList, acquisition, CEDULA_SUBTYPE,
                TYPE_CC);
    }

    @Test
    public void validateDocumentNotPresentTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(ERROR_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        doReturn("Error").when(persistenceDocumentUseCase).validateError(any(PersistenceDocumentList.class),
                any(AcquisitionReply.class), anyString());
        persistenceDocumentUseCase.validateDocument(persistenceDocumentList, acquisition, RUT_SUBTYPE, TYPE_CC);
        verify(persistenceDocumentUseCase).validateDocument(persistenceDocumentList, acquisition, RUT_SUBTYPE,
                TYPE_CC);
    }
}
