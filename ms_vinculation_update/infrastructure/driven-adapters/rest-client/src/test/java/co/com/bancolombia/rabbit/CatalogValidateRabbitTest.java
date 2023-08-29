package co.com.bancolombia.rabbit;

import co.com.bancolombia.catalog.CatalogUseCase;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ValidateCatalogQuery;
import co.com.bancolombia.geographiccatalog.GeographicCatalogUseCase;
import co.com.bancolombia.model.geographiccatalog.GeographicCatalog;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_SIN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class CatalogValidateRabbitTest {

    @InjectMocks
    @Spy
    private CatalogValidateRabbit catalogValidateRabbit;

    @Mock
    private CatalogUseCase catalogUseCase;

    @Mock
    private GeographicCatalogUseCase geographicCatalogUseCase;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void validateCatalogSuccessTest() {
        CatalogQuery catalog1 = CatalogQuery.builder().code("GENERO_M").parents("GENERO").build();
        CatalogQuery catalog2 = CatalogQuery.builder().code("ESTCIV_2").parents("ESTCIVIL").build();

        GeographicQuery geographic1 = GeographicQuery.builder()
                .codeCity("CIUDAD_52405006").codeCountry("PAIS_CO").build();
        GeographicQuery geographic2 = GeographicQuery.builder()
                .codeCity("CIUDAD_52405006").codeDepartment("DPTO_CO_000052").codeCountry("PAIS_CO").build();

        List<CatalogQuery> catalogList = Arrays.asList(catalog1, catalog2);
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        catalog.put(ERROR_CODE_CATALOG_SIN, catalogList);

        List<GeographicQuery> geographicList = Arrays.asList(geographic1, geographic2);
        Map<String, List<GeographicQuery>> geographic = new HashMap<>();
        geographic.put(ERROR_CODE_CATALOG_SIN, geographicList);

        ValidateCatalogQuery query = ValidateCatalogQuery.builder().catalog(catalog).geographic(geographic).build();

        doReturn(Collections.emptyList()).when(catalogUseCase).validate(anyString(), anyString(), any(), any());
        doReturn(Collections.emptyList()).when(geographicCatalogUseCase).validateCountryCity(
                anyString(), anyString(), any(), any());
        doReturn(Collections.emptyList()).when(geographicCatalogUseCase).validateCityDepartmentCountry(
                any(), any(GeographicCatalog.class));

        EmptyReply reply = catalogValidateRabbit.validateCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void validateCatalogSuccessNullGeographicTest() {
        CatalogQuery catalog1 = CatalogQuery.builder().code("GENERO_M").parents("GENERO").build();
        CatalogQuery catalog2 = CatalogQuery.builder().code("ESTCIV_2").parents("ESTCIVIL").build();

        List<CatalogQuery> catalogList = Arrays.asList(catalog1, catalog2);
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        catalog.put(ERROR_CODE_CATALOG_SIN, catalogList);

        ValidateCatalogQuery query = ValidateCatalogQuery.builder().catalog(catalog).build();

        doReturn(Collections.emptyList()).when(catalogUseCase).validate(anyString(), anyString(), any(), any());
        doReturn(Collections.emptyList()).when(geographicCatalogUseCase).validateCountryCity(
                anyString(), anyString(), any(), any());
        doReturn(Collections.emptyList()).when(geographicCatalogUseCase).validateCityDepartmentCountry(
                any(), any(GeographicCatalog.class));

        EmptyReply reply = catalogValidateRabbit.validateCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void validateCatalogSuccessNullCatalogTest() {
        GeographicQuery geographic1 = GeographicQuery.builder()
                .codeCity("CIUDAD_52405006").codeCountry("PAIS_CO").build();
        GeographicQuery geographic2 = GeographicQuery.builder()
                .codeCity("CIUDAD_52405006").codeDepartment("DPTO_CO_000052").codeCountry("PAIS_CO").build();

        List<GeographicQuery> geographicList = Arrays.asList(geographic1, geographic2);
        Map<String, List<GeographicQuery>> geographic = new HashMap<>();
        geographic.put(ERROR_CODE_CATALOG_SIN, geographicList);

        ValidateCatalogQuery query = ValidateCatalogQuery.builder().geographic(geographic).build();

        doReturn(Collections.emptyList()).when(catalogUseCase).validate(anyString(), anyString(), any(), any());
        doReturn(Collections.emptyList()).when(geographicCatalogUseCase).validateCountryCity(
                anyString(), anyString(), any(), any());
        doReturn(Collections.emptyList()).when(geographicCatalogUseCase).validateCityDepartmentCountry(
                any(), any(GeographicCatalog.class));

        EmptyReply reply = catalogValidateRabbit.validateCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void validateCatalogNullDataTest() {
        CatalogQuery catalog1 = CatalogQuery.builder().build();
        CatalogQuery catalog2 = CatalogQuery.builder().build();

        GeographicQuery geographic1 = GeographicQuery.builder().build();
        GeographicQuery geographic2 = GeographicQuery.builder().build();

        List<CatalogQuery> catalogList = Arrays.asList(catalog1, catalog2);
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        catalog.put(ERROR_CODE_CATALOG_SIN, catalogList);

        List<GeographicQuery> geographicList = Arrays.asList(geographic1, geographic2);
        Map<String, List<GeographicQuery>> geographic = new HashMap<>();
        geographic.put(ERROR_CODE_CATALOG_SIN, geographicList);

        ValidateCatalogQuery query = ValidateCatalogQuery.builder().catalog(catalog).geographic(geographic).build();

        EmptyReply reply = catalogValidateRabbit.validateCatalog(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateCatalogEmptyDataTest() {
        CatalogQuery catalog1 = CatalogQuery.builder().code("").parents("").build();
        CatalogQuery catalog2 = CatalogQuery.builder().code("").parents("").build();

        GeographicQuery geographic1 = GeographicQuery.builder().codeCity("").codeCountry("").build();
        GeographicQuery geographic2 = GeographicQuery.builder().codeCity("").codeDepartment("").codeCountry("").build();

        List<CatalogQuery> catalogList = Arrays.asList(catalog1, catalog2);
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        catalog.put(ERROR_CODE_CATALOG_SIN, catalogList);

        List<GeographicQuery> geographicList = Arrays.asList(geographic1, geographic2);
        Map<String, List<GeographicQuery>> geographic = new HashMap<>();
        geographic.put(ERROR_CODE_CATALOG_SIN, geographicList);

        ValidateCatalogQuery query = ValidateCatalogQuery.builder().catalog(catalog).geographic(geographic).build();

        EmptyReply reply = catalogValidateRabbit.validateCatalog(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateCatalogNotValidTest() {
        CatalogQuery catalog1 = CatalogQuery.builder().code("GENERO_M").parents("GENERO").build();
        CatalogQuery catalog2 = CatalogQuery.builder().code("ESTCIV_2").parents("ESTCIVIL").build();

        GeographicQuery geographic1 = GeographicQuery.builder()
                .codeCity("CIUDAD_52405006").codeCountry("PAIS_CO").build();
        GeographicQuery geographic2 = GeographicQuery.builder()
                .codeCity("CIUDAD_52405006").codeDepartment("DPTO_CO_000052").codeCountry("PAIS_CO").build();

        List<CatalogQuery> catalogList = Arrays.asList(catalog1, catalog2);
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        catalog.put(ERROR_CODE_CATALOG_SIN, catalogList);

        List<GeographicQuery> geographicList = Arrays.asList(geographic1, geographic2);
        Map<String, List<GeographicQuery>> geographic = new HashMap<>();
        geographic.put(ERROR_CODE_CATALOG_SIN, geographicList);

        ValidateCatalogQuery query = ValidateCatalogQuery.builder().catalog(catalog).geographic(geographic).build();
        ErrorField error = ErrorField.builder().build();

        doReturn(Collections.singletonList(error)).when(catalogUseCase).validate(
                anyString(), anyString(), any(), any());
        doReturn(Collections.singletonList(error)).when(geographicCatalogUseCase).validateCountryCity(
                anyString(), anyString(), any(), any());
        doReturn(Collections.singletonList(error)).when(geographicCatalogUseCase).validateCityDepartmentCountry(
                any(), any(GeographicCatalog.class));

        EmptyReply reply = catalogValidateRabbit.validateCatalog(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateCatalogNullQueryTest() {
        EmptyReply reply = catalogValidateRabbit.validateCatalog(null);
        assertFalse(reply.isValid());
    }
}
