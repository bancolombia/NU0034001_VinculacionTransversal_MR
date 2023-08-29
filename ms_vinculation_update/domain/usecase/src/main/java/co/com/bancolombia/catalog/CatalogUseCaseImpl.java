package co.com.bancolombia.catalog;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.catalog.Catalog;
import co.com.bancolombia.model.catalog.gateways.CatalogRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CatalogUseCaseImpl implements CatalogUseCase {

    private final CatalogRepository catalogRepository;

    @Override
    public Optional<Catalog> findByCodeAndParent(String code, String parents) {
        return this.catalogRepository.findByCodeAndParent(code, parents);
    }

    @Override
    public List<ErrorField> validate(String code, String parents, String field, String nameList) {
        List<ErrorField> errors = new ArrayList<>();
        if (code != null) {
            Optional<Catalog> catalogs = findByCodeAndParent(code, parents);
            if (!catalogs.isPresent()) {
                errors.add(ErrorField.builder().name(code)
                        .complement(field).nameList(nameList).build());
            }
        }
        return errors;
    }
}
