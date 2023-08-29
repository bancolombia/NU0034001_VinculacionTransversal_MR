package co.com.bancolombia.matrixtypeacquisitionclause;

import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.model.matrixtypeacquisitionclause.gateways.MatrixTypeAcquisitionClauseRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class MatrixTypeAcquisitionClauseUseCaseTest {

    @InjectMocks
    @Spy
    private MatrixTypeAcquisitionClauseUseCaseImpl useCase;

    @Mock
    private MatrixTypeAcquisitionClauseRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByTypeAcquisitionAndActiveTest() {
        Mockito.doReturn(new ArrayList<>()).when(repository).findByTypeAcquisitionAndActive(
                any(MatrixTypeAcquisitionClause.class));
        List<MatrixTypeAcquisitionClause> list = useCase
                .findByTypeAcquisitionAndActive(MatrixTypeAcquisitionClause.builder().build());

        assertNotNull(list);
    }
}
