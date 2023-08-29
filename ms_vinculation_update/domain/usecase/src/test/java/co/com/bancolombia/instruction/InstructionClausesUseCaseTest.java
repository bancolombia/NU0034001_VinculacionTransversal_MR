package co.com.bancolombia.instruction;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.clause.Clause;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixInfoClause;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.acquisition.ClauseInstructionsWithAcquisition;
import co.com.bancolombia.model.clauseacquisitionchecklist.gateways.ClauseAcquisitionCheckListRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class InstructionClausesUseCaseTest {

    protected static final String CODE_STATE_ACQUISITION_INITIAL = "1";

    @InjectMocks
    @Spy
    private InstructionClausesUseCaseImpl instructionClausesUseCase;

    @Mock
    private AcquisitionValidateUseCase acquisitionValidateUseCase;

    @Mock
    private AcquisitionOperationUseCase acquisitionUseCase;

    @Mock
    private CheckListUseCase checkListUseCase;

    @Mock
    private ClauseAcquisitionCheckListRepository cAcCheckListRepository;

    @Mock
    private InstructionUseCase instructionUseCase;

    @Mock
    private InstructionSearchUseCase instructionSearchUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchClausesAndInstructionsTest() {
        final String idAcq = "935a98b8-e59c-4d36-9f88-7820346a3895";
        final String documentNumber = "1061000000";
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final MatrixAcquisition mAcquisition = MatrixAcquisition.builder().typeAcquisition(typeAcquisition).build();
        final List<MatrixAcquisition> mAcList = Stream.of(mAcquisition).collect(Collectors.toList());
        final List<MatrixInfoClause> clauses = Stream.of(MatrixInfoClause.builder().build()).collect(Collectors.toList());
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition)
                .documentNumber(documentNumber).documentType(documentType)
                .stateAcquisition(stateAcquisition).build();

        Mockito.doReturn(mAcList).when(instructionSearchUseCase).searchInstructions(any(Acquisition.class));
        Mockito.doReturn(Optional.ofNullable(acquisition)).when(acquisitionValidateUseCase)
                .validateInfoSearchAndGet(idAcq, "FS001", "1061000000", "");
        Mockito.doReturn(clauses).when(instructionClausesUseCase).searchClauses(any(Acquisition.class));
        final ClauseInstructionsWithAcquisition clauseInstructionsWithAcquisition = instructionClausesUseCase
                .searchClausesAndInstructions(idAcq, documentType.getCode(),
                        documentNumber, "");
        assertNotNull(clauseInstructionsWithAcquisition);
    }

    @Test
    public void searchClausesTest() {
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Clause clause = Clause.builder().code("").name("").build();
        final Step step = Step.builder().code("").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step).sequence(1).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();

        Mockito.doNothing().when(instructionUseCase).validateAcquisitionInstructions(any(Acquisition.class));
        Mockito.doReturn(Collections.singletonList(clauseAcquisitionCheckList)).when(cAcCheckListRepository)
                .findByAcquisition(any(Acquisition.class));
        List<MatrixInfoClause> clauses = instructionClausesUseCase.searchClauses(acquisition);
        assertNotNull(clauses);
    }

    @Test
    public void searchClausesNotEqualStepTest() {
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Clause clause = Clause.builder().code("").name("").build();
        final Step step = Step.builder().code("").build();
        final Step step2 = Step.builder().code("1").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step).sequence(1).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause2 = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step2).sequence(1).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList2 = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause2).build();
        List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists = new ArrayList<>();
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList);
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList2);

        Mockito.doNothing().when(instructionUseCase).validateAcquisitionInstructions(any(Acquisition.class));
        Mockito.doReturn(clauseAcquisitionCheckLists).when(cAcCheckListRepository)
                .findByAcquisition(any(Acquisition.class));
        List<MatrixInfoClause> clauses = instructionClausesUseCase.searchClauses(acquisition);
        assertNotNull(clauses);
    }

    @Test
    public void searchClausesNotEqualSequenceTest() {
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Clause clause = Clause.builder().code("").name("").build();
        final Step step = Step.builder().code("").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step).sequence(1).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause2 = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step).sequence(2).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList2 = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause2).build();
        List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists = new ArrayList<>();
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList);
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList2);

        Mockito.doNothing().when(instructionUseCase).validateAcquisitionInstructions(any(Acquisition.class));
        Mockito.doReturn(clauseAcquisitionCheckLists).when(cAcCheckListRepository)
                .findByAcquisition(any(Acquisition.class));
        List<MatrixInfoClause> clauses = instructionClausesUseCase.searchClauses(acquisition);
        assertNotNull(clauses);
    }

    @Test
    public void searchClausesNotEqualSequenceAndStepTest() {
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Clause clause = Clause.builder().code("").name("").build();
        final Step step = Step.builder().code("").build();
        final Step step2 = Step.builder().code("2").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step2).sequence(1).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause2 = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step).sequence(2).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList2 = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause2).build();
        List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists = new ArrayList<>();
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList);
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList2);

        Mockito.doNothing().when(instructionUseCase).validateAcquisitionInstructions(any(Acquisition.class));
        Mockito.doReturn(clauseAcquisitionCheckLists).when(cAcCheckListRepository)
                .findByAcquisition(any(Acquisition.class));
        List<MatrixInfoClause> clauses = instructionClausesUseCase.searchClauses(acquisition);
        assertNotNull(clauses);
    }

    @Test
    public void searchClausesNotEqualStepAndClauseTest() {
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Clause clause = Clause.builder().code("").name("").build();
        final Clause clause2 = Clause.builder().code("1").name("").build();
        final Step step = Step.builder().code("").build();
        final Step step2 = Step.builder().code("2").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step2).sequence(1).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause2 = MatrixTypeAcquisitionClause.builder()
                .clause(clause2).step(step).sequence(1).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList2 = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause2).build();
        List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists = new ArrayList<>();
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList);
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList2);

        Mockito.doNothing().when(instructionUseCase).validateAcquisitionInstructions(any(Acquisition.class));
        Mockito.doReturn(clauseAcquisitionCheckLists).when(cAcCheckListRepository)
                .findByAcquisition(any(Acquisition.class));
        List<MatrixInfoClause> clauses = instructionClausesUseCase.searchClauses(acquisition);
        assertNotNull(clauses);
    }

    @Test
    public void searchClausesNotEqualStepAndClauseAndSequenceTest() {
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Clause clause = Clause.builder().code("").name("").build();
        final Clause clause2 = Clause.builder().code("1").name("").build();
        final Step step = Step.builder().code("").build();
        final Step step2 = Step.builder().code("2").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step2).sequence(1).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause2 = MatrixTypeAcquisitionClause.builder()
                .clause(clause2).step(step).sequence(2).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList2 = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause2).build();
        List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists = new ArrayList<>();
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList);
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList2);

        Mockito.doNothing().when(instructionUseCase).validateAcquisitionInstructions(any(Acquisition.class));
        Mockito.doReturn(clauseAcquisitionCheckLists).when(cAcCheckListRepository)
                .findByAcquisition(any(Acquisition.class));
        List<MatrixInfoClause> clauses = instructionClausesUseCase.searchClauses(acquisition);
        assertNotNull(clauses);
    }

    @Test
    public void searchClausesDuplicateTest() {
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Clause clause = Clause.builder().code("").name("").build();
        final Step step = Step.builder().code("").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition).build();
        final MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .clause(clause).step(step).sequence(1).build();
        final ClauseAcquisitionCheckList clauseAcquisitionCheckList = ClauseAcquisitionCheckList.builder()
                .acquisition(acquisition).matrixTypeAcquisitionClause(matrixTypeAcquisitionClause).build();
        List<ClauseAcquisitionCheckList> clauseAcquisitionCheckLists = new ArrayList<>();
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList);
        clauseAcquisitionCheckLists.add(clauseAcquisitionCheckList);

        Mockito.doNothing().when(instructionUseCase).validateAcquisitionInstructions(any(Acquisition.class));
        Mockito.doReturn(clauseAcquisitionCheckLists).when(cAcCheckListRepository)
                .findByAcquisition(any(Acquisition.class));
        List<MatrixInfoClause> clauses = instructionClausesUseCase.searchClauses(acquisition);
        assertNotNull(clauses);
    }

    @Test(expected = ValidationException.class)
    public void searchClausesExceptionTest() {
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition).build();
        Mockito.doNothing().when(instructionUseCase).validateAcquisitionInstructions(any(Acquisition.class));
        Mockito.doReturn(new ArrayList<>()).when(cAcCheckListRepository)
                .findByAcquisition(any(Acquisition.class));
        instructionClausesUseCase.searchClauses(acquisition);
    }

}
