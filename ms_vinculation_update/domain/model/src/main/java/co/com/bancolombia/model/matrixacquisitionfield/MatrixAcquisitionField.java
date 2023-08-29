package co.com.bancolombia.model.matrixacquisitionfield;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.commons.Auditing;
import co.com.bancolombia.model.field.Field;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class MatrixAcquisitionField extends Auditing {
    private UUID id;
    private MatrixAcquisition matrixAcquisition;
    private Field field;
    private boolean mandatory;
    private boolean upgradeable;
    private boolean active;
}