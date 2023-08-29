package co.com.bancolombia.instruction;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.matrixacquisition.MatrixAcquisitionUseCase;
import co.com.bancolombia.matrixinstruction.MatrixInstructionUseCase;
import co.com.bancolombia.model.instruction.Instruction;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class InstructionSearchUseCaseTest {

    protected static final String CODE_STATE_ACQUISITION_INITIAL = "1";
    protected static final String CODE_STATE_STEP_REJECTED = "3";

    @InjectMocks
    @Spy
    private InstructionSearchUseCaseImpl instructionSearchUseCase;

    @Mock
    private AcquisitionOperationUseCase acquisitionOperationUseCase;

    @Mock
    private MatrixAcquisitionUseCase matrixAcquisitionUseCase;

    @Mock
    private MatrixInstructionUseCase matrixInstructionUseCase;

    @Mock
    private CheckListUseCase checkListUseCase;

    @Mock
    protected InstructionUseCase instructionUseCase;

    @Mock
    private AcquisitionUseCase acquisitionUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchInstructionsTest() {
        final String idAcq = "935a98b8-e59c-4d36-9f88-7820346a3895";
        final String documentNumber = "1061000000";
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition)
                .documentNumber(documentNumber).documentType(documentType)
                .stateAcquisition(stateAcquisition).build();
        final MatrixAcquisition mAcquisition = MatrixAcquisition.builder().typeAcquisition(typeAcquisition).build();
        final List<MatrixAcquisition> mAcList = Stream.of(mAcquisition).collect(Collectors.toList());
        final Instruction instruction = Instruction.builder().active(true).code("INS1").name("First instruction")
                .build();
        final MatrixInstruction mInstruction = MatrixInstruction.builder().active(true).instruction(instruction)
                .build();
        final List<MatrixInstruction> mInstructionsList = Stream.of(mInstruction).collect(Collectors.toList());

        when(acquisitionUseCase.findById(UUID.fromString(idAcq))).thenReturn(acquisition);
        when(matrixAcquisitionUseCase.findByTypeAcquisition(typeAcquisition)).thenReturn(mAcList);
        when(matrixInstructionUseCase.findAllByMatrixAcquisition(mAcList)).thenReturn(mInstructionsList);

        final List<MatrixInstruction> expected = instructionSearchUseCase.searchInstructions(acquisition);
        assertEquals(expected, mInstructionsList);
    }

    @Test(expected = RuntimeException.class)
    public void searchInstructionsNoMAcquisitionTest() {
        final String idAcq = "935a98b8-e59c-4d36-9f88-7820346a3895";
        final String documentNumber = "1061000000";
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition)
                .documentNumber(documentNumber).documentType(documentType).build();
        final List<MatrixAcquisition> mAcList = new ArrayList<>();

        when(acquisitionUseCase.findById(UUID.fromString(idAcq))).thenReturn(acquisition);
        when(matrixAcquisitionUseCase.findByTypeAcquisition(typeAcquisition)).thenReturn(mAcList);

        instructionSearchUseCase.searchInstructions(acquisition);
    }

    @Test(expected = CustomException.class)
    public void searchInstructionsCustomExceptionTest() {
        final String idAcq = "935a98b8-e59c-4d36-9f88-7820346a3895";
        final String documentNumber = "1061000000";
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final Acquisition acquisition = Acquisition.builder().documentNumber(documentNumber).documentType(documentType)
                .typeAcquisition(typeAcquisition).stateAcquisition(stateAcquisition)
                .build();
        final List<MatrixAcquisition> mAcList = new ArrayList<>();

        when(acquisitionUseCase.findById(UUID.fromString(idAcq))).thenReturn(acquisition);
        when(matrixAcquisitionUseCase.findByTypeAcquisition(typeAcquisition)).thenReturn(mAcList);

        instructionSearchUseCase.searchInstructions(acquisition);
    }

    @Test(expected = ValidationException.class)
    public void searchInstructionsNoInstructionsTest() {
        final String idAcq = "935a98b8-e59c-4d36-9f88-7820346a3895";
        final String documentNumber = "1061000000";
        final TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
        final DocumentType documentType = DocumentType.builder().code("FS001").build();
        final StateAcquisition stateAcquisition = StateAcquisition.builder().code(CODE_STATE_ACQUISITION_INITIAL)
                .build();
        final StateStep stateStep = StateStep.builder().code(CODE_STATE_STEP_REJECTED).build();
        final Acquisition acquisition = Acquisition.builder().typeAcquisition(typeAcquisition)
                .documentNumber(documentNumber).documentType(documentType)
                .stateAcquisition(stateAcquisition).build();
        final MatrixAcquisition mAcquisition = MatrixAcquisition.builder().typeAcquisition(typeAcquisition).build();
        final List<MatrixAcquisition> mAcList = Stream.of(mAcquisition).collect(Collectors.toList());
        final List<MatrixInstruction> mInstructionsList = new ArrayList<>();

        when(this.acquisitionUseCase.findById(UUID.fromString(idAcq))).thenReturn(acquisition);
        when(this.matrixAcquisitionUseCase.findByTypeAcquisition(typeAcquisition)).thenReturn(mAcList);
        when(this.matrixInstructionUseCase.findAllByMatrixAcquisition(mAcList)).thenReturn(mInstructionsList);

        final List<MatrixInstruction> expected = instructionSearchUseCase.searchInstructions(acquisition);
        assertEquals(expected, mInstructionsList);
    }
}
