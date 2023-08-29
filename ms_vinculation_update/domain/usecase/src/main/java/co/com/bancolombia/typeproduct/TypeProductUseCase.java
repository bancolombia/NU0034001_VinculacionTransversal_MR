package co.com.bancolombia.typeproduct;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;

import java.util.List;
import java.util.Optional;

public interface TypeProductUseCase {

    public Optional<TypeProduct> findByCode(String code);

    /**
     * This function validates that the code exists
     *
     * @param code,oTyPro
     * @return List<ErrorField>
     */
    public List<ErrorField> validate(String code, Optional<TypeProduct> oTyPro);

    /**
     * This function validates that the code is active
     *
     * @param oTyPro
     * @return List<ErrorField>
     */
    public List<ErrorField> validateActive(Optional<TypeProduct> oTyPro);
}
