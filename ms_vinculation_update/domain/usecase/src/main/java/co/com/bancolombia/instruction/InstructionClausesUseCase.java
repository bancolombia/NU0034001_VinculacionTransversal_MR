package co.com.bancolombia.instruction;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixInfoClause;
import co.com.bancolombia.model.acquisition.ClauseInstructionsWithAcquisition;

import java.util.List;

public interface InstructionClausesUseCase {
    public List<MatrixInfoClause> searchClauses(Acquisition acquisition);

    public ClauseInstructionsWithAcquisition searchClausesAndInstructions(
            String acqId, String documentType, String documentNumber, String operation);
}
