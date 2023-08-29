package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.GeographicReply;
import co.com.bancolombia.model.geographiccatalog.GeographicCatalog;
import co.com.bancolombia.model.geographiccatalog.gateways.GeographicCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GeographicFindRabbitTest {

    @InjectMocks
    @Spy
    private GeographicFindRabbit geographicFindRabbit;

    @Mock
    private GeographicCatalogRepository geographicCatalogRepository;

    private GeographicCatalog catalog;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        catalog = GeographicCatalog.builder()
                .codeCity("CIUDAD_05001000").nameCity("MEDELLIN")
                .codeDepartment("DPTO_CO_000005").nameDepartment("ANTIOQUIA")
                .codeCountry("PAIS_CO").nameCountry("COLOMBIA")
                .active(true).build();
    }

    @Test
    public void findGeographicCatalogCitySuccessTest() {
        doReturn(Optional.of(catalog)).when(geographicCatalogRepository).findByCodeCity(anyString());

        GeographicQuery query = GeographicQuery.builder().codeCity("CIUDAD_05001000").codeDepartment("").build();
        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findGeographicCatalogDepartmentSuccessTest() {
        doReturn(Optional.of(catalog)).when(geographicCatalogRepository).findByDepartment(anyString());

        GeographicQuery query = GeographicQuery.builder().codeDepartment("DPTO_CO_000005").codeCountry("").build();
        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findGeographicCatalogCountrySuccessTest() {
        doReturn(Optional.of(catalog)).when(geographicCatalogRepository).findByCountry(anyString());

        GeographicQuery query = GeographicQuery.builder().codeCountry("PAIS_CO").codeCity("").build();
        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findGeographicCatalogCityAndCountrySuccessTest() {
        doReturn(Optional.of(catalog)).when(geographicCatalogRepository).findByCodeCountryAndCodeCity(
                anyString(), anyString());

        GeographicQuery query = GeographicQuery.builder()
                .codeCity("CIUDAD_05001000").codeCountry("PAIS_CO").build();

        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findGeographicCatalogCityAndDepartmentSuccessTest() {
        doReturn(Optional.of(catalog)).when(geographicCatalogRepository).findByCodeDepartmentAndCodeCity(
                anyString(), anyString());

        GeographicQuery query = GeographicQuery.builder()
                .codeCity("CIUDAD_05001000").codeDepartment("DPTO_CO_000005").build();

        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findGeographicCatalogDepartmentAndCountrySuccessTest() {
        doReturn(Optional.of(catalog)).when(geographicCatalogRepository).findByCountryDepartment(
                anyString(), anyString());

        GeographicQuery query = GeographicQuery.builder()
                .codeDepartment("DPTO_CO_000005").codeCountry("PAIS_CO").build();

        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findGeographicCatalogAllFieldsSuccessTest() {
        doReturn(Optional.of(catalog)).when(geographicCatalogRepository).findByCodeCountryAndCodeDepartmentAndCodeCity(
                anyString(), anyString(), anyString());

        GeographicQuery query = GeographicQuery.builder()
                .codeCity("CIUDAD_05001000").codeDepartment("DPTO_CO_000005").codeCountry("PAIS_CO").build();

        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findGeographicCatalogNullSuccessTest() {
        doReturn(Optional.empty()).when(geographicCatalogRepository).findByCodeCity(anyString());

        GeographicQuery query = GeographicQuery.builder().codeCity("CIUDAD_05001000").codeDepartment("").build();
        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findGeographicCatalogNullDataTest() {
        GeographicQuery query = GeographicQuery.builder().build();
        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void findGeographicCatalogEmptyDataTest() {
        GeographicQuery query = GeographicQuery.builder()
                .codeCountry("").codeDepartment("").codeCity("").build();
        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void findGeographicCatalogNullQueryTest() {
        GeographicReply reply = geographicFindRabbit.findGeographicCatalog(null);
        assertFalse(reply.isValid());
    }
}
