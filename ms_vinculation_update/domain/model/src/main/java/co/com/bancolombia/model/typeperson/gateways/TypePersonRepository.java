package co.com.bancolombia.model.typeperson.gateways;

import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;

import java.util.Optional;

public interface TypePersonRepository {

    public Optional<TypePerson> findByCode(String code);
}
