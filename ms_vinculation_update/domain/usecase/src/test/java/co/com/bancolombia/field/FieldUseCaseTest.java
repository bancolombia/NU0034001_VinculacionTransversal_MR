package co.com.bancolombia.field;

import co.com.bancolombia.model.field.Field;
import co.com.bancolombia.model.field.gateways.FieldRepository;
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
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class FieldUseCaseTest {

    @InjectMocks
    @Spy
    private FieldUseCaseImpl fieldUseCase;

    @Mock
    private FieldRepository fieldRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeTest(){
        Field field = Field.builder().code("001").build();
        Mockito.doReturn(Optional.of(field)).when(fieldRepository).findByCode(any(String.class));
        Optional<Field> tcc = this.fieldUseCase.findByCode("");
        assertNotNull(tcc);
    }

    @Test
    public void saveFieldTest() {
        Field field = Field.builder().build();
        Mockito.doReturn(field).when(fieldRepository).save(any(Field.class));
        fieldUseCase.saveField(field);
        verify(fieldRepository, Mockito.times(1)).save(any(Field.class));
    }
}
