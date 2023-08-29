package co.com.bancolombia.typeproduct;

import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import co.com.bancolombia.model.typeproduct.gateways.TypeProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class TypeProductUseCaseTest {

    @InjectMocks
    @Spy
    TypeProductUseCaseImpl typeProductUseCase;

    @Mock
    private TypeProductRepository typeProductRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeTest() {

        TypeProduct tp = TypeProduct.builder().active(true).build();

        Mockito.doReturn(Optional.of(tp)).when(this.typeProductRepository).findByCode(any(String.class));

        Optional<TypeProduct> tpp = this.typeProductUseCase.findByCode("");

        assertNotNull(tpp);

    }

    @Test
    public void validateInactive() {
        TypeProduct tp = TypeProduct.builder().active(false).build();
        this.typeProductUseCase.validateActive(Optional.of(tp));
    }

    @Test
    public void validateNotFound() {
        TypeProduct tp = null;
        this.typeProductUseCase.validate("", Optional.ofNullable(tp));
    }

    @Test
    public void validate() {
        TypeProduct tp = TypeProduct.builder().active(true).build();
        this.typeProductUseCase.validate("", Optional.of(tp));
    }

    @Test
    public void validateActive() {
        TypeProduct tp = TypeProduct.builder().active(true).build();
        this.typeProductUseCase.validateActive(Optional.of(tp));
    }

}

