package co.com.bancolombia.model.parameter.gateways;

import co.com.bancolombia.model.parameter.Parameter;

import java.util.List;
import java.util.Optional;

public interface ParametersRepository {

    List<Parameter> listAll();

    Parameter save(Parameter parameter);

    List<Parameter> findByParent(String parent);

    Optional<Parameter> findByNameParent(String name, String parent);

    List<Parameter> findByParentTypeAcquisition(String parent, String type);

    void deletedAll();
}
