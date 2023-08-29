package co.com.bancolombia.model.typeacquisition.gateways;

import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;

import java.util.Optional;

public interface TypeAcquisitionRepository {
    public Optional<TypeAcquisition> findByCode(String code);
}
