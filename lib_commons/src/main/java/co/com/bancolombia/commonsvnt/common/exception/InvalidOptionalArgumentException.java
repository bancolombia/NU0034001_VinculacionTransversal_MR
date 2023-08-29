package co.com.bancolombia.commonsvnt.common.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.ConstraintViolation;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public class InvalidOptionalArgumentException {

    private String code;
    private String complement;
    private String nameList;
    private List<ConstraintViolation> constraintViolations;
}
