package co.com.bancolombia.catalog;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.catalog.Catalog;
import co.com.bancolombia.model.catalog.gateways.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class CatalogUseCaseTest {

    @InjectMocks
    @Spy
    private CatalogUseCaseImpl catalogUseCase;

    @Mock
    private CatalogRepository catalogRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeAndParentTest() {
        Catalog c = Catalog.builder().code("M").parent("gender").build();
        Mockito.doReturn(Optional.of(c)).when(catalogRepository)
                .findByCodeAndParent(any(String.class), any(String.class));

        Optional<Catalog> cc = this.catalogUseCase.findByCodeAndParent("", "");
        assertNotNull(cc);
    }

    @Test
    public void validateTest() {
        Catalog c = Catalog.builder().code("M").parent("gender").build();
        Mockito.doReturn(Optional.of(c)).when(catalogRepository)
                .findByCodeAndParent(any(String.class), any(String.class));

        List<ErrorField> errors = catalogUseCase.validate("", "","","");
        assertEquals(0, errors.size());
    }

    @Test()
    public void validateNotFoundTest() {
        Catalog c = null;
        Mockito.doReturn(Optional.ofNullable(c)).when(catalogRepository)
                .findByCodeAndParent(any(String.class), any(String.class));

        List<ErrorField> errors = catalogUseCase.validate("", "","","");
        assertEquals(1, errors.size());
    }

    @Test
    public void validateIsNullTest() {
        Catalog c = Catalog.builder().code(null).parent("gender").build();
        Mockito.doReturn(Optional.of(c)).when(catalogRepository)
                .findByCodeAndParent(any(String.class), any(String.class));

        List<ErrorField> errors = catalogUseCase.validate(null, "","","");
        assertEquals(0, errors.size());
    }
}
