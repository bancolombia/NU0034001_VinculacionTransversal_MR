package co.com.bancolombia.typeperson;

import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;
import co.com.bancolombia.model.typeperson.gateways.TypePersonRepository;
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
public class TypePersonUseCaseTest {

    @InjectMocks
    @Spy
    TypePersonUseCaseImpl typePersonUseCase;

    @Mock
    private TypePersonRepository typePersonRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeTest(){

        TypePerson tp = TypePerson.builder().code("1").build();

        Mockito.doReturn(Optional.of(tp)).when(this.typePersonRepository).findByCode(any(String.class));

        Optional<TypePerson> tpp = this.typePersonUseCase.findByCode("");

        assertNotNull(tpp);

    }
}
