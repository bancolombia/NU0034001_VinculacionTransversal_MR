package co.com.bancolombia.api.customerdocumentpersistence;

import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_APPLY_SIMULATED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OK_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class CustomerDocumentPersistenceLogControllerTest {

    @InjectMocks
    @Spy
    CustomerDocumentPersistenceLogController logController;

    @Mock
    GenericStep genericStep;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void contextLoads() {
        assertNotNull(logController);
    }

    @Test
    public void finallyStepCCTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Collections.singletonList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).idDocument("").documentNumber("111111").typeDocumentary("").subTypeDocumentary(CEDULA_SUBTYPE)
                .status(OK_STATE).build());
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(listList).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }

    @Test
    public void finallyStepRUTTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Collections.singletonList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).idDocument("").documentNumber("111111").typeDocumentary("").subTypeDocumentary(RUT_SUBTYPE)
                .status(OK_STATE).build());
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(listList).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }

    @Test
    public void finallyStepCCStateTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Collections.singletonList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).idDocument("").documentNumber("111111").typeDocumentary("").subTypeDocumentary(CEDULA_SUBTYPE)
                .status("").build());
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(listList).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }

    @Test
    public void finallyStepRUTStateTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Collections.singletonList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).idDocument("").documentNumber("111111").typeDocumentary("").subTypeDocumentary(RUT_SUBTYPE)
                .status("").build());
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(listList).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }

    @Test
    public void finallyStepAllTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Arrays.asList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").typeDocumentary("").subTypeDocumentary(CEDULA_SUBTYPE)
                .status(OK_STATE).idDocument("").build(), PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").typeDocumentary("").subTypeDocumentary("")
                .status(OK_STATE).idDocument("").build());
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(listList).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }

    @Test
    public void finallyStepOtherTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Arrays.asList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").typeDocumentary("").subTypeDocumentary(CEDULA_SUBTYPE)
                .status(ERROR_STATE).errorCode("").errorDescription("").idDocument("").build(), PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").typeDocumentary("").subTypeDocumentary("")
                .status(NO_APPLY_SIMULATED).errorCode("").errorDescription("").idDocument(NO_APPLY_SIMULATED).build());
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(listList).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }

    @Test
    public void finallyStepOtherErrorTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Arrays.asList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").typeDocumentary("").subTypeDocumentary(CEDULA_SUBTYPE)
                .status(OK_STATE).errorCode("").errorDescription("").idDocument("").build(), PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").typeDocumentary("").subTypeDocumentary("")
                .status(ERROR_STATE).errorCode("").errorDescription("").idDocument("").build());
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(listList).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }

    @Test
    public void finallyStepOtherErrorTwoTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        UUID uuidCode = UUID.randomUUID();
        List<PersistenceDocumentList> listList = Arrays.asList(PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").typeDocumentary("").subTypeDocumentary(CEDULA_SUBTYPE)
                .status(ERROR_STATE).errorCode("").errorDescription("").idDocument("").build(), PersistenceDocumentList.builder()
                .acquisitionId(uuidCode).documentNumber("111111").typeDocumentary("").subTypeDocumentary("")
                .status(ERROR_STATE).errorCode("").errorDescription("").idDocument("").build());
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(listList).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }

    @Test
    public void finallyStepListNullTest() {
        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().infoReuseCommon(info)
                .data(null).build();

        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class),
                anyString(), any(InfoReuseCommon.class), anyString());

        logController.finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);

        Mockito.verify(this.logController, Mockito.times(1))
                .finallyStep(stepForLogFunctional,acquisitionReply,persistenceDocument);
    }
}
