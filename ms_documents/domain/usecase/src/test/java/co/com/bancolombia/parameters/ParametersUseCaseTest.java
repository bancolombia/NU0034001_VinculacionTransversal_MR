package co.com.bancolombia.parameters;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ParametersUseCaseTest {

    @InjectMocks
    @Spy
    private ParametersUseCaseImpl useCase;

    @Mock
    private ParametersRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeAndParentTest(){
        Parameters parameters = Parameters.builder().code("CO").name("Colombia").parent("countryResidence").build();
        doReturn(Optional.of(parameters)).when(repository).findByCodeAndParent(any(String.class), any(String.class));

        Optional<Parameters> pa = useCase.findByCodeAndParent("","");
        assertNotNull(pa);
    }

    @Test
    public void findByNameAndParentTest(){
        Parameters parameters = Parameters.builder().code("CO").name("Colombia").parent("countryResidence").build();
        doReturn(Optional.of(parameters)).when(repository).findByCodeAndParent(any(String.class), any(String.class));

        Optional<Parameters> pa = useCase.findByNameAndParent("","");
        assertNotNull(pa);
    }

    @Test
    public void findByParentTest(){
        doReturn(new ArrayList<>()).when(repository).findByParent(any(String.class));

        List<Parameters> lPa = useCase.findByParent("");
        assertNotNull(lPa);
    }

    @Test
    public void validationMaxRepeatSuccessTest(){
        Parameters parameters = Parameters.builder().code("1").name("cellphone").parent("maxCellphone").build();
        doReturn(Collections.singletonList(parameters)).when(repository).findByParent(any(String.class));

        List<ErrorField> errorFields = useCase.validationMaxRepeat(0,"maxCellphone");
        assertNotNull(errorFields);
    }

    @Test
    public void validationMaxRepeatErrorTest(){
        Parameters parameters = Parameters.builder().code("1").name("cellphone").parent("maxCellphone").build();
        doReturn(Collections.singletonList(parameters)).when(repository).findByParent(any(String.class));

        List<ErrorField> errorFields = useCase.validationMaxRepeat(1,"maxCellphone");
        assertNotNull(errorFields);
    }
}
