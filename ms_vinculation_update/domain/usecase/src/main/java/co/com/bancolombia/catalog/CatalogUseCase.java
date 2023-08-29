package co.com.bancolombia.catalog;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.catalog.Catalog;

import java.util.List;
import java.util.Optional;

public interface CatalogUseCase {

    /**
     * This function searches the catalog by its code and assignment
     *
     * @param code
     * @param parents
     * @return Optional<Catalog>
     */
    public Optional<Catalog> findByCodeAndParent(String code, String parents);

    public List<ErrorField> validate(String code, String parents, String field, String nameList);
}
