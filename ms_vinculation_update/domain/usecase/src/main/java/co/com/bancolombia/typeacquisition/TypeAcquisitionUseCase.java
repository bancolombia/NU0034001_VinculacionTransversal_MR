package co.com.bancolombia.typeacquisition;

import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;

import java.util.Optional;

public interface TypeAcquisitionUseCase {

    /**
     * This function searches the type acquisition by its code
     *
     * @param code
     * @return Optional<TypeAcquisition>
     */
    public Optional<TypeAcquisition> findByCode(String code);
}
