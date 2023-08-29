package co.com.bancolombia.model.matrixinstruction;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.instruction.Instruction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class MatrixInstruction extends Auditing {
	private UUID id;
	private MatrixAcquisition matrixAcquisition;
	private Instruction instruction;
	private int sequence;
	private Boolean active;
}
