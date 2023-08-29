package co.com.bancolombia.field;

import co.com.bancolombia.model.field.Field;
import co.com.bancolombia.model.field.gateways.FieldRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class FieldUseCaseImpl implements FieldUseCase{

    private final FieldRepository fieldRepository;

    @Override
    public Optional<Field> findByCode(String code) {
        return this.fieldRepository.findByCode(code);
    }

    @Override
    public Field saveField(Field field) {
        return this.fieldRepository.save(field);
    }
}
