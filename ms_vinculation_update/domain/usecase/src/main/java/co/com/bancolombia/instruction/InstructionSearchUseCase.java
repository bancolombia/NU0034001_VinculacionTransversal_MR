package co.com.bancolombia.instruction;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;

import java.util.List;

public interface InstructionSearchUseCase {

    /**
     * Search instructions given acquisition, docType and docNumber. Call the func
     * validateInfoSearchAndGet to validate the parameters before calling this
     * function.
     *
     * @param acquisition
     * @return List of matrixInstruction
     */
    public List<MatrixInstruction> searchInstructions(Acquisition acquisition);
}
