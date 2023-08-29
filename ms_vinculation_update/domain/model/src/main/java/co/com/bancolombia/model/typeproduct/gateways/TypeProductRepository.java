package co.com.bancolombia.model.typeproduct.gateways;

import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;

import java.util.Optional;

public interface TypeProductRepository {

    public Optional<TypeProduct> findByCode(String code);
}
