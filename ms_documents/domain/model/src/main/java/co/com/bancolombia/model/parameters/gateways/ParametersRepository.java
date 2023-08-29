package co.com.bancolombia.model.parameters.gateways;

import co.com.bancolombia.model.parameters.Parameters;

import java.util.List;
import java.util.Optional;

public interface ParametersRepository {
    public Optional<Parameters> findByCodeAndParent(String code, String parent);
    public Optional<Parameters> findByNameAndParent(String code, String parent);
    public List<Parameters> findByParent(String parent);
}
