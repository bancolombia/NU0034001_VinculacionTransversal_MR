package co.com.bancolombia.checklist;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.activity.Activity;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.clause.Clause;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.commonsvnt.model.startlist.StartList;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.execfield.ExecFieldUseCase;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
import co.com.bancolombia.model.clauseacquisitionchecklist.gateways.ClauseAcquisitionCheckListRepository;
import co.com.bancolombia.statestep.StateStepUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class CheckListUseCaseTest {

    @InjectMocks
    @Spy
    private CheckListUseCaseImpl checkListUseCase;

    @Mock
    private AcquisitionValidateUseCase acquisitionValidateUseCase;

    @Mock
    private AcquisitionUseCase acquisitionUseCase;

    @Mock
    private StateStepUseCase stateStepUseCase;

    @Mock
    private CheckListRepository checkListRepository;

    @Mock
    private ExecFieldUseCase execFieldUseCase;

    @Mock
    private ClauseAcquisitionCheckListRepository cAcCheckListRepository;

    private String documentType;
    private String documentNumber;
    private String acquisitionId;
    private Acquisition acquisition;
    private TypeAcquisition typeAcquisition;
    private List<MatrixAcquisition> acquisitionMatrices;
    private List<Step> steps;
    private CheckList checkList;
    private HashMap<String, StateStep> stepStates;
    private CoreFunctionDate coreFunctionDate;
    private StateAcquisition stateAcquisition;
    private DocumentType objDocumentType;
    private List<ExecField> execFieldList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        acquisitionId = "df3b8c71-2911-4977-a66f-d6c2e23e3310";
        documentType = "FS001";
        documentNumber = "1061000000";

        stepStates = new HashMap<String, StateStep>();
        StateStep pending = StateStep.builder().code("1").name("Pendiente").build();
        StateStep completed = StateStep.builder().code("2").name("Completado").build();
        StateStep rejected = StateStep.builder().code("3").name("Rechazado").build();
        StateStep partialCompleted = StateStep.builder().code("7").name("Completado Parcialmente").build();
        stepStates.put("pending", pending);
        stepStates.put("completed", completed);
        stepStates.put("rejected", rejected);
        stepStates.put("partialCompleted", partialCompleted);

        steps = Stream.of(Step.builder()
                .active(true)
                .code("FS")
                .id(UUID.fromString(acquisitionId))
                .name("First step")
                .build()).collect(Collectors.toList());

        typeAcquisition = TypeAcquisition.builder().active(true).code("FFDP").id(UUID.randomUUID()).build();
        acquisitionMatrices = Stream.of(MatrixAcquisition.builder()
                .sequence(1)
                .step(steps.get(0))
                .id(UUID.randomUUID())
                .typeAcquisition(typeAcquisition)
                .build()).collect(Collectors.toList());
        typeAcquisition.setAcquisitionMatrices(acquisitionMatrices);

        coreFunctionDate = new CoreFunctionDate();
        stateAcquisition = StateAcquisition.builder().name("INITIAL").code("1").build();
        objDocumentType = DocumentType.builder().active(true).code(documentType).name("CC").build();

        acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionId))
                .stateAcquisition(stateAcquisition)
                .documentType(objDocumentType)
                .createdDate(coreFunctionDate.getDatetime())
                .typeAcquisition(typeAcquisition)
                .build();

        checkList = CheckList.builder()
                .acquisition(acquisition)
                .id(UUID.randomUUID())
                .step(steps.get(0))
                .state(null)
                .build();

        execFieldList = Stream.of(ExecField.builder()
                .checkList(checkList)
                .code("FField")
                .name("First field")
                .mandatory(true)
                .build()).collect(Collectors.toList());
    }

    @Test
    public void createCheckListTest() {
        when(acquisitionUseCase.findAndValidateById(UUID.fromString(acquisitionId))).thenReturn(acquisition);
        when(checkListRepository.saveAll(anyList())).thenReturn(Stream.of(checkList).collect(Collectors.toList()));
        when(stateStepUseCase.findByCode(stepStates.get("pending").getCode()))
                .thenReturn(Optional.of(stepStates.get("pending")));
        List<CheckList> saved = checkListUseCase.createCheckList(UUID.fromString(acquisitionId), "usrMod");
        assertNotNull(saved);
    }

    @Test(expected = CustomException.class)
    public void createCheckListNoStepStateTest() {
        when(stateStepUseCase.findByCode(stepStates.get("pending").getCode())).thenReturn(Optional.ofNullable(null));
        List<CheckList> saved = checkListUseCase.createCheckList(UUID.fromString(acquisitionId), "usrMod");
        assertNotNull(saved);
    }

    @Test
    public void markOperationTest() {
        when(acquisitionUseCase.findAndValidateById(UUID.fromString(acquisitionId))).thenReturn(acquisition);
        when(stateStepUseCase.findByCode(stepStates.get("pending").getCode()))
                .thenReturn(Optional.of(stepStates.get("pending")));
        when(checkListRepository.findByAcquisitionAndStep(acquisition, steps.get(0))).thenReturn(checkList);
        checkListUseCase.markOperation(
                UUID.fromString(acquisitionId), steps.get(0).getCode(), stepStates.get("pending").getCode());
        verify(checkListUseCase, times(1)).markOperation(
                UUID.fromString(acquisitionId), steps.get(0).getCode(), stepStates.get("pending").getCode());
    }

    @Test(expected = CustomException.class)
    public void markOperationNoMatrixTest() {
        when(acquisitionUseCase.findAndValidateById(UUID.fromString(acquisitionId)))
                .thenReturn(acquisition.toBuilder().typeAcquisition(
                        typeAcquisition.toBuilder().acquisitionMatrices(new ArrayList<>()).build())
                        .build());

        checkListUseCase.markOperation(
                UUID.fromString(acquisitionId), steps.get(0).getCode(), stepStates.get("pending").getCode());
    }

    @Test(expected = CustomException.class)
    public void markOperationNoStepTest() {
        when(acquisitionUseCase.findAndValidateById(UUID.fromString(acquisitionId)))
                .thenReturn(
                        acquisition.toBuilder().typeAcquisition(
                                typeAcquisition.toBuilder()
                                        .acquisitionMatrices(
                                                Stream.of(acquisitionMatrices.get(0).toBuilder()
                                                        .step(null)
                                                        .build()).collect(Collectors.toList()))
                                        .build())
                                .build());

        checkListUseCase.markOperation(
                UUID.fromString(acquisitionId), steps.get(0).getCode(), stepStates.get("pending").getCode());
    }

    @Test(expected = CustomException.class)
    public void markOperationBadStepCodeTest() {
        when(acquisitionUseCase.findAndValidateById(UUID.fromString(acquisitionId)))
                .thenReturn(acquisition.toBuilder().typeAcquisition(
                        typeAcquisition.toBuilder()
                                .acquisitionMatrices(
                                        Stream.of(acquisitionMatrices.get(0).toBuilder()
                                                        .step(steps.get(0).toBuilder().code("TEST").build())
                                                .build()).collect(Collectors.toList()))
                                .build())
                        .build());

        checkListUseCase.markOperation(
                UUID.fromString(acquisitionId), steps.get(0).getCode(), stepStates.get("pending").getCode());
    }

    @Test(expected = CustomException.class)
    public void markOperationNoStepStateTest() {
        when(acquisitionUseCase.findAndValidateById(UUID.fromString(acquisitionId))).thenReturn(acquisition);
        when(stateStepUseCase.findByCode(stepStates.get("pending").getCode())).thenReturn(Optional.ofNullable(null));

        checkListUseCase.markOperation(
                UUID.fromString(acquisitionId), steps.get(0).getCode(), stepStates.get("pending").getCode());
    }

    @Test
    public void getAcquisitionActivitiesTest() {
        StateStep stateStep = StateStep.builder().id(UUID.randomUUID()).code("2").name("Completado").build();
        Step step = Step.builder().id(UUID.randomUUID()).name("Prueba").build();
        CheckList checkList = CheckList.builder().state(stateStep).step(step).build();

        List<CheckList> checkLists = Collections.singletonList(checkList);

        when(acquisitionUseCase.findAndValidateById(any(UUID.class))).thenReturn(acquisition);
        when(checkListRepository.findByAcquisition(any(Acquisition.class))).thenReturn(checkLists);
        doNothing().when(checkListUseCase).markOperation(any(UUID.class), anyString(), anyString());

        List<Activity> activities = checkListUseCase.getAcquisitionActivities(UUID.randomUUID());

        assertNotNull(activities);
    }

    @Test
    public void getAcquisitionActivitiesStateNullTest() {
        Step step = Step.builder().id(UUID.randomUUID()).code("2").name("Completado").build();
        CheckList checkList = CheckList.builder().state(null).step(step).build();

        List<CheckList> checkLists = Collections.singletonList(checkList);

        when(acquisitionUseCase.findAndValidateById(any(UUID.class))).thenReturn(acquisition);
        when(checkListRepository.findByAcquisition(any(Acquisition.class))).thenReturn(checkLists);
        doNothing().when(checkListUseCase).markOperation(any(UUID.class), anyString(), anyString());

        List<Activity> activities = checkListUseCase.getAcquisitionActivities(UUID.randomUUID());

        assertNotNull(activities);
    }

    @Test
    public void getProcessesCheckListTest() {
        List<Acquisition> acquisitions = Collections.singletonList(acquisition);
        Clause clause = Clause.builder().name("").code("").build();
        MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).build();
        ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();
        List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists =
                Collections.singletonList(clauseAcquisitionCheckList);

        when(acquisitionValidateUseCase.getAllByOpAcqIdDocTypeAndDocNum(any(UUID.class), anyString(), anyString(),
                anyString())).thenReturn(acquisitions);
        when(cAcCheckListRepository.findByAcquisition(any(Acquisition.class))).thenReturn(clauseAcquisitionCheckLists);
        doReturn(new ArrayList<>()).when(checkListUseCase).getAcquisitionActivities(any(UUID.class));

        List<StartList> response = checkListUseCase.getProcessesCheckList(acquisition.getId(),
                acquisition.getDocumentType().getCode(), documentNumber, "");

        assertNotNull(response);
    }

    @Test
    public void getProcessesCheckListDuplicateTest() {
        List<Acquisition> acquisitions = Collections.singletonList(acquisition);
        Clause clause = Clause.builder().name("").code("").build();
        Clause clause2 = Clause.builder().name("").code("").build();
        MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).build();
        MatrixTypeAcquisitionClause matrixTypeAcquisitionClause2 = MatrixTypeAcquisitionClause.builder()
                .clause(clause2).build();
        ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();
        ClauseAcquisitionCheckList clauseAcquisitionCheckList2 = ClauseAcquisitionCheckList.builder()
                .matrixTypeAcquisitionClause(matrixTypeAcquisitionClause2).build();
        List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists = new ArrayList<>();
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList);
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList2);

        when(acquisitionValidateUseCase.getAllByOpAcqIdDocTypeAndDocNum(any(UUID.class), anyString(), anyString(),
                anyString())).thenReturn(acquisitions);
        when(cAcCheckListRepository.findByAcquisition(any(Acquisition.class))).thenReturn(clauseAcquisitionCheckLists);
        doReturn(new ArrayList<>()).when(checkListUseCase).getAcquisitionActivities(any(UUID.class));

        List<StartList> response = this.checkListUseCase.getProcessesCheckList(acquisition.getId(),
                acquisition.getDocumentType().getCode(), documentNumber, "");

        assertNotNull(response);
    }
}