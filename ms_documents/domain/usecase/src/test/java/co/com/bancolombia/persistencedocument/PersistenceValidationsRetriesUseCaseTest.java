package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.util.constants.Constants.TYPE_CC;
import static co.com.bancolombia.util.constants.Constants.TYPE_RUT;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class PersistenceValidationsRetriesUseCaseTest {

    @InjectMocks
    @Spy
    PersistenceValidationsRetriesUseCaseImpl persistenceDocValUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private Exceptions exceptions;

    @Mock
    private LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE, OPER_CUSTOMER_DOC_PERSISTENCE);

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
        assertNotNull(persistenceDocValUseCase.validateError(persistenceDocument));
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
        persistenceDocValUseCase.validateError(persistenceDocument);
        verify(persistenceDocValUseCase).validateError(persistenceDocument);
    }

    @Test
    public void validateDocumentTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(OK_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(TYPE_CC).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        doNothing().when(persistenceDocValUseCase).markState(any(PersistenceDocumentList.class),
                any(AcquisitionReply.class), anyString());
        persistenceDocValUseCase.validateDocument(persistenceDocumentList, acquisition, TYPE_CC);
        verify(persistenceDocValUseCase).validateDocument(persistenceDocumentList, acquisition, TYPE_CC);
    }

    @Test
    public void validateDocumentTwoTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder()
                .errorCode(DOC_SEND_CONTINGENCY).typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(TYPE_CC).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        doNothing().when(persistenceDocValUseCase).markState(any(PersistenceDocumentList.class),
                any(AcquisitionReply.class), anyString());
        persistenceDocValUseCase.validateDocument(persistenceDocumentList, acquisition, TYPE_CC);
        verify(persistenceDocValUseCase).validateDocument(persistenceDocumentList, acquisition, TYPE_CC);
    }

    @Test
    public void validateDocumentAnyErrorTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder()
                .errorCode("asd").typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(TYPE_CC).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        doNothing().when(persistenceDocValUseCase).markState(any(PersistenceDocumentList.class),
                any(AcquisitionReply.class), anyString());
        persistenceDocValUseCase.validateDocument(persistenceDocumentList, acquisition, TYPE_CC);
        verify(persistenceDocValUseCase).validateDocument(persistenceDocumentList, acquisition, TYPE_CC);
    }

    @Test
    public void validateDocumentErrorTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(ERROR_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(TYPE_CC).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        doReturn("Error").when(persistenceDocValUseCase).validateError(any(PersistenceDocumentList.class));
        persistenceDocValUseCase.validateDocument(persistenceDocumentList, acquisition, TYPE_CC);
        verify(persistenceDocValUseCase).validateDocument(persistenceDocumentList, acquisition, TYPE_CC);
    }

    @Test
    public void validateDocumentNotPresentTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<PersistenceDocumentList> persistenceDocumentList = new ArrayList<>();
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().status(OK_STATE)
                .typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(TYPE_CC).build();
        persistenceDocumentList.add(persistenceDocumentCC);
        persistenceDocValUseCase.validateDocument(persistenceDocumentList, acquisition, TYPE_RUT);
        verify(persistenceDocValUseCase).validateDocument(persistenceDocumentList, acquisition, TYPE_RUT);
    }

    @Test
    public void markStateTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<String> fileName = new ArrayList<>();
        fileName.add("Name");
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().fileNameOriginal(fileName)
                .status(ERROR_STATE).typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(persistenceDocValUseCase).deleteFile(anyString());
        persistenceDocValUseCase.markState(persistenceDocumentCC, acquisition, "1");
        verify(persistenceDocValUseCase).markState(persistenceDocumentCC, acquisition, "1");
    }

    @Test
    public void markStateCompTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<String> fileName = new ArrayList<>();
        fileName.add("Name");
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().fileNameOriginal(fileName)
                .status(ERROR_STATE).typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(persistenceDocValUseCase).deleteFile(anyString());
        persistenceDocValUseCase.markState(persistenceDocumentCC, acquisition, "2");
        verify(persistenceDocValUseCase).markState(persistenceDocumentCC, acquisition, "2");
    }

    @Test
    public void markStateDefaultTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<String> fileName = new ArrayList<>();
        fileName.add("Name");
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().fileNameOriginal(fileName)
                .status(ERROR_STATE).typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        persistenceDocValUseCase.markState(persistenceDocumentCC, acquisition, "3");
        verify(persistenceDocValUseCase).markState(persistenceDocumentCC, acquisition, "3");
    }

    @Test
    public void deleteFileTest() {
        boolean flag = true;
        doReturn(flag).when(dataFileRepository).remove(anyString());
        persistenceDocValUseCase.deleteFile("nameFile");
        verify(persistenceDocValUseCase).deleteFile("nameFile");
    }

    @Test
    public void deleteFileFalseTest() {
        boolean flag = false;
        doReturn(flag).when(dataFileRepository).remove(anyString());
        persistenceDocValUseCase.deleteFile("nameFile");
        verify(persistenceDocValUseCase).deleteFile("nameFile");
    }
}

