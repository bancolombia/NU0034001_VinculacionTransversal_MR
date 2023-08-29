package co.com.bancolombia.commonsvnt.model.matrixacquisition;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class MatrixAcquisition extends Auditing {
	private UUID id;
	private TypeAcquisition typeAcquisition;
	private Step step;
	private boolean mandatory;
	private int sequence;
}
