package co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.clause.Clause;
import co.com.bancolombia.commonsvnt.model.containeraction.ContainerAction;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class MatrixTypeAcquisitionClause  extends Auditing {
    private UUID id;
    private boolean active;
    private int sequence;
    private Clause clause;
    private ContainerAction action;
    private Step step;
    private TypeAcquisition typeAcquisition;
}
