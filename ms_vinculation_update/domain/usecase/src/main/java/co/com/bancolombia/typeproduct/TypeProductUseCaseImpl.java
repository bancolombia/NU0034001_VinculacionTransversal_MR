package co.com.bancolombia.typeproduct;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import co.com.bancolombia.model.typeproduct.gateways.TypeProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_TYPE;

@RequiredArgsConstructor
public class TypeProductUseCaseImpl implements TypeProductUseCase {

    private final TypeProductRepository typeProductRepository;

    @Override
    public Optional<TypeProduct> findByCode(String code) {
        return this.typeProductRepository.findByCode(code);
    }

    @Override
    public List<ErrorField> validate(String code, Optional<TypeProduct> oTyPro) {
        List<ErrorField> error = new ArrayList<>();
        if (!oTyPro.isPresent()) {
            error.add(ErrorField.builder().name(code).complement(PRODUCT_TYPE).build());
        }
        return error;
    }

    @Override
    public List<ErrorField> validateActive(Optional<TypeProduct> oTyPro) {
        List<ErrorField> error = new ArrayList<>();
        if (oTyPro.isPresent() && !oTyPro.get().isActive()) {
            error.add(ErrorField.builder().name(oTyPro.get().getCode()).complement(PRODUCT_TYPE).build());
        }
        return error;
    }
}