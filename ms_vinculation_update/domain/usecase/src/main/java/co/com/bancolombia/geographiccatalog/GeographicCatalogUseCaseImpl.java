package co.com.bancolombia.geographiccatalog;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.geographiccatalog.GeographicCatalog;
import co.com.bancolombia.model.geographiccatalog.gateways.GeographicCatalogRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;

@RequiredArgsConstructor
public class GeographicCatalogUseCaseImpl implements GeographicCatalogUseCase {

    private final GeographicCatalogRepository geographicCatalogRepository;

    @Override
    public Optional<GeographicCatalog> findByCountry(String codeCountry) {
        return geographicCatalogRepository.findByCountry(codeCountry);
    }

    @Override
    public Optional<GeographicCatalog> findByCountryDepartment(String codeCountry, String department) {
        return geographicCatalogRepository.findByCountryDepartment(codeCountry, department);
    }

    @Override
    public Optional<GeographicCatalog> findByCodeCountryAndCodeCity(String codeCountry, String codeCity) {
        return geographicCatalogRepository.findByCodeCountryAndCodeCity(codeCountry, codeCity);
    }

    @Override
    public Optional<GeographicCatalog> findByCodeCountryAndCodeDepartmentAndCodeCity(
            String codeCountry, String codeDepartment, String codeCity) {
        return geographicCatalogRepository.findByCodeCountryAndCodeDepartmentAndCodeCity(codeCountry,
                codeDepartment, codeCity);
    }

    @Override
    public List<ErrorField> validateCountry(String codeCountry, String fieldCountry, String nameList) {
        if (codeCountry != null) {
            Optional<GeographicCatalog> country = this.findByCountry(codeCountry);
            if (!country.isPresent()) {
                return Collections.singletonList(ErrorField.builder().name(codeCountry).complement(fieldCountry)
                        .nameList(nameList).build());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<ErrorField> validateDepartment(GeographicCatalog iSearch, String nameList, boolean error) {
        if (iSearch.getCodeDepartment() != null) {
            if (!error) {
                return Collections.singletonList(ErrorField.builder().name(iSearch.getCodeDepartment())
                        .complement(iSearch.getNameDepartment()).nameList(nameList).build());
            } else {
                Optional<GeographicCatalog> department = findByCountryDepartment(iSearch.getCodeCountry(),
                        iSearch.getCodeDepartment());
                if (!department.isPresent()) {
                    return Collections.singletonList(ErrorField.builder().name(iSearch.getCodeDepartment())
                            .complement(iSearch.getNameDepartment()).nameList(nameList).build());
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<ErrorField> validateCountryCity(String codeCountry, String code, String fieldCountry, String field) {
        List<ErrorField> errors = new ArrayList<>(validateCountry(codeCountry, fieldCountry, EMPTY));
        if (code != null) {
            if (!errors.isEmpty()) {
                errors.add(ErrorField.builder().name(code).complement(field).build());
            } else {
                Optional<GeographicCatalog> city = this.findByCodeCountryAndCodeCity(codeCountry, code);
                if (!city.isPresent()) {
                    errors.add(ErrorField.builder().name(code).complement(field).build());
                }
            }
        }
        return errors;
    }

    @Override
    public List<ErrorField> validateCityDepartmentCountry(String nameList, GeographicCatalog iSearch) {
        List<ErrorField> errors = new ArrayList<>(validateCountry(iSearch.getCodeCountry(), iSearch.getNameCountry(),
                nameList));
        errors.addAll(validateDepartment(iSearch, nameList, errors.isEmpty()));
        if (iSearch.getCodeCity() != null) {
            if (!errors.isEmpty()) {
                errors.add(ErrorField.builder().name(iSearch.getCodeCity())
                        .complement(iSearch.getNameCity()).nameList(nameList).build());
            } else {
                Optional<GeographicCatalog> city = findByCodeCountryAndCodeDepartmentAndCodeCity(
                        iSearch.getCodeCountry(), iSearch.getCodeDepartment(), iSearch.getCodeCity());
                if (!city.isPresent()) {
                    errors.add(ErrorField.builder().name(iSearch.getCodeCity())
                            .complement(iSearch.getNameCity()).nameList(nameList).build());
                }
            }
        }
        return errors;
    }
}
