package co.com.bancolombia.typeacquisition;

import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.typeacquisition.gateways.TypeAcquisitionRepository;
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
public class TypeAcquisitionUseCaseTest {

    @InjectMocks
    @Spy
    TypeAcquisitionUseCaseImpl typeAcquisitionUseCase;

    @Mock
    TypeAcquisitionRepository typeAcquisitionRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void findByCodeTest(){

        TypeAcquisition tc = TypeAcquisition.builder().code("001").active(true).build();

        Mockito.doReturn(Optional.of(tc)).when(this.typeAcquisitionRepository).findByCode(any(String.class));

        Optional<TypeAcquisition> tcc = this.typeAcquisitionUseCase.findByCode("");

        assertNotNull(tcc);

    }


}
