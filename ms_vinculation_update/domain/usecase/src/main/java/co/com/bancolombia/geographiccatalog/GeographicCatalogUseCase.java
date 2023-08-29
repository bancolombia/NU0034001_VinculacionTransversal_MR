package co.com.bancolombia.geographiccatalog;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.geographiccatalog.GeographicCatalog;

import java.util.List;
import java.util.Optional;

public interface GeographicCatalogUseCase {

    /**
     * This function searches the country by its code
     *
     * @param codeCountry
     * @return Optional<GeographicCatalog>
     */
    Optional<GeographicCatalog> findByCountry(String codeCountry);

    /**
     * This function searches the department by its code
     *
     * @param codeCountry
     * @param department
     * @return Optional<GeographicCatalog>
     */
    Optional<GeographicCatalog> findByCountryDepartment(String codeCountry, String department);

    /**
     * This function searches the country - city by its code
     *
     * @param codeCountry
     * @param codeCity
     * @return Optional<GeographicCatalog>
     */
    Optional<GeographicCatalog> findByCodeCountryAndCodeCity(String codeCountry, String codeCity);

    /**
     * This function searches the country - department - city by its code
     *
     * @param codeCountry
     * @param codeDepartment
     * @param codeCity
     * @return Optional<GeographicCatalog>
     */
    Optional<GeographicCatalog> findByCodeCountryAndCodeDepartmentAndCodeCity(
            String codeCountry, String codeDepartment, String codeCity);

    /**
     * Validate if a country exists by its code.
     *
     * @param codeCountry
     * @param fieldCountry
     * @param nameList
     * @return List<ErrorField>
     */
    List<ErrorField> validateCountry(String codeCountry, String fieldCountry, String nameList);

    /**
     * Validate id a department exists by its code and the country code.
     *
     * @param iSearch
     * @param nameList
     * @param error
     * @return List<ErrorField>
     */
    List<ErrorField> validateDepartment(GeographicCatalog iSearch, String nameList, boolean error);

    /**
     * Validate if a city exists by its code and the country code.
     *
     * @param codeCountry
     * @param code
     * @param fieldCountry
     * @param field
     * @return List<ErrorField>
     */
    List<ErrorField> validateCountryCity(String codeCountry, String code, String fieldCountry, String field);

    /**
     * Validate if a city exists by its code and the department and country code.
     *
     * @param nameList
     * @param iSearch
     * @return List<ErrorField>
     */
    List<ErrorField> validateCityDepartmentCountry(String nameList, GeographicCatalog iSearch);
}
