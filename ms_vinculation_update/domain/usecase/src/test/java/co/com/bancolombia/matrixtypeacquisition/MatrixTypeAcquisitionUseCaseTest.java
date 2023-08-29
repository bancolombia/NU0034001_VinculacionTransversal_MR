package co.com.bancolombia.matrixtypeacquisition;

import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.matrixtypeacquisition.MatrixTypeAcquisition;
import co.com.bancolombia.model.matrixtypeacquisition.gateways.MatrixTypeAcquisitionRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class MatrixTypeAcquisitionUseCaseTest {

    @InjectMocks
    @Spy
    private MatrixTypeAcquisitionUseCaseImpl matrixTypeAcquisitionUseCase;

    @Mock
    private MatrixTypeAcquisitionRepository matrixTypeAcquisitionRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findSearchTest(){
    	MatrixTypeAcquisition pa  = MatrixTypeAcquisition.builder().build();
    	AcquisitionStartObjectModel acquisitionStartObjectModel  = AcquisitionStartObjectModel.builder().build();
    	
        Mockito.doReturn(Optional.of(pa)).when(matrixTypeAcquisitionRepository).search(
                any(AcquisitionStartObjectModel.class));
        Optional<MatrixTypeAcquisition> tcc = matrixTypeAcquisitionUseCase.search(acquisitionStartObjectModel);

        assertNotNull(tcc);
    }

    @Test
    public void saveStepTest() {
        MatrixTypeAcquisition matrixTypeAcquisition = MatrixTypeAcquisition.builder().build();
        Mockito.doReturn(matrixTypeAcquisition).when(matrixTypeAcquisitionRepository).save(
                any(MatrixTypeAcquisition.class));

        matrixTypeAcquisitionUseCase.saveParameterAcquisition(matrixTypeAcquisition);

        verify(matrixTypeAcquisitionRepository, times(1)).save(any(MatrixTypeAcquisition.class));
    }
}
