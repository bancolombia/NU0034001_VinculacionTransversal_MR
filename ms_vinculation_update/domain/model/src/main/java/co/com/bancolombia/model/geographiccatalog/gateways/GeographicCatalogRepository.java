package co.com.bancolombia.model.geographiccatalog.gateways;

import co.com.bancolombia.model.geographiccatalog.GeographicCatalog;

import java.util.Optional;

public interface GeographicCatalogRepository {

    public Optional<GeographicCatalog> findByCodeCountryAndCodeDepartmentAndCodeCity(String codeCountry
            , String codeDepartment, String codeCity);

    public Optional<GeographicCatalog> findByCodeDepartmentAndCodeCity(String codeDepartment, String codeCity);

    public Optional<GeographicCatalog> findByCodeCountryAndCodeCity(String codeCountry, String codeCity);

    public Optional<GeographicCatalog> findByCountry(String codeCountry);

    public Optional<GeographicCatalog> findByCountryDepartment(String codeCountry, String codeDepartment);

    public Optional<GeographicCatalog> findByCodeCity(String codeCity);

    public Optional<GeographicCatalog> findByDepartment(String codeDepartment);
}
