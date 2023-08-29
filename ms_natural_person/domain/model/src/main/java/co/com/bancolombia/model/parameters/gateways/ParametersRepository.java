package co.com.bancolombia.model.parameters.gateways;

import co.com.bancolombia.model.parameters.Parameters;

import java.util.List;
import java.util.Optional;

public interface ParametersRepository {
    Optional<Parameters> findByCodeAndParent(String code, String parent);
    Optional<Parameters> findByNameAndParent(String code, String parent);
    List<Parameters> findByParent(String parent);
}
