package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistUpdateQuery;
import co.com.bancolombia.model.statestep.gateways.StateStepRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ChecklistUpdateRabbitTest {

    @InjectMocks
    @Spy
    private ChecklistUpdateRabbit update;

    @Mock
    private AcquisitionUseCase acquisitionUseCase;

    @Mock
    private CheckListUseCase checkListUseCase;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private StateStepRepository stateStepRepository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void updateChecklistSuccessTest() {
        Acquisition acquisition = Acquisition.builder().id(uuid).build();
        doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));

        Step step = Step.builder().operation(CODE_START_ACQUISITION).build();
        doReturn(Optional.of(step)).when(stepRepository).findByCode(anyString());

        StateStep stateStep = StateStep.builder().code("1").build();
        doReturn(Optional.of(stateStep)).when(stateStepRepository).findByCode(anyString());

        doNothing().when(checkListUseCase).markOperation(any(UUID.class), anyString(), anyString());

        ChecklistUpdateQuery query = ChecklistUpdateQuery.builder()
                .acquisitionId(uuid.toString()).stateCode("2").stepCode(CODE_START_ACQUISITION).build();
        EmptyReply reply = update.updateChecklistReply(query);

        assertTrue(reply.isValid());
    }

    @Test
    public void updateChecklistAcqNullTest() {
        doReturn(null).when(acquisitionUseCase).findById(any(UUID.class));

        ChecklistUpdateQuery query = ChecklistUpdateQuery.builder()
                .acquisitionId(uuid.toString()).stateCode("2").stepCode(CODE_START_ACQUISITION).build();
        EmptyReply reply = update.updateChecklistReply(query);

        assertFalse(reply.isValid());
    }

    @Test
    public void updateChecklistStateIsNotPresentTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));

        Step step = Step.builder().operation("asd").build();
        doReturn(Optional.of(step)).when(stepRepository).findByCode(anyString());

        doReturn(Optional.empty()).when(stateStepRepository).findByCode(anyString());

        ChecklistUpdateQuery query = ChecklistUpdateQuery.builder()
                .acquisitionId(uuid.toString()).stateCode("2").stepCode(CODE_START_ACQUISITION).build();
        EmptyReply reply = update.updateChecklistReply(query);

        assertFalse(reply.isValid());
    }

    @Test
    public void updateChecklistStepIsNotPresentTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));

        doReturn(Optional.empty()).when(stepRepository).findByCode(anyString());

        ChecklistUpdateQuery query = ChecklistUpdateQuery.builder()
                .acquisitionId(uuid.toString()).stateCode("2").stepCode(CODE_START_ACQUISITION).build();
        EmptyReply reply = update.updateChecklistReply(query);

        assertFalse(reply.isValid());
    }

    @Test
    public void updateChecklistNullQueryTest() {
        EmptyReply reply = update.updateChecklistReply(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateChecklistNullDataTest() {
        ChecklistUpdateQuery query = ChecklistUpdateQuery.builder().build();
        EmptyReply reply = update.updateChecklistReply(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateChecklistEmptyDataTest() {
        ChecklistUpdateQuery query = ChecklistUpdateQuery.builder()
                .acquisitionId("").stepCode("").stateCode("").build();
        EmptyReply reply = update.updateChecklistReply(query);
        assertFalse(reply.isValid());
    }
}
