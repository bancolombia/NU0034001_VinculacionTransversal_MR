package co.com.bancolombia.field;

import co.com.bancolombia.model.field.Field;

import java.util.Optional;

public interface FieldUseCase {

    /**
     * This function searches the field by its code
     *
     * @param code
     * @return Optional<Field>
     */
    public Optional<Field> findByCode(String code);

    public Field saveField(Field field);
}
