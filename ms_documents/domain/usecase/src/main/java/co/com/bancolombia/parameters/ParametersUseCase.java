package co.com.bancolombia.parameters;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.parameters.Parameters;

import java.util.List;
import java.util.Optional;

public interface ParametersUseCase {
    Optional<Parameters> findByCodeAndParent(String code, String parent);

    Optional<Parameters> findByNameAndParent(String name, String parent);

    List<Parameters> findByParent(String parent);

    /**
     * Validation for maximum field repetition count
     * @param countRepeat
     * @param parent
     * @return
     */
    List<ErrorField> validationMaxRepeat(long countRepeat, String parent);
}
