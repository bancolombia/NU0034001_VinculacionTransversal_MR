package co.com.bancolombia.matrixinstruction;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.instruction.Instruction;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;

import java.util.List;
import java.util.Optional;

public interface MatrixInstructionUseCase {
    public Optional<MatrixInstruction> findByMatrixAcquisitionAndInstruction(MatrixAcquisition matrixAcquisition,
                                                                             Instruction instruction);

    public List<MatrixInstruction> findAllByMatrixAcquisition(List<MatrixAcquisition> matrixAcquisitionList);

    public MatrixInstruction saveMatrixInstruction(MatrixInstruction matrixInstruction);
}
