package co.com.bancolombia.parameters;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ParametersUseCaseImpl implements ParametersUseCase {
    private final ParametersRepository repository;

    @Override
    public Optional<Parameters> findByCodeAndParent(String code, String parent) {
        return this.repository.findByCodeAndParent(code, parent);
    }

    @Override
    public Optional<Parameters> findByNameAndParent(String name, String parent) {
        return this.repository.findByNameAndParent(name, parent);
    }

    @Override
    public List<Parameters> findByParent(String parent) {
        return this.repository.findByParent(parent);
    }

    @Override
    public List<ErrorField> validationMaxRepeat(long countRepeat, String parent) {
        List<ErrorField> errorFieldList = new ArrayList<>();
        List<Parameters> parameters = this.findByParent(parent);
        long countMax = Long.parseLong(parameters.get(0).getCode());
        if (countRepeat >= countMax) {
            errorFieldList.add(ErrorField.builder().build());
        }
        return errorFieldList;
    }
}
