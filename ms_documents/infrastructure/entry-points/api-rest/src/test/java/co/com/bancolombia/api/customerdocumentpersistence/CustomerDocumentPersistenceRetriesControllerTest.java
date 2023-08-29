package co.com.bancolombia.api.customerdocumentpersistence;

import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.customerdocumentpersistence.CustomerPersistenceDocumentRequestData;
import co.com.bancolombia.api.model.customerdocumentpersistence.DocumentPersistenceRetriesRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalDTO;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.persistencedocument.PersistenceDocumentUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DETAIL_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT_APPLY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PUBLISHING_RESULT_SECOND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TITTLE_SECOND;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class CustomerDocumentPersistenceRetriesControllerTest {

    @InjectMocks
    @Spy
    CustomerDocumentPersistenceRetriesController persistenceDocumentController;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private PersistenceDocumentUseCase persistenceDocumentUseCase;

    @Mock
    private CustomerDocumentPersistenceLogController dPLogController;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @SneakyThrows
    @Test
    public void contextLoads() {
        assertNotNull(persistenceDocumentController);
        assertNotNull(persistenceDocumentUseCase);
    }

    @SneakyThrows
    @Test
    public void persistenceDocumentTest() {
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Collections.singletonList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").subTypeDocumentary(CEDULA_SUBTYPE).build());
        List<PersistenceDocument> data = Collections.singletonList(PersistenceDocument.builder()
                .infoReuseCommon(InfoReuseCommon.builder().build()).data(listList).documentType("")
                .initOperation(new Date()).build());
        DocumentPersistenceRetriesRequest persistenceDocumentRequest = new DocumentPersistenceRetriesRequest(data);

        Mockito.doReturn(AcquisitionReply.builder().acquisitionId(uuidCode.toString()).build())
                .when(vinculationUpdateUseCase).validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doNothing().when(persistenceDocumentUseCase).processResponse(any(AcquisitionReply.class),
                any(PersistenceDocument.class), anyBoolean());
        Mockito.doReturn(StepForLogFunctional.builder().build()).when(genericStep).firstStepForLogFunctional(
                any(CustomerPersistenceDocumentRequestData.class), any(MetaRequest.class), anyString());
        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        persistenceDocumentController.persistenceDocumentOperation(persistenceDocumentRequest);
        Mockito.verify(this.persistenceDocumentController, Mockito.times(1))
                .persistenceDocumentOperation(persistenceDocumentRequest);
    }


    @SneakyThrows
    @Test
    public void persistenceDocumentListTest() {
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Arrays.asList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").subTypeDocumentary(CEDULA_SUBTYPE).build(),PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").subTypeDocumentary(RUT_SUBTYPE).build());
        List<PersistenceDocument> data = Collections.singletonList(PersistenceDocument.builder()
                .infoReuseCommon(InfoReuseCommon.builder().build()).data(listList).documentType("")
                .initOperation(new Date()).build());
        DocumentPersistenceRetriesRequest persistenceDocumentRequest = new DocumentPersistenceRetriesRequest(data);

        Mockito.doReturn(AcquisitionReply.builder().acquisitionId(uuidCode.toString()).build())
                .when(vinculationUpdateUseCase).validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doNothing().when(persistenceDocumentUseCase).processResponse(any(AcquisitionReply.class),
                any(PersistenceDocument.class), anyBoolean());
        Mockito.doReturn(StepForLogFunctional.builder().build()).when(genericStep).firstStepForLogFunctional(
                any(CustomerPersistenceDocumentRequestData.class), any(MetaRequest.class), anyString());
        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        persistenceDocumentController.persistenceDocumentOperation(persistenceDocumentRequest);
        Mockito.verify(this.persistenceDocumentController, Mockito.times(1))
                .persistenceDocumentOperation(persistenceDocumentRequest);
    }

    @SneakyThrows
    @Test
    public void persistenceDocumentRutTest() {
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Collections.singletonList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").subTypeDocumentary(RUT_SUBTYPE).build());
        List<PersistenceDocument> data = Collections.singletonList(PersistenceDocument.builder()
                .infoReuseCommon(InfoReuseCommon.builder().build()).data(listList).documentType("")
                .initOperation(new Date()).build());
        DocumentPersistenceRetriesRequest persistenceDocumentRequest = new DocumentPersistenceRetriesRequest(data);

        Mockito.doReturn(AcquisitionReply.builder().acquisitionId(uuidCode.toString()).build())
                .when(vinculationUpdateUseCase).validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doNothing().when(persistenceDocumentUseCase).processResponse(any(AcquisitionReply.class),
                any(PersistenceDocument.class), anyBoolean());
        Mockito.doReturn(StepForLogFunctional.builder().build()).when(genericStep).firstStepForLogFunctional(
                any(CustomerPersistenceDocumentRequestData.class), any(MetaRequest.class), anyString());
        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        persistenceDocumentController.persistenceDocumentOperation(persistenceDocumentRequest);
        Mockito.verify(this.persistenceDocumentController, Mockito.times(1))
                .persistenceDocumentOperation(persistenceDocumentRequest);
    }
}
