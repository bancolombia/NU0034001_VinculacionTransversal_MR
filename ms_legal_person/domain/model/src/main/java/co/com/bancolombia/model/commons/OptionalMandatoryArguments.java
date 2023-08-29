package co.com.bancolombia.model.commons;

import co.com.bancolombia.commonsvnt.common.exception.InvalidOptionalArgumentException;
import co.com.bancolombia.commonsvnt.common.exception.ValidateListException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OptionalMandatoryArguments {

    public static List<InvalidOptionalArgumentException> validArgumentsList(
            Object data, Class<?>[] groups, String code, String complement, String nameList) {
        List<InvalidOptionalArgumentException> listException = new ArrayList<>();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(data, groups);
        if (!constraintViolations.isEmpty()) {
            List<ConstraintViolation> constraintViolationList = new ArrayList<>(constraintViolations);
            listException.add(
                    InvalidOptionalArgumentException.builder()
                            .constraintViolations(constraintViolationList).code(code)
                            .complement(complement).nameList(nameList).build()
            );
        }
        return listException;
    }

    public static void validateException(List<InvalidOptionalArgumentException> list) {
        if (!list.isEmpty()) {
            throw new ValidateListException(list);
        }
    }
}