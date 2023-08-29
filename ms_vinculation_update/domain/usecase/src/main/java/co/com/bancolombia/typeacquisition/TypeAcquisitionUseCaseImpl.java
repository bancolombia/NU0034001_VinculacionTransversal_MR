package co.com.bancolombia.typeacquisition;

import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.typeacquisition.gateways.TypeAcquisitionRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class TypeAcquisitionUseCaseImpl implements TypeAcquisitionUseCase {

    private final TypeAcquisitionRepository typeAcquisitionRepository;

    @Override
    public Optional<TypeAcquisition> findByCode(String code) {
        return typeAcquisitionRepository.findByCode(code);
    }
}
