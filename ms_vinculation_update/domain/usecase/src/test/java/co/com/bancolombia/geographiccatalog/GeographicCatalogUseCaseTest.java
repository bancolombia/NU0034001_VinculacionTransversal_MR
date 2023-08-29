package co.com.bancolombia.geographiccatalog;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.geographiccatalog.GeographicCatalog;
import co.com.bancolombia.model.geographiccatalog.gateways.GeographicCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class GeographicCatalogUseCaseTest {

    @InjectMocks
    @Spy
    private GeographicCatalogUseCaseImpl useCase;

    @Mock
    private GeographicCatalogRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCountryTest() {
        Mockito.doReturn(Optional.of(GeographicCatalog.builder().build())).when(repository)
                .findByCountry("");
        Optional<GeographicCatalog> geographicCatalog = useCase.findByCountry("");
        assertNotNull(geographicCatalog.get());
    }

    @Test
    public void findByCountryDepartmentTest() {
        Mockito.doReturn(Optional.of(GeographicCatalog.builder().build())).when(repository)
                .findByCountryDepartment(anyString(), anyString());
        Optional<GeographicCatalog> geographicCatalog = useCase.findByCountryDepartment("", "");
        assertNotNull(geographicCatalog.get());
    }

    @Test
    public void findByCodeCountryAndCodeCityTest() {
        Mockito.doReturn(Optional.of(GeographicCatalog.builder().build())).when(repository)
                .findByCodeCountryAndCodeCity(anyString(), anyString());
        Optional<GeographicCatalog> geographicCatalog = useCase.findByCodeCountryAndCodeCity(anyString(), anyString());
        assertNotNull(geographicCatalog.get());
    }

    @Test
    public void findByCodeCountryAndCodeDepartmentAndCodeCityTest() {
        Mockito.doReturn(Optional.of(GeographicCatalog.builder().build())).when(repository)
                .findByCodeCountryAndCodeDepartmentAndCodeCity(anyString(), anyString(), anyString());
        Optional<GeographicCatalog> geographicCatalog = useCase
                .findByCodeCountryAndCodeDepartmentAndCodeCity("", "", "");
        assertNotNull(geographicCatalog.get());
    }

    @Test
    public void validateCountryTest() {
        Mockito.doReturn(Optional.of(GeographicCatalog.builder().build())).when(useCase).findByCountry(anyString());
        List<ErrorField> errorFields = this.useCase.validateCountry("", "", "");
        assertNotNull(errorFields);
    }

    @Test
    public void validateCountryNullTest() {
        List<ErrorField> errorFields = this.useCase.validateCountry(null, "", "");
        assertNotNull(errorFields);
    }

    @Test
    public void validateCountryNotPresentTest() {
        Mockito.doReturn(Optional.empty()).when(useCase).findByCountry(anyString());
        List<ErrorField> errorFields = this.useCase.validateCountry("", "", "");
        assertNotNull(errorFields);
    }

    @Test
    public void validateDepartmentTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().codeDepartment("").codeCountry("").build();
        Mockito.doReturn(Optional.of(GeographicCatalog.builder().build())).when(useCase)
                .findByCountryDepartment(anyString(), anyString());
        List<ErrorField> errorFields = this.useCase.validateDepartment(geographicCatalog, "", true);
        assertNotNull(errorFields);
    }

    @Test
    public void validateDepartmentNullTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().build();
        List<ErrorField> errorFields = this.useCase.validateDepartment(geographicCatalog, "", true);
        assertNotNull(errorFields);
    }

    @Test
    public void validateDepartmentNotPresentTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().codeDepartment("").codeCountry("").build();
        Mockito.doReturn(Optional.empty()).when(useCase).findByCountryDepartment(anyString(), anyString());
        List<ErrorField> errorFields = this.useCase.validateDepartment(geographicCatalog, "", true);
        assertNotNull(errorFields);
    }

    @Test
    public void validateDepartmentCountryErrorTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().codeDepartment("").codeCountry("").build();
        List<ErrorField> errorFields = this.useCase.validateDepartment(geographicCatalog, "", false);
        assertNotNull(errorFields);
    }

    @Test
    public void validateCountryCityTest() {
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateCountry(anyString(), anyString(), anyString());
        Mockito.doReturn(Optional.of(GeographicCatalog.builder().build())).when(useCase)
                .findByCodeCountryAndCodeCity(anyString(), anyString());
        List<ErrorField> errorFields = this.useCase.validateCountryCity("", "", "", "");
        assertNotNull(errorFields);
    }

    @Test
    public void validateCountryCityNullTest() {
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateCountry(anyString(), anyString(), anyString());
        List<ErrorField> errorFields = this.useCase.validateCountryCity("", null, "", "");
        assertNotNull(errorFields);
    }

    @Test
    public void validateCountryCityNotPresentTest() {
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateCountry(anyString(), anyString(), anyString());
        Mockito.doReturn(Optional.empty()).when(useCase).findByCodeCountryAndCodeCity(anyString(), anyString());
        List<ErrorField> errorFields = this.useCase.validateCountryCity("", "", "", "");
        assertNotNull(errorFields);
    }

    @Test
    public void validateCountryCityCountryErrorTest() {
        Mockito.doReturn(Collections.singletonList(ErrorField.builder().build())).when(useCase)
                .validateCountry(anyString(), anyString(), anyString());
        List<ErrorField> errorFields = this.useCase.validateCountryCity("", "", "", "");
        assertNotNull(errorFields);
    }

    @Test
    public void validateCityDepartmentCountryTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().codeCity("").codeCountry("").nameCountry("")
                .codeDepartment("").build();
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateCountry(anyString(), anyString(), anyString());
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateDepartment(any(GeographicCatalog.class), anyString(),
                anyBoolean());
        Mockito.doReturn(Optional.of(GeographicCatalog.builder().build())).when(useCase)
                .findByCodeCountryAndCodeDepartmentAndCodeCity(anyString(), anyString(), anyString());
        List<ErrorField> errorFields = this.useCase.validateCityDepartmentCountry("", geographicCatalog);
        assertNotNull(errorFields);
    }

    @Test
    public void validateCityDepartmentCountryNullTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().build();
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateCountry(anyString(), anyString(), anyString());
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateDepartment(any(GeographicCatalog.class), anyString(),
                anyBoolean());
        List<ErrorField> errorFields = this.useCase.validateCityDepartmentCountry("", geographicCatalog);
        assertNotNull(errorFields);
    }

    @Test
    public void validateCityDepartmentCountryNotPresentTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().codeCity("").codeCountry("").nameCountry("")
                .codeDepartment("").build();
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateCountry(anyString(), anyString(), anyString());
        Mockito.doReturn(new ArrayList<>()).when(useCase).validateDepartment(any(GeographicCatalog.class), anyString(),
                anyBoolean());
        Mockito.doReturn(Optional.empty()).when(useCase).findByCodeCountryAndCodeDepartmentAndCodeCity(anyString(),
                anyString(), anyString());
        List<ErrorField> errorFields = this.useCase.validateCityDepartmentCountry("", geographicCatalog);
        assertNotNull(errorFields);
    }

    @Test
    public void validateCityDepartmentCountryCountryErrorTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().codeCity("").codeCountry("").codeDepartment("").build();
        Mockito.doReturn(Collections.singletonList(ErrorField.builder().build())).when(useCase)
                .validateCountry(anyString(), anyString(), anyString());
        Mockito.doReturn(new ArrayList<>()).when(useCase)
                .validateDepartment(any(GeographicCatalog.class), anyString(), anyBoolean());
        List<ErrorField> errorFields = this.useCase.validateCityDepartmentCountry("", geographicCatalog);
        assertNotNull(errorFields);
    }

    @Test
    public void validateCityDepartmentCountryDepartmentErrorTest() {
        GeographicCatalog geographicCatalog = GeographicCatalog.builder().codeCity("").codeCountry("").codeDepartment("").build();
        Mockito.doReturn(new ArrayList<>()).when(useCase)
                .validateCountry(anyString(), anyString(), anyString());
        Mockito.doReturn(Collections.singletonList(ErrorField.builder().build())).when(useCase)
                .validateDepartment(any(GeographicCatalog.class), anyString(), anyBoolean());
        List<ErrorField> errorFields = this.useCase.validateCityDepartmentCountry("", geographicCatalog);
        assertNotNull(errorFields);
    }
}
