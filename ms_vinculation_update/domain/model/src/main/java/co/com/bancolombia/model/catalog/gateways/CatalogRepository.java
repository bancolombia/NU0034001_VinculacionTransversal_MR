package co.com.bancolombia.model.catalog.gateways;

import co.com.bancolombia.model.catalog.Catalog;

import java.util.Optional;

public interface CatalogRepository {

    public Optional<Catalog> findByCodeAndParent(String code, String parent);
}
