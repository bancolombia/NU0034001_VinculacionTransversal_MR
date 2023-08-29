package co.com.bancolombia.matrixinstruction;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.instruction.Instruction;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;
import co.com.bancolombia.model.matrixinstruction.gateways.MatrixInstructionRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class MatrixInstructionUseCaseTest {

	@InjectMocks
	@Spy
	private MatrixInstructionUseCaseImpl matrixInstructionUseCase;

	@Mock
	private MatrixInstructionRepository matrixInstructionRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findByMatrixAcquisitionAndInstructionTest() {
		MatrixInstruction mai = MatrixInstruction.builder().build();
		MatrixAcquisition matrixAcquisition = MatrixAcquisition.builder().build();
		Instruction instruction = Instruction.builder().build();

		doReturn(Optional.of(mai)).when(matrixInstructionRepository).findByMatrixAcquisitionAndInstruction(
				any(MatrixAcquisition.class), any(Instruction.class));
		Optional<MatrixInstruction> oac = matrixInstructionUseCase.findByMatrixAcquisitionAndInstruction(
				matrixAcquisition, instruction);

		assertNotNull(oac);
	}

	@Test
	public void findAllByMatrixAcquisitionTest() {
		List<MatrixInstruction>  mai = Collections.singletonList(MatrixInstruction.builder().build());
		List<MatrixAcquisition> matrixAcquisition = Collections.singletonList(MatrixAcquisition.builder().build());

		doReturn(mai).when(matrixInstructionRepository).findAllByMatrixAcquisition(new ArrayList<>());
		List<MatrixInstruction> oac = matrixInstructionUseCase.findAllByMatrixAcquisition(matrixAcquisition);

		assertNotNull(oac);
	}

	@Test
	public void saveMatrixInstructionTest() {
		MatrixInstruction mai = MatrixInstruction.builder().build();

		doReturn(mai).when(matrixInstructionRepository).save(any(MatrixInstruction.class));
		matrixInstructionUseCase.saveMatrixInstruction(mai);

		verify(matrixInstructionRepository, times(1)).save(any(MatrixInstruction.class));
	}
}
