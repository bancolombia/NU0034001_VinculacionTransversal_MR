package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
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
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTARY_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_STATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class PersistenceValidationsUseCaseTest {

    @InjectMocks
    @Spy
    PersistenceValidationsUseCaseImpl persistenceDocumentUseCase;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;
    @Mock
    DataFileRepository dataFileRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
        doNothing().when(persistenceDocumentUseCase).deleteFile(anyString());
        persistenceDocumentUseCase.markState(persistenceDocumentCC, acquisition, "1");
        verify(persistenceDocumentUseCase).markState(persistenceDocumentCC, acquisition, "1");
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
        doNothing().when(persistenceDocumentUseCase).deleteFile(anyString());
        persistenceDocumentUseCase.markState(persistenceDocumentCC, acquisition, "2");
        verify(persistenceDocumentUseCase).markState(persistenceDocumentCC, acquisition, "2");
    }

    @Test
    public void markStateDefaultTest() {
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(UUID.randomUUID().toString()).build();
        List<String> fileName = new ArrayList<>();
        fileName.add("Name");
        PersistenceDocumentList persistenceDocumentCC = PersistenceDocumentList.builder().fileNameOriginal(fileName)
                .status(ERROR_STATE).typeDocumentary(DOCUMENTARY_TYPE).subTypeDocumentary(CEDULA_SUBTYPE).build();
        persistenceDocumentUseCase.markState(persistenceDocumentCC, acquisition, "3");
        verify(persistenceDocumentUseCase).markState(persistenceDocumentCC, acquisition, "3");
    }

    @Test
    public void deleteFileTest() {
        boolean flag = true;
        doReturn(flag).when(dataFileRepository).remove(anyString());
        persistenceDocumentUseCase.deleteFile("nameFile");
        verify(persistenceDocumentUseCase).deleteFile("nameFile");
    }

    @Test
    public void deleteFileFalseTest() {
        boolean flag = false;
        doReturn(flag).when(dataFileRepository).remove(anyString());
        persistenceDocumentUseCase.deleteFile("nameFile");
        verify(persistenceDocumentUseCase).deleteFile("nameFile");
    }

}
