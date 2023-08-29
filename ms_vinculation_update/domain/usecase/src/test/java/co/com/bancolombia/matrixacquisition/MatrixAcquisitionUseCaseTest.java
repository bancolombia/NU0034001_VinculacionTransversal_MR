package co.com.bancolombia.matrixacquisition;

import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.matrixacquisition.gateways.MatrixAcquisitionRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class MatrixAcquisitionUseCaseTest {

    @InjectMocks
    @Spy
    private MatrixAcquisitionUseCaseImpl matrixAcquisitionUseCase;

    @Mock
    MatrixAcquisitionRepository matrixAcquisitionRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveMatrixAcquisitionTest() {
        MatrixAcquisition matrixAcquisition = MatrixAcquisition.builder().build();
        Mockito.doReturn(matrixAcquisition).when(matrixAcquisitionRepository).save(any(MatrixAcquisition.class));
        matrixAcquisitionUseCase.saveMatrixAcquisition(matrixAcquisition);
        verify(matrixAcquisitionRepository, times(1)).save(any(MatrixAcquisition.class));
    }

    @Test
    public void findAllByTypeAcquisitionTest(){
    	TypeAcquisition typeAcquisition = TypeAcquisition.builder().build();
    	List<MatrixAcquisition>  list = Collections.singletonList(MatrixAcquisition.builder().build());
    	doReturn(list).when(matrixAcquisitionRepository).findByTypeAcquisition(any(TypeAcquisition.class));
		List<MatrixAcquisition> oac = matrixAcquisitionUseCase.findByTypeAcquisition(typeAcquisition);
		assertNotNull(oac);
    }
}
