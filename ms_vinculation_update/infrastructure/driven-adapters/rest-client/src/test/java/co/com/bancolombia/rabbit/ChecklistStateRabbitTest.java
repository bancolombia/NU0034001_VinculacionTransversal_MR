package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.dependentfield.DependentField;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
import co.com.bancolombia.model.dependentfield.gateways.DependentFieldRepository;
import co.com.bancolombia.model.execfield.gateways.ExecFieldRepository;
import co.com.bancolombia.model.step.gateways.StepRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_ACQUISITION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ChecklistStateRabbitTest {

    @InjectMocks
    @Spy
    private ChecklistStateRabbit checklistRabbit;

    @Mock
    private AcquisitionUseCase acquisitionUseCase;

    @Mock
    private CheckListRepository checkListRepository;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private ExecFieldRepository execFieldRepository;

    @Mock
    private DependentFieldRepository dependentFieldRepository;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void stateChecklistSuccessTest() {
        Acquisition acquisition = Acquisition.builder().id(uuid).documentNumber("123")
                .documentType(DocumentType.builder().codeHomologation("CC").build())
                .typeAcquisition(TypeAcquisition.builder().code("VT001").build()).build();
        doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));

        Step step = Step.builder().operation(CODE_START_ACQUISITION).build();
        doReturn(Optional.of(step)).when(stepRepository).findByCode(anyString());

        CheckList checkList = CheckList.builder().state(StateStep.builder().name("Completado").code("1").build()).build();
        doReturn(checkList).when(checkListRepository).findByAcquisitionAndStep(any(Acquisition.class), any(Step.class));

        List<ExecField> execFieldList = new ArrayList<>();
        ExecField execField = ExecField.builder()
                .name("firstName").code("FIRSTNAME").mandatory(true).upgradeable(true).build();
        execFieldList.add(execField);
        doReturn(execFieldList).when(execFieldRepository).findByChecklist(any(CheckList.class));

        List<DependentField> dependentFields = new ArrayList<>();
        doReturn(dependentFields).when(dependentFieldRepository).findByTypeAcquisitionAndCurrentOperationAndActive(
                anyString(), anyString(), anyBoolean());

        ChecklistQuery query = ChecklistQuery.builder()
                .acquisitionId(uuid.toString()).operation(CODE_START_ACQUISITION).build();
        ChecklistReply reply = checklistRabbit.stateChecklist(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void stateChecklistNullAcquisitionTest() {
        doReturn(null).when(acquisitionUseCase).findById(any(UUID.class));
        doReturn(Optional.empty()).when(stepRepository).findByCode(anyString());

        ChecklistQuery query = ChecklistQuery.builder()
                .acquisitionId(uuid.toString()).operation(CODE_START_ACQUISITION).build();
        ChecklistReply reply = checklistRabbit.stateChecklist(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void stateChecklistEmptyStateTest() {
        doReturn(Acquisition.builder().build()).when(acquisitionUseCase).findById(any(UUID.class));
        doReturn(Optional.empty()).when(stepRepository).findByCode(anyString());

        ChecklistQuery query = ChecklistQuery.builder()
                .acquisitionId(uuid.toString()).operation(CODE_START_ACQUISITION).build();
        ChecklistReply reply = checklistRabbit.stateChecklist(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void stateChecklistNullQueryTest() {
        ChecklistReply reply = checklistRabbit.stateChecklist(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void stateChecklistNullDataTest() {
        ChecklistQuery query = ChecklistQuery.builder().build();
        ChecklistReply reply = checklistRabbit.stateChecklist(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void stateChecklistEmptyDataTest() {
        ChecklistQuery query = ChecklistQuery.builder().acquisitionId("").operation("").build();
        ChecklistReply reply = checklistRabbit.stateChecklist(query);
        assertFalse(reply.isValid());
    }
}
