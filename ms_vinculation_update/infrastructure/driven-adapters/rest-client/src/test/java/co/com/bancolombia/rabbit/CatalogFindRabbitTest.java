package co.com.bancolombia.rabbit;

import co.com.bancolombia.catalog.CatalogUseCase;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.model.catalog.Catalog;
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
public class CatalogFindRabbitTest {

    @InjectMocks
    @Spy
    private CatalogFindRabbit catalogFindRabbit;

    @Mock
    private CatalogUseCase catalogUseCase;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void findCatalogCodeAndParentSuccessTest() {
        Catalog catalog = Catalog.builder().code("GENERO_M").parent("GENERO").name("Masculino").active(true).build();
        doReturn(Optional.of(catalog)).when(catalogUseCase).findByCodeAndParent(anyString(), anyString());

        CatalogQuery query = CatalogQuery.builder().code("GENERO_M").parents("GENERO").build();

        CatalogReply reply = catalogFindRabbit.findCatalogCodeAndParent(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findCatalogCodeAndParentNullSuccessTest() {
        doReturn(Optional.empty()).when(catalogUseCase).findByCodeAndParent(anyString(), anyString());

        CatalogQuery query = CatalogQuery.builder().code("GENERO_R").parents("GENERO").build();

        CatalogReply reply = catalogFindRabbit.findCatalogCodeAndParent(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void findCatalogCodeAndParentNullDataTest() {
        CatalogQuery query = CatalogQuery.builder().build();
        CatalogReply reply = catalogFindRabbit.findCatalogCodeAndParent(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void findCatalogCodeAndParentEmptyDataTest() {
        CatalogQuery query = CatalogQuery.builder().code("").parents("").build();
        CatalogReply reply = catalogFindRabbit.findCatalogCodeAndParent(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void findCatalogCodeAndParentNullQueryTest() {
        CatalogReply reply = catalogFindRabbit.findCatalogCodeAndParent(null);
        assertFalse(reply.isValid());
    }
}
