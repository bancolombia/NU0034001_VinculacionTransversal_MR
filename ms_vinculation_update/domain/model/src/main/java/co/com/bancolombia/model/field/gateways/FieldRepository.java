package co.com.bancolombia.model.field.gateways;

import co.com.bancolombia.model.field.Field;

import java.util.Optional;

public interface FieldRepository {
    public Optional<Field> findByCode(String code);

    public Field save(Field field);
}
