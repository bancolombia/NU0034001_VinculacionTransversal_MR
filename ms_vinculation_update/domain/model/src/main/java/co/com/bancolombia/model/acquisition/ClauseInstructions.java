package co.com.bancolombia.model.acquisition;

import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixInfoClause;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class ClauseInstructions {
    List<MatrixInfoClause> clauses;
    List<MatrixInstruction> instructions;
}