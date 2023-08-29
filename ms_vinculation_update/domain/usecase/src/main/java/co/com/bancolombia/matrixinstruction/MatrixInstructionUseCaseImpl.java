package co.com.bancolombia.matrixinstruction;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.instruction.Instruction;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;
import co.com.bancolombia.model.matrixinstruction.gateways.MatrixInstructionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MatrixInstructionUseCaseImpl implements MatrixInstructionUseCase {
	private final MatrixInstructionRepository matrixInstructionRepository;

	@Override
	public Optional<MatrixInstruction> findByMatrixAcquisitionAndInstruction(MatrixAcquisition matrixAcquisition,
																			 Instruction instruction){
		return this.matrixInstructionRepository.findByMatrixAcquisitionAndInstruction(matrixAcquisition, instruction);
	}

	@Override
	public List<MatrixInstruction> findAllByMatrixAcquisition(List<MatrixAcquisition> matrixAcquisitionList){
		return this.matrixInstructionRepository.findAllByMatrixAcquisition(matrixAcquisitionList);
	}
	
	@Override
	public MatrixInstruction saveMatrixInstruction(MatrixInstruction matrixInstruction) {
		return this.matrixInstructionRepository.save(matrixInstruction);
	}
}
