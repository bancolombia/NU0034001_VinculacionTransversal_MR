package co.com.bancolombia.typeperson;

import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;
import co.com.bancolombia.model.typeperson.gateways.TypePersonRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class TypePersonUseCaseImpl implements TypePersonUseCase {

    private final TypePersonRepository typePersonRepository;

    @Override
    public Optional<TypePerson> findByCode(String code) {
        return typePersonRepository.findByCode(code);
    }
}
