package co.com.bancolombia.typeperson;

import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;

import java.util.Optional;

public interface TypePersonUseCase {

    /**
     * This function searches the person by its code
     *
     * @param code
     * @return Optional<TypePerson>
     */
    public Optional<TypePerson> findByCode(String code);
}
