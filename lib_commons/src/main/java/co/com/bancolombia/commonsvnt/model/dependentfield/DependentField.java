package co.com.bancolombia.commonsvnt.model.dependentfield;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DependentField {
    private UUID id;
    private String typeAcquisition;
    private String fieldDependent;
    private String dependentValue;
    private String dependentTable;
    private String dependentOperation;
    private String logic;
    private String currentField;
    private String currentValue;
    private String currentTable;
    private String currentOperation;
    private boolean mandatory;
    private boolean active;
}