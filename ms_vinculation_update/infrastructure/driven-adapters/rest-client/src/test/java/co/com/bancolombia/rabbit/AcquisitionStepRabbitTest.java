package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
import co.com.bancolombia.model.step.gateways.StepRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_ACQUISITION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class AcquisitionStepRabbitTest {

    @InjectMocks
    @Spy
    private AcquisitionStepRabbit acquisitionStepRabbit;

    @Mock
    private AcquisitionUseCase acquisitionUseCase;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private CheckListRepository checkListRepository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void getAcquisitionStateStepByAcquisitionSuccessTest() {
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").name("Activo").build();
        DocumentType documentType = DocumentType.builder().code("FS001").build();
        Acquisition acquisition = Acquisition.builder().id(uuid).documentNumber("123")
                .stateAcquisition(stateAcquisition).documentType(documentType).build();
        doReturn(acquisition).when(acquisitionUseCase).findByIdWitOutState(any(UUID.class));

        Step step = Step.builder().build();
        doReturn(Optional.of(step)).when(stepRepository).findByCode(anyString());

        StateStep stateStep = StateStep.builder().code("2").name("Completado").build();
        CheckList checkList = CheckList.builder().state(stateStep).build();
        doReturn(checkList).when(checkListRepository).findByAcquisitionAndStep(any(Acquisition.class), any(Step.class));

        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId(uuid.toString()).operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getAcquisitionStateStepByDocumentSuccessTest() {
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").name("Activo").build();
        DocumentType documentType = DocumentType.builder().code("FS001").build();
        Acquisition acquisition = Acquisition.builder().id(uuid).documentNumber("123")
                .stateAcquisition(stateAcquisition).documentType(documentType).build();
        doReturn(acquisition).when(acquisitionUseCase).findByDocumentTypeAndDocumentNumber(anyString(), anyString());

        Step step = Step.builder().build();
        doReturn(Optional.of(step)).when(stepRepository).findByCode(anyString());

        StateStep stateStep = StateStep.builder().code("2").name("Completado").build();
        CheckList checkList = CheckList.builder().state(stateStep).build();
        doReturn(checkList).when(checkListRepository).findByAcquisitionAndStep(any(Acquisition.class), any(Step.class));

        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .documentNumber("123").documentType("FS001").operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNullQueryTest() {
        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNullDataTest() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder().build();
        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNullAcquisitionAndDocumentTest() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateEmptyDataTest() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId("").documentType("").documentNumber("").operation("").build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateEmptyAcquisitionAndDocumentTest() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId("").documentType("").documentNumber("").operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNotValidData1Test() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId("").documentType("FS001").documentNumber("").operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNotValidData2Test() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId("").documentType("FS001").documentNumber(null).operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNotValidData3Test() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId("").documentType("").documentNumber("123").operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNotValidData4Test() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId("").documentType(null).documentNumber("123").operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNotValidData5Test() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .documentType("FS001").documentNumber("").operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNotValidData6Test() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .documentType("FS001").documentNumber(null).operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNotValidData7Test() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .documentType("").documentNumber("123").operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNotValidData8Test() {
        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .documentType(null).documentNumber("123").operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNullAcquisitionTest() {
        doReturn(null).when(acquisitionUseCase).findByIdWitOutState(any(UUID.class));

        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId(uuid.toString()).operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateEmptyStepTest() {
        doReturn(Acquisition.builder().build()).when(acquisitionUseCase).findByIdWitOutState(any(UUID.class));
        doReturn(Optional.empty()).when(stepRepository).findByCode(anyString());

        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId(uuid.toString()).operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getAcquisitionStateNullChecklistTest() {
        doReturn(Acquisition.builder().build()).when(acquisitionUseCase).findByIdWitOutState(any(UUID.class));
        doReturn(Optional.of(Step.builder().build())).when(stepRepository).findByCode(anyString());
        doReturn(null).when(checkListRepository).findByAcquisitionAndStep(any(Acquisition.class), any(Step.class));

        AcquisitionStateQuery query = AcquisitionStateQuery.builder()
                .acquisitionId(uuid.toString()).operation(CODE_START_ACQUISITION).build();

        AcquisitionStateReply reply = acquisitionStepRabbit.getAcquisitionStepState(query);
        assertFalse(reply.isValid());
    }
}
